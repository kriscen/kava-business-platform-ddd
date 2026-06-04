## Why

当前菜单的 `scope` 字段（SYSTEM/TENANT/SYSTEM_TENANT）语义模糊，租户菜单通过 `sys_tenant.menu_id` 逗号字符串手动分配，缺乏 App 概念。随着平台进入 App 市场阶段，需要一套声明式的菜单分发模型：App 作为菜单组合包，租户购买 App 后自动获得对应菜单能力，而非手动推送。设计决策详见 `docs/07-product/menu-app-model.md`。

## What Changes

- **BREAKING** `sys_menu.scope` 字段替换为 `sys_menu.level`（PLATFORM/TENANT 两个值），移除 `SYSTEM_TENANT`，共享菜单拆成两条记录
- **BREAKING** 移除 `sys_tenant.menu_id` 逗号字符串，改用 `sys_tenant_app` 关系表声明式管理
- 新增 `sys_app` 表：App 产品定义（code, name, icon, status）
- 新增 `sys_app_menu` 表：App-菜单多对多关联
- 新增 `sys_tenant_app` 表：租户购买/订阅关系（status: ACTIVE/EXPIRED）
- 新增 `SysApp` 完整 DDD 链路（Entity → DomainService → Repository → AppService → Controller）
- 新增 `SysTenantApp` 管理链路（购买、到期、查询已购 App）
- 预设 `kava-base` 系统应用，所有租户自动关联
- 菜单可见性查询重写：平台管理员 → 所有 PLATFORM 菜单；租户管理员 → 已购 App 的 TENANT 菜单并集
- 角色分配菜单时增加 App 购买范围约束：租户角色只能选 (kava-base ∪ 已购App) 范围内的菜单
- OAuth Client 可选关联 `app_id`，标记该 Client 属于哪个 App 的租户实例

## Capabilities

### New Capabilities

- `app-management`: App 产品管理 — SysApp CRUD、App-菜单关联管理、kava-base 系统应用预设
- `tenant-app-subscription`: 租户-App 订阅管理 — 购买/退订、订阅状态生命周期、已购 App 查询

### Modified Capabilities

- `menu-domain-service`: scope → level 字段替换，枚举值域从三个改为两个
- `permission-system`: 菜单可见性链路重写（基于 App 购买关系），角色菜单分配增加 App 范围约束，scope → level
- `tenant-system`: 移除 menuId 逗号字符串，创建租户时自动关联 kava-base 而非手动分配菜单
- `role-menu-binding`: 角色分配菜单时需过滤 App 购买范围
- `oauth-client-domain-service`: 可选关联 app_id 外键

## Impact

**模块**：kbpd-upms（所有改动局限于此模块）

**受影响的关键文件**：
- `docs/01-sql/kbpd-upms.sql` — 新增 3 张表，修改 sys_menu 字段，移除 sys_tenant.menu_id
- `kbpd-upms-types` — SysMenuScope → SysMenuLevel，新增 SysAppStatus、SysTenantAppStatus 枚举
- `kbpd-upms-domain` — SysMenuEntity.scope → level，新增 SysAppEntity/SysTenantAppEntity，调整 SysTenantEntity 移除 menuId
- `kbpd-upms-infrastructure` — 新增 SysAppPO/SysTenantAppPO/SysAppMenuPO + Mapper + Converter，调整现有 Converter
- `kbpd-upms-application` — 新增 App/TenantApp Command/DTO/AppService，调整 Menu/Tenant AppService 查询逻辑
- `kbpd-upms-adapter` — 新增 SysAppController/SysTenantAppController，调整 Menu/Tenant Controller
- `kbpd-upms-api` — Request/Response DTO 中 scope → level，新增 App 相关 DTO

**数据迁移**：sys_menu 表 scope → level 值映射 + SYSTEM_TENANT 拆分，sys_tenant.menu_id → sys_tenant_app 迁移
