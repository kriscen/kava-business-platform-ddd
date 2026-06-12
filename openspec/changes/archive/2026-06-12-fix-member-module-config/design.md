## Context

Member 模块首次搭建时以 UPMS 为参考，但各子模块的 pom.xml 依赖配置存在多处偏差。当前构建可通过（BUILD SUCCESS），但存在 MapStruct 警告和运行时安全隐患。本次修复的目标是将 Member 模块的依赖配置对齐到 UPMS 的成熟模式。

## Goals / Non-Goals

**Goals:**
- 修正所有子模块 pom.xml 依赖，使其符合 DDD 分层规范
- 激活 Spring Security 资源服务器（JWT 认证）
- 清理 archetype 残留代码
- 补充基础冒烟测试

**Non-Goals:**
- 不涉及业务逻辑开发（Application Service、Domain Service 等）
- 不修改已有的 domain 模型或 infrastructure 实现
- 不调整 bootstrap 的运行时配置（端口、数据源等）
- 不解决 MapStruct unmapped 警告（属于后续业务开发范畴）

## Decisions

### 1. types 层依赖修正

**决策**：将 `kbpd-member-types` 的 `spring-boot-starter-web` 替换为 `kbpd-common-core`

**Because**：types 层是 DDD 中最轻量的模块，仅包含枚举、常量、异常类型。`spring-boot-starter-web` 引入了整个 Web 栈，违反了 types 层的轻量原则。`kbpd-common-core` 提供了基础类型（如值对象 ID、聚合根接口），是 types 层的正确依赖。

### 2. adapter 层使用 kbpd-common-web 替代 spring-boot-starter-web

**决策**：将 `kbpd-member-adapter` 的 `spring-boot-starter-web` 替换为 `kbpd-common-web`

**Because**：`kbpd-common-web` 封装了项目统一的 Web 基础设施（响应包装、异常处理、全局拦截器等），直接使用 `spring-boot-starter-web` 会缺失这些共享能力。UPMS adapter 已验证此模式。

### 3. application 层补充 mapstruct + spring-tx

**决策**：在 `kbpd-member-application` 添加 `mapstruct`、`mapstruct-processor`、`lombok-mapstruct-binding`、`spring-tx`、`slf4j-api`

**Because**：Application Service 需要 DTO ↔ Domain 对象转换（mapstruct）和事务控制（spring-tx @Transactional）。这些是 UPMS application 层已验证的依赖组合。不预先添加会导致后续开发时编译失败。

### 4. 安全注解放在 Application 类上

**决策**：在 `MemberApplication.java` 添加 `@EnableResourceServer`，保持与 UPMS 相同的模式

**Because**：`@EnableResourceServer` 是 `kbpd-common-security` 提供的组合注解，激活 JWT 资源服务器配置。放在 Application 类上是项目约定（UPMS 已验证），而非放在独立的 SecurityConfig 中。`MemberSecurityConfig` 保留为空壳，后续按需扩展。

### 5. parent pom 补充 build 配置

**决策**：在 `kbpd-member/pom.xml` 添加与 UPMS 一致的 `<build>` 段

**Because**：统一编译插件版本（maven-compiler-plugin、maven-resources-plugin）确保所有子模块使用相同的编译设置，避免各子模块自行声明版本导致不一致。

## Risks / Trade-offs

- **[低风险] kbpd-common-web 传递依赖冲突** → 替换 spring-boot-starter-web 为 kbpd-common-web 后，需确认 web starter 通过 common-web 正确传递。构建验证即可确认。
- **[低风险] @EnableResourceServer 可能与 DevSecurityConfig 冲突** → DevSecurityConfig 使用 `@Profile("dev")` 放行所有请求，与资源服务器的 JWT 校验可能冲突。但 UPMS 采用相同模式已正常工作，说明 Spring Security 的 profile 优先级机制可以正确处理。
