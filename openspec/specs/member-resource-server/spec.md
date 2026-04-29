# Capability: member-resource-server

## Purpose

定义 Member 服务作为 OAuth2 Resource Server 的配置要求，包括 JWT token 验证、JWK Set URI 配置以及开发环境安全策略。

## Requirements

### Requirement: Member 服务启用 ResourceServer 认证

Member 服务的 `MemberApplication` MUST 添加 `@EnableResourceServer` 注解，激活 JWT token 验证能力。

#### Scenario: Member 服务验证 JWT token
- **WHEN** Member 服务收到携带有效 JWT token 的请求
- **THEN** Spring Security MUST 成功验证 token 并构造 SecurityContext
- **AND** `SecurityUtils.getUserContext()` 返回有效的 UserContext

#### Scenario: Member 服务拒绝无效 token
- **WHEN** Member 服务收到携带无效或过期 JWT token 的请求
- **THEN** MUST 返回 HTTP 401 Unauthorized

### Requirement: Member 服务配置 JWK Set URI

Member 服务的 `application-dev.yml` MUST 配置 `spring.security.oauth2.resource-server.jwt.jwk-set-uri` 指向 Auth 服务的 JWKS endpoint（`http://localhost:8600/auth/oauth2/jwks`）。

#### Scenario: 配置正确的 JWKS endpoint
- **WHEN** Member 服务启动
- **THEN** NimbusJwtDecoder MUST 从 `http://localhost:8600/auth/oauth2/jwks` 获取公钥用于 JWT 验签

### Requirement: Member 服务提供 Dev 环境安全配置

Member 服务 MUST 提供 `DevSecurityConfig`（`@Profile("dev")`），在开发环境下允许所有请求不经过认证。

#### Scenario: dev 环境跳过认证
- **WHEN** Member 服务以 dev profile 运行
- **THEN** 所有请求 MUST 不经过 JWT 验证，直接放行

#### Scenario: 非 dev 环境启用认证
- **WHEN** Member 服务以非 dev profile 运行
- **THEN** ResourceServerConfiguration MUST 生效，要求 JWT 认证
