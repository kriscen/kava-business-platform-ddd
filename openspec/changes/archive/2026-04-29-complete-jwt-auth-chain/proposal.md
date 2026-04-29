## Why

当前 JWT token 中缺少 tenantId 和 userType 等关键多租户上下文信息，导致下游服务无法从 token 中获取租户和用户类型；同时 authorities 字段混合了角色和权限，既导致 JWT 膨胀，又使权限变更无法即时生效。此外，Dubbo RPC 调用时用户上下文丢失，Member 服务尚未接入资源服务器认证。本次 change 旨在完善从 JWT 生成到校验再到上下文传播的完整认证链路。

## What Changes

- **JWT claims 补全**: 在 `jwtTokenCustomizer` 中添加 tenantId、userType，将 authorities 替换为 roles（仅角色，不含细粒度权限）
- **新增 JwtClaimConstants 常量**: TENANT_ID、USER_TYPE、ROLES
- **统一用户上下文**: 新增 `UserContext` 对象封装 JWT claims，下游通过 `SecurityUtils.getUserContext()` 获取
- **Dubbo 上下文自动传播**: 通过 Dubbo SPI 实现 Consumer/Provider Filter，自动将 UserContext 写入/读取 RpcContext
- **Member 服务接入认证**: 添加 `@EnableResourceServer`、配置 `jwk-set-uri`、添加 DevSecurityConfig
- **C端 JWT 预留 roles**: Member 用户 JWT 中 roles 字段为空数组，Kava 不定义 C端角色，由 App 按需扩展
- **Gateway 保持纯路由**: 不做 JWT 验签，认证由下游服务自行处理

## Capabilities

### New Capabilities
- `jwt-claims-enhancement`: JWT token 结构增强 — 补全 tenantId/userType/roles claims，替换 authorities
- `unified-user-context`: 统一用户上下文对象 — UserContext 封装 + SecurityUtils 增强 + Dubbo 自动传播
- `member-resource-server`: Member 服务接入资源服务器认证

### Modified Capabilities
- `real-user-authentication`: jwtTokenCustomizer 行为变更 — claims 结构从 authorities 变为 roles，新增 tenantId/userType
- `tenant-aware-auth-security`: 无 spec 级别变更（认证流程不变，只影响 token 生成）

## Impact

### 受影响的关键文件
- `kbpd-auth/.../config/AuthorizationServiceConfig.java` — jwtTokenCustomizer 重写
- `kbpd-common/kbpd-common-core/.../constants/JwtClaimConstants.java` — 新增常量
- `kbpd-common/kbpd-common-security/.../config/ResourceServerConfiguration.java` — JWT converter 增强
- `kbpd-common/kbpd-common-security/.../utils/SecurityUtils.java` — 新增 UserContext 相关方法
- `kbpd-upms/kbpd-upms-adapter/.../rpc/RemoteUserService.java` — 返回 DTO 需区分角色和权限
- `kbpd-member/kbpd-member-bootstrap/.../MemberApplication.java` — 添加 @EnableResourceServer
- `kbpd-member/kbpd-member-bootstrap/.../config/MemberSecurityConfig.java` — DevSecurityConfig
- `kbpd-member/kbpd-member-bootstrap/src/main/resources/application-dev.yml` — jwk-set-uri 配置

### 新增文件
- `kbpd-common/kbpd-common-core/.../model/UserContext.java` — 统一用户上下文
- `kbpd-common/kbpd-common-security/.../context/UserContextHolder.java` — ThreadLocal 持有者
- `kbpd-common/kbpd-common-security/.../dubbo/ConsumerUserContextFilter.java` — Dubbo 消费端传播
- `kbpd-common/kbpd-common-security/.../dubbo/ProviderUserContextFilter.java` — Dubbo 提供端恢复
