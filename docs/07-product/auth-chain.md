# 认证链路

本文档描述 Kava 平台的完整认证链路，包括 JWT Token 结构、验证流程和租户上下文传播机制。

## JWT Token 结构

### 标准 Claims

| Claim | 来源 | 说明 |
|-------|------|------|
| `sub` | Spring Authorization Server | 主体标识（username） |
| `iss` | Spring Authorization Server | 签发者（Auth 服务地址） |
| `aud` | Spring Authorization Server | 受众（client_id） |
| `exp` / `iat` / `nbf` | Spring Authorization Server | 过期/签发/生效时间 |
| `jti` | Spring Authorization Server | Token 唯一 ID |
| `sid` | Spring Authorization Server | Session ID |

### 自定义 Claims

| Claim | 常量 | B端用户 | C端用户 |
|-------|------|--------|--------|
| `tenantId` | `JwtClaimConstants.TENANT_ID` | 租户 ID | 租户 ID |
| `userType` | `JwtClaimConstants.USER_TYPE` | `"1"` (TO_B) | `"2"` (TO_C) |
| `userId` | `JwtClaimConstants.USER_ID` | 用户 ID | — |
| `username` | `JwtClaimConstants.USERNAME` | 用户名 | — |
| `groupId` | `JwtClaimConstants.GROUP_ID` | 分组 ID | — |
| `memberId` | `JwtClaimConstants.MEMBER_ID` | — | 会员 ID |
| `roles` | `JwtClaimConstants.ROLES` | 角色列表 | 空列表 |

### Token 签名

- **算法**：RSA-2048 (RS256)
- **密钥管理**：RSA 密钥对存储在 Redis（key: `AUTH:jwk`），Auth 服务重启后复用同一密钥

## 端到端认证流程

```
┌─────────────────────────────────────────────────────────────────────┐
│                     登录阶段 (Auth Code + PKCE)                       │
│                                                                     │
│  [前端 SPA]                                                          │
│     │ 1. 生成 code_verifier, code_challenge (SHA-256), state         │
│     │ 2. 重定向到 GET /auth/oauth2/authorize                         │
│     │    ?client_id=xxx&response_type=code                           │
│     │    &code_challenge=xxx&code_challenge_method=S256              │
│     │    &redirect_uri=xxx&scope=xxx&state=xxx                       │
│     ▼                                                                │
│  [Spring Authorization Server]                                       │
│     │ 验证 PKCE 参数 (code_challenge 必须存在)                        │
│     │ 未认证 → 重定向到 /oauth2/login                                 │
│     ▼                                                                │
│  [TenantAwareAuthenticationFilter]                                   │
│     │ 1. 通过 client_id 查询 RegisteredClient                        │
│     │ 2. 从 ClientSettings 提取 tenant_id + user_type                │
│     │ 3. 构建 ExtendAuthenticationToken(username, password,          │
│     │    tenantId, userType)                                         │
│     ▼                                                                │
│  [CustomerAuthenticationProvider]                                    │
│     │ 委托 PwdUserDetailsService:                                    │
│     │  userType=1 → Dubbo→UPMS → SysUserDetails + roles             │
│     │  userType=2 → Dubbo→Member → MemberDetails                    │
│     ▼                                                                │
│  [Spring Authorization Server]                                       │
│     │ 认证成功 → 302 重定向到 redirect_uri?code=xxx&state=xxx         │
│     ▼                                                                │
│  [前端 SPA (回调)]                                                    │
│     │ 1. 验证 state 匹配                                             │
│     │ 2. POST /auth/oauth2/token                                     │
│     │    grant_type=authorization_code&code=xxx                      │
│     │    &code_verifier=xxx&client_id=xxx&redirect_uri=xxx           │
│     ▼                                                                │
│  [OAuth2 Token Endpoint]                                            │
│     │ 1. 验证 code_verifier SHA-256 哈希 == 存储的 code_challenge    │
│     │ 2. jwtTokenCustomizer 注入自定义 Claims                         │
│     │ 3. JWT Generator 用 RSA 密钥签名                                │
│     ▼                                                                │
│  [前端 SPA] ← access_token (JWT) + refresh_token                     │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                        请求阶段                                      │
│                                                                     │
│  [客户端]                                                            │
│     │ GET /upms/api/...                                              │
│     │ Authorization: Bearer <jwt>                                    │
│     ▼                                                                │
│  [Gateway]  ←── 纯路由，Token 透传，不做验证                           │
│     │                                                                │
│     ▼                                                                │
│  [微服务 Resource Server]                                            │
│     │ 1. 从 Auth 服务获取 JWK Set (/auth/oauth2/jwks)                │
│     │ 2. 验证 JWT 签名和有效期                                        │
│     │ 3. 自定义 JwtAuthenticationConverter:                          │
│     │    JWT Claims → UserContext → UserContextHolder (ThreadLocal)  │
│     │ 4. 业务代码通过 SecurityUtils.getTenantId() 等获取上下文         │
│     ▼                                                                │
│  [UserContextCleanupFilter]                                          │
│     │ 请求结束后清理 ThreadLocal，防止线程池中的上下文泄漏               │
└─────────────────────────────────────────────────────────────────────┘
```

