## Context

UPMS 模块是 Kava 平台的第一个完整 DDD 实现，也是后续模块（Member 等）的参考范式。项目已建立 DDD 实现规范（docs/03-conventions/ddd-rules.md），明确了五项决策：充血方向、构造器注入、按需 DomainService、跨聚合读写分离、Adapter 引用边界。当前代码与规范存在系统性偏差，需要一次性修正。

现有代码规模：
- Domain 层：14 个 DomainService（10 透传 + 4 有逻辑）、14 个 Entity/AggregateRoot、14 组 ID/ListQuery 值对象、16 个 Repository 接口
- Application 层：14 个 AppService
- Adapter 层：14 个 Controller、16 个 Converter、2 个 RPC Service
- Infrastructure 层：16 个 Repository 实现、14 个 Infra Converter

## Goals / Non-Goals

**Goals:**
- 将 Domain 层注入方式统一为构造器注入，pom 依赖改为 spring-context
- 清理 10 个透传 DomainService 中的 Repository 注入，AppService 移除未使用的 DomainService 注入
- 将 SysTenantService 的跨聚合写逻辑提升到 SysTenantAppService
- 移除 Adapter 中对 domain.entity 的违规引用
- 为 ListQuery 补充实际过滤字段，修复查询条件丢弃的功能缺陷

**Non-Goals:**
- 不做充血模型重构（Entity 行为封装留待后续各子域按需演进）
- 不做 ID 类型位置调整（ID 按需提升到 common-core，当前不移动）
- 不做 ListQuery 位置调整（留在 domain 层，依赖方向决定的）
- 不做 RemoteUserService 的 4 次 RPC 调用合并（性能优化独立处理）
- 不做空的 AppListDTO 补充（属于前端接口完善，不在本次范围）

## Decisions

### D1: 构造器注入 — 渐进式改造

**决策**：所有 Domain 和 Application 层类统一改为构造器注入

**Because** 字段注入（@Resource/@Autowired）隐藏依赖关系，不利于测试，且违反 DDD 规范中 Domain 层应保持行为纯净的原则。Spring 4.3+ 支持单一构造器自动注入，无需额外配置。

**方案**：
- Domain 类移除 `@Resource`/`@Autowired`，改为构造器参数
- Application 类同步改造
- 不需要新建 @Configuration 类手动装配（Spring 自动发现 @Service + 单一构造器）

### D2: 透传 DomainService — 移除 Repo 注入，保留接口

**决策**：10 个透传 DomainService 的实现类移除 Repository 注入，方法体改为抛出 `UnsupportedOperationException`。对应的 AppService 移除未使用的 DomainService 注入。

**Because** 保留接口作为扩展点，未来有业务规则时只需补充实现。移除 Repo 注入避免无意义的依赖链。AppService 已经直接调 Repository，不存在功能影响。

**受影响的 10 个 Service**：
- SysAuditLogService, SysDeptService, SysFileService, SysFileGroupService
- SysI18nService, SysLogService, SysMenuService, SysOauthClientService
- SysPublicParamService, SysRouteConfService

### D3: 跨聚合写 — 提升到 AppService 编排

**决策**：`SysTenantServiceImpl.initTenantAdminRole()` 的逻辑拆分：Role 初始化逻辑移到 `SysRoleServiceImpl.initTenantAdminRole(tenantId, menuIdStr)`，编排逻辑移到 `SysTenantAppService.createTenant()`。

**Because** DomainService 禁止跨聚合写。角色初始化是 Role 聚合的关注点，编排（先建租户再建角色）是应用层的职责。同一 @Transactional 保证强一致。

### D4: ListQuery 过滤字段 — 按实际查询需求补充

**决策**：为每个 ListQuery 值对象补充实际过滤字段，字段来源是 AdapterConverter 中已有的 AdapterListQuery 字段。

**Because** 当前 ListQuery 只有 queryParam（分页），Adapter 传入的过滤条件在转换时被丢弃，是功能缺陷。补充字段后，Repository 实现需要相应修改查询逻辑。

**优先处理的 ListQuery**（有实际过滤需求的）：
- SysUserListQuery：username、phone、lockFlag、deptId
- SysRoleListQuery：roleName、roleCode
- SysDeptListQuery：deptName
- SysMenuListQuery：menuName、type、scope
- SysTenantListQuery：tenantName

其余 ListQuery（AuditLog、File、Log 等后台管理类）暂保持只有分页参数，等有实际查询需求时再补充。

### D5: Adapter 违规清理

**决策**：移除 `SysFileAdapterConverter` 和 `SysI18nAdapterConverter` 中对 domain.entity 的未使用 import。

**Because** Adapter 禁止引用 domain.model.entity，这两个 import 是死代码。

## Risks / Trade-offs

| 风险 | 影响 | 缓解 |
|------|------|------|
| 10 个透传 DomainService 方法体抛 UnsupportedOperationException | 如果有代码路径调用了这些方法会运行时报错 | 已确认 AppService 不调用它们，编译通过即安全 |
| ListQuery 补充字段后 Infrastructure 的 Repository 实现需要同步修改 | 改动面较大，涉及 MyBatis 查询条件 | 优先处理有实际过滤需求的 5 个 ListQuery，其余暂不处理 |
| 跨聚合写逻辑搬迁可能遗漏事务边界 | 租户创建后角色未初始化 | AppService 的 @Transactional 已覆盖，原有逻辑在同一事务中 |
| 构造器注入改造涉及 28+ 个类的改动 | 改动量大，可能有遗漏 | 按层批量处理，每层编译验证 |
