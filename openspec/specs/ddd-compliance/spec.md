### Requirement: Domain 层统一使用构造器注入
Domain 层所有类（DomainService 实现、Repository 实现等）MUST 使用构造器注入，MUST NOT 使用 @Resource 或 @Autowired 字段注入。Domain 层允许依赖轻量级 `spring-context`（仅注解），MUST NOT 依赖 `spring-boot-starter`、MyBatis、数据库驱动等重型框架。

#### Scenario: DomainService 使用构造器注入
- **WHEN** DomainService 实现类需要注入 Repository 依赖
- **THEN** 通过构造器参数注入，不使用 @Resource 或 @Autowired 字段注解

#### Scenario: Domain 模块 pom 依赖
- **WHEN** kbpd-upms-domain 模块声明 Spring 依赖
- **THEN** 依赖 spring-context 而非 spring-boot-starter

#### Scenario: CLAUDE.md 架构描述与 ddd-rules.md 一致
- **WHEN** CLAUDE.md 描述 domain 层约束
- **THEN** MUST 使用"domain 不依赖 Spring Boot / MyBatis 等重型框架，允许轻量 spring-context 注解"的措辞
- **AND** MUST NOT 使用"domain 不依赖任何外部框架"的绝对化表述

### Requirement: 未使用的 DomainService 实现不注入 Repository
未被 AppService 调用的 DomainService 实现 MUST NOT 注入 Repository，方法体 MUST 抛出 UnsupportedOperationException。

#### Scenario: 透传 DomainService 移除 Repository 注入
- **WHEN** 一个 DomainService 的所有方法均为纯 Repository 透传（零逻辑）
- **AND** AppService 未调用该 DomainService 的任何方法
- **THEN** 该 DomainService 实现类移除所有 Repository 注入
- **AND** 所有方法体改为 `throw new UnsupportedOperationException("暂未实现")`

#### Scenario: AppService 移除未使用的 DomainService 注入
- **WHEN** AppService 注入了 DomainService 但从未调用其任何方法
- **THEN** 该 DomainService 注入 MUST 被移除

### Requirement: DomainService 禁止跨聚合写
DomainService MUST NOT 直接写入其他聚合的 Repository。跨聚合写操作 MUST 由 AppService 在 @Transactional 内编排。

#### Scenario: 租户创建时初始化管理员角色
- **WHEN** 创建新租户
- **THEN** SysTenantService 只负责创建租户实体
- **AND** SysRoleService 提供 initTenantAdminRole 方法负责角色初始化
- **AND** SysTenantAppService.createTenant 在 @Transactional 内依次调用两者

### Requirement: Adapter 禁止引用 Domain 内部类型
Adapter 层 MUST NOT import domain.model.entity、domain.model.aggregate、domain.service、domain.repository 包中的任何类型。

#### Scenario: AdapterConverter 清理违规 import
- **WHEN** AdapterConverter 文件中存在对 domain.model.entity 的 import
- **THEN** 该 import MUST 被移除

#### Scenario: Adapter 允许引用 domain.model.valobj
- **WHEN** Adapter 需要构造 ID 值对象或 ListQuery
- **THEN** 可以 import domain.model.valobj 包中的类型
