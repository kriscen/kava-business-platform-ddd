## MODIFIED Requirements

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
