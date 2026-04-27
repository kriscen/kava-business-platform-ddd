# kbpd-auth 业务规则

> 注：kbpd-auth 是功能模块（非 DDD 业务模块），本文档记录其核心认证流程中的规则与约束，而非领域业务规则。

---

## 1. 多租户认证路由

### 规则

认证时必须携带 `tenantId` 和 `userType`，系统根据 `userType` 决定路由到不同的用户存储：

| userType | 路由目标 | Dubbo 接口 | UserDetails 类型 |
|----------|----------|-----------|-----------------|
| `TO_B` | kbpd-upms | `IRemoteUserService` | `SysUserDetails` |
| `TO_C` | kbpd-member | `IRemoteMemberService` | `MemberDetails` |
| 其他 | — | — | 抛出 `UsernameNotFoundException` |

### 传递链路

```
客户端 → /oauth2/authorize?client_id=xxx
  → CustomLoginUrlAuthenticationEntryPoint（从 query 提取 client_id，附加到登录页 URL）
  → OauthController（通过 client_id 查询 DB 获取 tenantId、userType，注入表单隐藏域）
  → TenantAwareAuthenticationFilter（从请求参数提取 tenantId、userType、clientId）
  → ExtendAuthenticationToken（携带 tenantId、userType 的认证令牌）
  → CustomerAuthenticationProvider → PwdUserDetailsService（按 userType 路由）
```

### 约束

- `client_id` 必须在系统中注册，否则登录页展示错误
- `tenantId` 和 `userType` 从 OAuth2 Client 配置中获取，与 `client_id` 绑定
- 当前未校验 `userType`、`tenantId`、`clientId` 的匹配关系（TODO）

---

## 2. OAuth2 Client 管理

### 规则

- Client 信息存储在 UPMS 数据库中，通过 `IRemoteOauthClientService` 远程加载
- Client 密码使用 `{noop}` 前缀存储（明文），未启用加密
- 支持的客户端认证方式：`CLIENT_SECRET_BASIC` + `CLIENT_SECRET_POST`

### Client 配置映射

从 `SysOauthClientDTO` 映射到 Spring Authorization Server 的 `RegisteredClient`：

| 字段 | 映射逻辑 |
|------|---------|
| `authorizedGrantTypes` | 逗号分隔 → `AuthorizationGrantType` 集合 |
| `webServerRedirectUri` | 逗号分隔 → 重定向 URI 集合 |
| `scope` | 逗号分隔 → Scope 集合 |
| `autoapprove` | 决定是否需要用户确认授权（`requireAuthorizationConsent`） |
| `userType` / `tenantId` | 存储为 Client Settings 自定义属性 |
| `accessTokenValiditySeconds` | 覆盖全局默认 Access Token TTL |
| `refreshTokenValiditySeconds` | 覆盖全局默认 Refresh Token TTL |

### Token 格式

Access Token 格式固定为 `SELF_CONTAINED`（JWT），不支持 `REFERENCE`（Opaque Token）。

---

## 3. Token 签发与存储

### Token 生成

使用 `DelegatingOAuth2TokenGenerator`，依次尝试：

1. `JwtGenerator` — Access Token 签发为 JWT
2. `OAuth2AccessTokenGenerator` — Opaque Token 兜底
3. `OAuth2RefreshTokenGenerator` — Refresh Token 生成

### JWT 签名

- 算法：RSA 2048
- 密钥持久化：存储在 Redis key `auth:jwk`，服务重启后复用同一密钥对
- 若 Redis 中无密钥，启动时自动生成新密钥对

### Token 响应策略

根据 Client ID 区分响应格式：

| Client 类型 | 判断条件 | 响应格式 |
|------------|---------|---------|
| 工具类客户端 | Client ID 在 `kbpd.auth.tool-client-id` 列表中 | 标准 OAuth2 Token Response（裸 JSON） |
| 普通客户端 | 其他 | 包裹在 `JsonResult.buildSuccess()` 中 |

**工具类客户端用途**：Knife4j / Swagger UI 等 API 测试工具需要直接获取 `access_token` 字段，无法解析 `JsonResult` 包裹。

---

## 4. 授权确认规则

### 规则

- 当 Client 配置 `autoapprove = true` 时，跳过授权确认页，直接签发 Code
- 当 Client 配置 `autoapprove = false` 时：
  - 用户首次授权 → 展示待批准的 Scope 列表，需手动确认
  - 用户已授权的 Scope → 下次自动跳过，仅展示新增 Scope
- 授权确认记录存储在 Redis，TTL 默认 10 分钟（`authorizationConsentTimeout`）
- 过期后需重新确认

---

## 5. 安全规则

### 密码认证

- 密码比较使用 `DelegatingPasswordEncoder`，支持 `{bcrypt}`、`{noop}` 等前缀
- 认证包含时间常量比较（防时序攻击）
- 支持密码泄露检测（`CompromisedPasswordChecker`，框架内置）

### CSRF

- CSRF 防护已禁用（`csrf.disable()`），因服务主要提供 API 端点

### 白名单

- 静态资源路径完全绕过 Security 过滤链
- `GET /oauth2/login` 和 `GET /oauth2/error` 也绕过过滤链
- 可通过 `kbpd.auth.whitelist-paths` 配置额外白名单

---

## 6. Redis 存储规则

### Key 命名规范

| 存储对象 | Key 模式 | TTL |
|----------|---------|-----|
| Authorization（state 阶段） | `auth:token:{type}:{id}` | `authorizationTimeout`（默认 10 分钟） |
| Authorization Code | `auth:token:authorization_code:{code}` | code 有效期 |
| Access Token | `auth:token:access_token:{value}` | `accessTokenValiditySeconds`（默认 12 小时） |
| Refresh Token | `auth:token:refresh_token:{value}` | `refreshTokenValiditySeconds`（默认 30 天） |
| Consent | `auth:consent:{principalName}:{clientId}` | `authorizationConsentTimeout`（默认 10 分钟） |
| JWK 密钥对 | `auth:jwk` | 永不过期 |

### 序列化

所有 OAuth2 对象（`OAuth2Authorization`、`OAuth2AuthorizationConsent`、`ExtendAuthenticationToken`、`SysUserDetails`、`MemberDetails`）通过自定义 Jackson Module + Mixin 实现序列化，以支持存入 Redis。

### 不支持的操作

- `RedisAuthorizationService.findById()` 抛出 `UnsupportedOperationException`（仅支持按 Token 查询）
- `DBRegisteredClientRepository.save()` / `findById()` 不支持（客户端数据由 UPMS 管理）
