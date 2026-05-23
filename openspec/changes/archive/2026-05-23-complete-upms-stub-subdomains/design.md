## Context

UPMS 模块 14 个子域中，Menu、Dept、OauthClient 三个子域的 DomainService 处于空壳状态（所有方法抛 `UnsupportedOperationException`）。这三个子域的基础设施层（Repository 实现、Mapper、PO、Converter）均已完整就绪，唯一缺口是 Domain Service 层未接入 Repository。

当前项目中已有一致的模式可参考：Tenant、Log、AuditLog 等 Tier 3 子域均采用"注入 Repository + 委托转发"的委托型 DomainService 模式。

## Goals / Non-Goals

**Goals:**
- 三个空壳 DomainService 补齐为委托型实现，与 Tier 3 子域保持一致
- DomainService 接口暴露 Repository 已有的额外查询方法（Menu 的 `queryAll`/`queryByIds`，OauthClient 的 `queryByClientId`）
- 保持构造器注入 + `@RequiredArgsConstructor` 风格

**Non-Goals:**
- 不向实体中添加业务行为方法（充血模型演进属于后续业务驱动）
- 不改变 Repository CQRS 选型（三个子域均使用 SimpleRepository，当前合理）
- 不调整 Entity/AggregateRoot 分类（按 DDD 规范按需演进）
- 不修改 AppService 层或 Adapter 层（本次仅补齐 Domain 层）

## Decisions

### D1: 采用委托型 DomainService 模式

遵循 `ISysPublicParamService` / `SysTenantService` 的现有模式：注入 `IBaseSimpleRepository`，所有方法直接委托转发。

Because: 项目 DDD 规范 3.2 明确要求"所有子域保留 DomainService 接口，AppService 必须通过 DomainService 调用 Repository"。委托模式保证分层合规，同时为未来业务规则增长预留入口。

### D2: DomainService 接口暴露 Repository 额外查询方法

- `ISysMenuService` 新增 `queryAll()` 和 `queryByIds(List<SysMenuId>)`
- `ISysOauthClientService` 新增 `queryByClientId(String)`

Because: 这些方法已在 Repository 接口中定义且 Infrastructure 层已实现。当前 `SysRoleService` 直接注入 `ISysMenuRepository` 做跨聚合读——虽然规范允许 DomainService 跨聚合读，但通过 DomainService 接口暴露更符合分层规范，也为 AppService 层提供合规调用路径。

### D3: 使用 `@RequiredArgsConstructor` 而非手写构造器

Because: 与项目中 SysTenantService 等已有一致的风格保持统一。`@RequiredArgsConstructor` 是 Lombok 提供的编译期构造器生成，不违反 DDD 规范中"构造器注入"的要求。

## Risks / Trade-offs

- **委托型 Service 缺乏业务逻辑** → 可接受。按 DDD 规范"暂无复杂规则的实体保持数据壳状态，等业务规则出现时再补充"
- **新增接口方法可能影响现有调用方** → 低风险。新增方法不影响现有方法签名，AppService 和 Adapter 层按需调用即可
