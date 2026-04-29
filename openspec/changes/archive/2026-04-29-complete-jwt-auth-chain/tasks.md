## 1. JWT Claims 基础常量

- [x] 1.1 在 `JwtClaimConstants` 中新增 `TENANT_ID`、`USER_TYPE`、`ROLES` 常量
  - 文件: `kbpd-common/kbpd-common-core/src/main/java/com/kava/kbpd/common/core/constants/JwtClaimConstants.java`

## 2. UserContext 统一上下文模型

- [x] 2.1 创建 `UserContext` 类，包含 tenantId、userType、userId、memberId、username、deptId、roles 字段，提供静态工厂方法 `fromJwtClaims(Map<String, Object> claims)`
  - 文件: `kbpd-common/kbpd-common-core/src/main/java/com/kava/kbpd/common/core/model/UserContext.java`

- [x] 2.2 创建 `UserContextHolder`，基于 ThreadLocal 持有 UserContext，提供 get/set/clear 静态方法
  - 文件: `kbpd-common/kbpd-common-security/src/main/java/com/kava/kbpd/common/security/context/UserContextHolder.java`

- [x] 2.3 增强 `ResourceServerConfiguration` 的 `jwtAuthenticationConverter`，在 JWT 验证成功后构造 UserContext 并设置到 UserContextHolder
  - 文件: `kbpd-common/kbpd-common-security/src/main/java/com/kava/kbpd/common/security/config/ResourceServerConfiguration.java`

- [x] 2.4 增强 `SecurityUtils`，新增 `getUserContext()`、`getTenantId()`、`getUserType()`、`getRoles()` 方法
  - 文件: `kbpd-common/kbpd-common-security/src/main/java/com/kava/kbpd/common/security/utils/SecurityUtils.java`

## 3. Auth 端 JWT Claims 增强

- [x] 3.1 重写 `AuthorizationServiceConfig.jwtTokenCustomizer()`，从 `ExtendAuthenticationToken` 读取 tenantId 和 userType 写入 claims，将 authorities 替换为 roles（仅角色代码）
  - 文件: `kbpd-auth/src/main/java/com/kava/kbpd/auth/config/AuthorizationServiceConfig.java`
  - 注意: 需要检查 `context.getPrincipal() instanceof ExtendAuthenticationToken` 来获取 tenantId/userType

## 4. ~~权限缓存服务~~ → DEFERRED

> 权限缓存属于 UPMS 权限领域的职责，移至后续 change 与 UPMS domain 一起设计。
> 已清理：`PermissionCacheService` 已删除，`PwdUserDetailsService` 中的缓存预热调用已移除。

- [x] ~~4.1 创建 `PermissionCacheService`~~ → 已删除
- [x] ~~4.2 在 `PwdUserDetailsService` 中预热权限缓存~~ → 已移除

## 5. Dubbo 上下文传播

- [x] 5.1 创建 `ConsumerUserContextFilter`（实现 Dubbo `Filter`），从 UserContextHolder 提取 UserContext 写入 RpcContext attachment
  - 文件: `kbpd-common/kbpd-common-security/src/main/java/com/kava/kbpd/common/security/dubbo/ConsumerUserContextFilter.java`

- [x] 5.2 创建 `ProviderUserContextFilter`（实现 Dubbo `Filter`），从 RpcContext attachment 读取用户上下文构造 UserContext 设置到 UserContextHolder，调用完成后清理
  - 文件: `kbpd-common/kbpd-common-security/src/main/java/com/kava/kbpd/common/security/dubbo/ProviderUserContextFilter.java`

- [x] 5.3 注册 Dubbo SPI：创建 `META-INF/dubbo/org.apache.dubbo.rpc.Filter` 文件，配置两个 Filter
  - 文件: `kbpd-common/kbpd-common-security/src/main/resources/META-INF/dubbo/org.apache.dubbo.rpc.Filter`

## 6. Member 服务接入 ResourceServer

- [x] 6.1 在 `MemberApplication` 添加 ResourceServer 认证支持（引入 kbpd-common-security 依赖）
  - 文件: `kbpd-member/kbpd-member-bootstrap/pom.xml`, `kbpd-member/kbpd-member-bootstrap/src/main/java/com/kava/kbpd/member/MemberApplication.java`

- [x] 6.2 创建 `MemberDevSecurityConfig`（`@Profile("dev")`），允许所有请求不经过认证
  - 文件: `kbpd-member/kbpd-member-bootstrap/src/main/java/com/kava/kbpd/member/config/MemberDevSecurityConfig.java`

- [x] 6.3 在 `application-dev.yml` 中添加 `spring.security.oauth2.resource-server.jwt.jwk-set-uri: http://localhost:8600/auth/oauth2/jwks`
  - 文件: `kbpd-member/kbpd-member-bootstrap/src/main/resources/application-dev.yml`

## 7. 清理废弃代码

- [x] 7.1 清理 `SecurityConstants` 中与 `JwtClaimConstants` 重复的 claim key 常量，统一使用 `JwtClaimConstants`
  - 文件: `kbpd-common/kbpd-common-security/src/main/java/com/kava/kbpd/common/security/constants/SecurityConstants.java`

- [x] 7.2 更新 `ResourceServerConfiguration` 中的 `jwtAuthenticationConverter`，将 `AUTHORITIES` 改为 `ROLES`
  - 文件: `kbpd-common/kbpd-common-security/src/main/java/com/kava/kbpd/common/security/config/ResourceServerConfiguration.java`

## 8. 验证

- [x] 8.1 编译全项目，确保无编译错误：`mvn clean install -DskipTests`

- [ ] 8.2 验证 JWT 结构：启动 Auth 服务，通过 OAuth2 token endpoint 获取 JWT，检查 payload 包含 tenantId、userType、roles 字段

- [ ] 8.3 验证下游 UserContext：启动 UPMS 服务，携带 JWT 访问受保护接口，确认 SecurityUtils.getUserContext() 返回完整上下文

- [ ] 8.4 验证 Dubbo 传播：在 UPMS 的 RPC 调用链路中添加临时日志，确认 Provider 端能获取 UserContext

- [ ] 8.5 验证 Member 服务：确认 Member 以 dev profile 启动时跳过认证，确认 jwk-set-uri 配置正确
