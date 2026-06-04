## Context

当前 UPMS 模块的菜单体系使用三值 scope（SYSTEM/TENANT/SYSTEM_TENANT）控制可见性，租户菜单通过 `sys_tenant.menu_id` 逗号字符串手动分配，没有 App 概念。新设计（详见 `docs/07-product/menu-app-model.md`）引入 App 作为菜单组合包，采用声明式分发模型。本 change 将新设计落地到 kbpd-upms 模块。

## Goals / Non-Goals

**Goals:**

- 将 `sys_menu.scope` 替换为 `sys_menu.level`（PLATFORM/TENANT），消除 SYSTEM_TENANT 的歧义
- 新增 `sys_app` + `sys_app_menu` + `sys_tenant_app` 三张表，建立 App 管理和订阅体系
- 实现 kava-base 系统应用预设，所有租户自动关联
- 重写菜单可见性链路：基于 App 购买关系而非手动菜单分配
- 角色分配菜单时增加 App 购买范围约束
- 租户创建流程改为自动关联 kava-base

**Non-Goals:**

- App 市场 UI 和支付/计费系统（仅后端模型和 API）
- OAuth Client 自动创建（App 实例的 Client 创建仍手动）
- 第三方开发者接入（当前所有 App 由平台方开发）
- App 间数据联动策略
- C 端 App 前端相关

## Decisions

### D1: App 独立建表（sys_app），不扩展 sys_oauth_client

Because: kava-base 系统应用没有 OAuth Client（它是管理后台本身），如果扩展 OAuth Client 则 kava-base 要么造一个假 Client，要么走特殊逻辑。App 是产品定义（菜单组合），OAuth Client 是认证实例（密钥、回调），一个 App 对应多个 Client（每租户一个），职责不同。多一张表但消除所有特殊处理。

### D2: 菜单 level 两个值（PLATFORM/TENANT），共享菜单拆成两条记录

Because: 三值 scope 中 SYSTEM_TENANT 语义模糊——"双方可见"到底意味着什么数据权限不明确。拆成两条记录后，PLATFORM 和 TENANT 各自独立，可指向相同前端组件，后端根据用户身份返回不同范围的数据。语义清晰，无歧义。

### D3: 声明式分发，sys_tenant_app 关系表

Because: 菜单不需要"推送"操作。菜单始终存在于 sys_menu 表中，通过 sys_tenant_app 的购买关系控制可见性——声明式而非命令式。租户购买 App → 写入 sys_tenant_app → 查询逻辑自动包含该 App 的菜单。到期时 status → EXPIRED → 查询自动排除。

### D4: kava-base 系统应用预设

Because: 即使没有业务 App，平台也有内置管理菜单。kava-base 作为系统应用，创建租户时自动关联（写入 sys_tenant_app），包含 PLATFORM 级和 TENANT 级基础菜单。这使得菜单可见性查询逻辑完全统一——所有 App 都走同一条查询链路，无特殊分支。

### D5: App-菜单多对多（sys_app_menu），支持菜单复用

Because: 同一个菜单（如"会员管理"）可能被多个 App 使用。如果菜单属于单个 App，则多 App 共享菜单时需重复创建。多对多关联自然解决复用问题，租户购买多个 App 时查询菜单并集自动去重。

### D6: OAuth Client 可选关联 app_id（外键）

Because: 当租户购买了某个 App，为其创建 OAuth Client 实例时，标记该 Client 属于哪个 App 的实例，便于后续查询和管理。但这是可选的——B 端管理后台的 Client 不需要关联 App。当前阶段不强制，作为可选字段。

### D7: SysApp 和 SysTenantApp 的 DDD 分层策略

Because: SysApp 是平台级资源（无 tenant_id），由平台管理员管理，使用 `IBaseSimpleRepository` 单一 Repository 模式（与 Menu 和 Tenant 一致）。SysTenantApp 是租户级关联数据，由于涉及状态生命周期（ACTIVE/EXPIRED），使用 DomainService 封装业务规则（购买校验、到期判定、去重）。

### D8: 数据迁移策略

Because: 已有 sys_menu 数据中 scope=SYSTEM 映射为 level=PLATFORM，scope=TENANT 映射为 level=TENANT，scope=SYSTEM_TENANT 需要拆分为两条记录（一条 PLATFORM，一条 TENANT，相同组件路径）。sys_tenant.menu_id 需迁移到 sys_tenant_app（所有租户默认关联 kava-base）。迁移脚本作为 SQL incremental migration 提供。

## Risks / Trade-offs

**[SYSTEM_TENANT 拆分复杂度]** → 通过 SQL 迁移脚本处理，拆分时复制记录并改 level，同时更新 sys_role_menu 中引用旧菜单 ID 的记录（TENANT 版本使用新 ID）。

**[菜单可见性查询性能]** → 新链路涉及 sys_tenant_app → sys_app_menu → sys_menu 多表关联。缓解：查询结果可缓存（已购 App 的菜单集合变更频率低），且查询量有限（登录时获取一次菜单树）。

**[scope → level 字段重命名影响面大]** → 全链路替换（Domain → Infra → App → Adapter → API），但都是机械性替换，风险可控。先改枚举和实体，再逐层向上替换。

**[kava-base 预设数据的初始化时机]** → 通过 SQL 脚本预设 sys_app 记录和 sys_app_menu 关联，不硬编码在业务逻辑中。应用启动时不自动创建，依赖初始化脚本。
