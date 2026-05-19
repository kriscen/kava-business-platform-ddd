## Why

UPMS 模块作为项目的 DDD 参考实现，当前存在多项偏离 DDD 最佳实践的问题：DomainService 大量透传（71% 为空壳）、Spring 字段注入泄漏到 Domain 层、跨聚合写操作绕过应用层编排、Adapter 直接引用 domain 内部类型（entity）。这些问题若不修正，将随着模块扩展而恶化，也会给 Member 等后续模块树立错误范式。

## What Changes

- **重构 DomainService 注入方式**：所有 Domain 层类从 `@Resource` 字段注入改为构造器注入，domain 模块 pom 从 `spring-boot-starter` 改为 `spring-context`
- **清理透传 DomainService**：移除 10 个未被 AppService 调用的 DomainService 实现中的 Repository 注入，AppService 移除未使用的 DomainService 注入
- **修正跨聚合写操作**：将 `SysTenantService` 中的跨聚合写（创建租户时初始化 Role）提升到 `SysTenantAppService` 编排
- **清理 Adapter 违规引用**：移除 `SysFileAdapterConverter` 和 `SysI18nAdapterConverter` 中对 domain.entity 的未使用 import
- **补充 ListQuery 过滤字段**：为 `SysUserListQuery`、`SysRoleListQuery` 等添加实际过滤条件字段（当前只有分页参数，查询条件被丢弃）

## Capabilities

### New Capabilities

- `ddd-compliance`: DDD 规范合规改造，覆盖注入方式、DomainService 治理、跨聚合访问、Adapter 引用边界四个维度
- `list-query-filter`: 为 ListQuery 值对象补充实际过滤字段，修复查询条件被丢弃的功能缺陷

### Modified Capabilities

（无现有 spec 需要修改）

## Impact

### kbpd-upms-domain
- 14 个 DomainService 实现类（注入方式改造）
- 10 个透传 DomainService 实现（移除 Repository 注入）
- `pom.xml`（依赖从 spring-boot-starter 改为 spring-context）
- `SysTenantService`（移除跨聚合写逻辑）
- `SysRoleService`（新增 `initTenantAdminRole` 方法，接收跨聚合写逻辑）
- 所有 `*ListQuery` 值对象（补充过滤字段）

### kbpd-upms-application
- 14 个 AppService 实现（移除未使用的 DomainService 注入、构造器注入改造）
- `SysTenantAppService`（新增跨聚合写编排逻辑）
- AppConverter（适配 ListQuery 新字段）

### kbpd-upms-adapter
- `SysFileAdapterConverter`（移除 domain.entity import）
- `SysI18nAdapterConverter`（移除 domain.entity import）
- AdapterConverter（适配 ListQuery 新字段）

### kbpd-upms-infrastructure
- Repository 实现（适配 ListQuery 新字段的查询条件）
