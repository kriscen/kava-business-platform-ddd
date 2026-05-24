## Context

Tenant 子域是 Kava 平台的核心（一个家庭 = 一个租户），当前存在以下问题：

1. **Application 层数据管道断裂**：`SysTenantCreateCommand`、`SysTenantUpdateCommand`、`SysTenantAppDetailDTO`、`SysTenantAppListDTO` 全部为空类，MapStruct 转换后所有字段为 null，数据在 create/update/query 时静默丢失
2. **Domain 层无业务逻辑**：`SysTenantService` 纯透传 Repository，无编码唯一性校验、无状态管理
3. **status 字段类型不安全**：Entity 和 PO 中 `status` 为裸 `String`（"0" 正常，"9" 停用），无枚举约束
4. **PO 缺少字段**：`SysTenantPO` 没有 `startTime`、`endTime` 列，Entity 中定义了但无法持久化
5. **管理员用户未创建**：Spec 中定义了"创建租户时自动生成管理员用户"，但仅实现了角色初始化（`initTenantAdminRole`），未创建用户

当前已实现：租户 CRUD 骨架、角色初始化（`initTenantAdminRole`）、租户数据隔离拦截器。

## Goals / Non-Goals

**Goals:**

- 补齐 Application 层 Commands/DTOs 字段，打通数据流转管道
- 在 Domain 层实现租户编码唯一性校验
- 引入 `SysTenantStatus` 枚举替代裸 String，实现状态流转校验
- 补齐 PO 层 `startTime`/`endTime` 字段，支持租户到期判定
- 实现创建租户时可选生成管理员用户（用户创建 + 角色绑定）
- 暴露 Dubbo RPC 供 Auth 模块查询租户状态

**Non-Goals:**

- 不涉及 Auth 模块的登录拦截改造（那是 `authentication-flow` 的范畴，本 change 仅暴露 RPC 接口供其调用）
- 不涉及租户菜单分配逻辑的改动（已实现）
- 不涉及前端 UI
- 不涉及数据库迁移脚本以外的 schema 变更

## Decisions

### D1: Tenant 不升级为聚合根

**决策**：保持 `SysTenantEntity` 为 Entity，不升级为 AggregateRoot，不拆分 Read/Write Repository。

**Because**：Tenant 的关联操作（角色初始化、用户创建）由 AppService 编排，Tenant 自身不持有子集合（不像 User 持有 roleIds、Role 持有 menuIds）。当前 `IBaseSimpleRepository` 足以满足需求，无需引入 CQRS 复杂度。

### D2: Domain Service 承载业务规则

**决策**：在 `SysTenantService` 中实现编码唯一性校验和状态流转校验，AppService 仅做编排。

**Because**：遵循项目 DDD 规范——业务规则收入 Domain 层，AppService 负责 `@Transactional` 编排。编码唯一性和状态流转是纯业务逻辑，不属于编排关注点。

### D3: SysTenantStatus 枚举放在 types 层

**决策**：新增 `SysTenantStatus` 枚举放在 `kbpd-upms-types` 模块。

**Because**：`SysMenuScope`、`SysRoleDataScope` 等枚举均放在 types 层，保持一致。若未来跨模块共享（如 Auth 需要引用），再按需提升到 common-core。

### D4: 到期判定采用查询时实时计算

**决策**：不引入定时任务刷状态，而是在查询和 RPC 调用时实时比较 `endTime` 与当前时间。

**Because**：当前阶段租户数量少，定时任务引入额外基础设施成本。实时计算简单可靠，与"租户到期 → 登录拦截"的场景天然匹配。数据库中 `status` 字段保持手动管理（NORMAL/DISABLED），到期状态通过计算得出。

### D5: 管理员用户创建放在 AppService 编排

**决策**：在 `SysTenantAppService.createTenant()` 中编排：创建租户 → 初始化角色 → 可选创建管理员用户 → 绑定用户角色。

**Because**：涉及跨聚合写操作（创建 User 聚合 + 创建 Role 聚合 + 更新 Tenant 实体），按 DDD 规范应在 AppService 的 `@Transactional` 中编排。DomainService 不跨聚合写。

### D6: RPC 接口设计

**决策**：在已有 `IRemoteTenantService`（如不存在则新建）中新增 `checkTenantStatus(Long tenantId)` 方法，返回租户状态信息。

**Because**：Auth 模块在认证时需要知道租户是否可用（正常/停用/到期）。通过 Dubbo RPC 调用 UPMS 查询是最直接的方式，避免 Auth 模块直接访问 UPMS 数据库。

## Risks / Trade-offs

- **[数据库变更]** PO 新增 `startTime`/`endTime` 列需要 ALTER TABLE → 提供 SQL 迁移脚本，向后兼容（允许 NULL）
- **[空类修复范围大]** Commands/DTOs 从空类变为有字段类，可能影响已有调用方（尽管当前数据全丢失，调用方无法正常工作）→ 修复后行为从"静默丢失"变为"正常流转"，不破坏已有契约
- **[到期实时计算的边界]** `endTime` 精确到秒，在到期时刻的并发请求可能有短暂不一致 → 可接受，最大偏差在秒级