## 租户上下文传播

### HTTP 请求内传播

每个微服务的 `ResourceServerConfiguration` 配置了自定义 `JwtAuthenticationConverter`：

```
JWT Claims
  → UserContext.fromJwtClaims(claims)    // 构建 UserContext
  → UserContextHolder.set(userContext)   // 存入 ThreadLocal
  → 业务代码通过 SecurityUtils 访问
  → UserContextCleanupFilter 清理        // 请求结束时
```

### Dubbo RPC 跨服务传播

微服务间通过 Dubbo RPC 调用时，`UserContext` 通过 Dubbo Filter 自动传播：

```
[服务 A]                           [服务 B]
   │                                  │
   │ Dubbo 调用                       │
   ▼                                  │
ConsumerUserContextFilter              │
   │ 读取 UserContextHolder           │
   │ 写入 RpcContext.ClientAttachment  │
   │ (ctx_tenantId, ctx_userType,     │
   │  ctx_userId, ctx_memberId, ...)  │
   │ ──── RPC 调用 ──────────────→    │
   │                                  ▼
   │                          ProviderUserContextFilter
   │                             │ 读取 RpcContext.ServerAttachment
   │                             │ 构建 UserContext
   │                             │ UserContextHolder.set(userContext)
   │                             │
   │                          [业务逻辑执行]
   │                             │
   │                          UserContextHolder.clear() // finally
```

### 上下文字段映射

| UserContext 字段 | ThreadLocal Key | Dubbo Attachment Key |
|-----------------|----------------|---------------------|
| tenantId | UserContextHolder | `ctx_tenantId` |
| userType | UserContextHolder | `ctx_userType` |
| userId | UserContextHolder | `ctx_userId` |
| memberId | UserContextHolder | `ctx_memberId` |
| username | UserContextHolder | `ctx_username` |
| groupId | UserContextHolder | `ctx_groupId` |
| roles | UserContextHolder | `ctx_roles` |

## 关键组件索引

| 组件 | 模块 | 说明 |
|------|------|------|
| `JwtClaimConstants` | kbpd-common-core | JWT 自定义 Claim 常量定义 |
| `UserContext` | kbpd-common-core | 用户上下文模型（tenantId, userId, roles 等） |
| `UserType` | kbpd-common-core | 用户类型枚举（TO_B=1, TO_C=2） |
| `UserContextHolder` | kbpd-common-security | ThreadLocal 上下文持有者 |
| `SecurityUtils` | kbpd-common-security | 上下文访问工具类 |
| `ResourceServerConfiguration` | kbpd-common-security | 资源服务器配置 + JWT 验证 + UserContext 构建 |
| `UserContextCleanupFilter` | kbpd-common-security | 请求结束清理 ThreadLocal |
| `ConsumerUserContextFilter` | kbpd-common-security | Dubbo 消费端：UserContext → RPC Attachment |
| `ProviderUserContextFilter` | kbpd-common-security | Dubbo 提供端：RPC Attachment → UserContext |
| `TenantAwareAuthenticationFilter` | kbpd-auth | 登录时从 Client 提取租户和用户类型 |
| `CustomerAuthenticationProvider` | kbpd-auth | 自定义认证 Provider |
| `PwdUserDetailsService` | kbpd-auth | 按 userType 分流加载用户 |
| `DBRegisteredClientRepository` | kbpd-auth | 从 UPMS 数据库加载 OAuth2 Client 配置 |
| `jwtTokenCustomizer` | kbpd-auth | JWT 签发时注入自定义 Claims |
| `ExtendAuthenticationToken` | kbpd-auth | 携带 tenantId + userType 的认证 Token |

## 开发环境注意事项

在 `dev` profile 下，各微服务通过 `DevSecurityConfig` 配置 `permitAll()` 跳过安全验证。生产环境使用 `ResourceServerConfiguration` 启用完整的 JWT 验证链路。

这意味着：
- 开发时不需要有效的 JWT Token 即可访问 API
- 租户上下文在开发环境不可用（除非手动设置）
- 测试租户隔离相关功能需要使用非 `dev` profile
