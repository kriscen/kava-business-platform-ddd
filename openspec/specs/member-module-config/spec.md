# Member 模块配置规范

## Purpose

定义 Member 模块（kbpd-member）各子模块的 DDD 依赖规范、安全配置要求和代码卫生约束，确保模块结构符合项目 DDD 分层架构。

## Requirements

### Requirement: Member 模块各层依赖符合 DDD 规范

Member 模块的每个子模块 SHALL 仅依赖其 DDD 分层允许的模块，依赖方向 MUST 遵循 adapter/infrastructure → application → domain 的向内原则。

#### Scenario: types 层仅依赖轻量基础模块
- **WHEN** 检查 `kbpd-member-types` 的 pom.xml
- **THEN** 其内部依赖 MUST 仅为 `kbpd-common-core`，MUST NOT 包含 `spring-boot-starter-web` 等重型框架

#### Scenario: domain 层支持构造器注入
- **WHEN** 检查 `kbpd-member-domain` 的 pom.xml
- **THEN** 其依赖 MUST 包含 `spring-context`（用于 `@Service` + 构造器注入），MUST NOT 包含 `spring-boot-starter`

#### Scenario: application 层具备对象映射和事务能力
- **WHEN** 检查 `kbpd-member-application` 的 pom.xml
- **THEN** 其依赖 MUST 包含 `mapstruct`、`spring-tx`、`slf4j-api`

#### Scenario: adapter 层使用项目统一 Web 模块
- **WHEN** 检查 `kbpd-member-adapter` 的 pom.xml
- **THEN** 其依赖 MUST 使用 `kbpd-common-web` 而非直接使用 `spring-boot-starter-web`
- **AND** 其依赖 MUST 包含 `nacos-discovery` 和 `nacos-config`

#### Scenario: api 层支持 JSON 序列化注解
- **WHEN** 检查 `kbpd-member-api` 的 pom.xml
- **THEN** 其依赖 MUST 包含 `jackson-annotations`

### Requirement: Member 服务必须启用 JWT 资源服务器

Member 服务 SHALL 启用 Spring Security 资源服务器，对所有非公开端点进行 JWT 认证。

#### Scenario: Application 类启用资源服务器
- **WHEN** 检查 `MemberApplication.java`
- **THEN** 类上 MUST 有 `@EnableResourceServer` 注解

#### Scenario: dev 环境放行请求
- **WHEN** 使用 `dev` profile 启动
- **THEN** `MemberDevSecurityConfig` SHALL 放行所有请求并禁用 CSRF（开发调试用）

### Requirement: Member 模块无 archetype 残留代码

Member 模块 MUST NOT 包含 Maven archetype 生成的占位代码。

#### Scenario: application 层无 App.java 残留
- **WHEN** 检查 `kbpd-member-application` 源码
- **THEN** MUST NOT 存在 `com.kava.kbpd.App.java` 文件

### Requirement: Member 模块具备基础冒烟测试

Member bootstrap 模块 SHALL 至少包含一个 Spring 上下文加载测试。

#### Scenario: 存在 ApplicationTest 类
- **WHEN** 检查 `kbpd-member-bootstrap/src/test/`
- **THEN** MUST 存在 `MemberApplicationTest.java`，验证 Spring 上下文可正常加载
