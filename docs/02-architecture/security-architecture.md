# 安全架构

## 3.1 认证流程

### 3.1.1 整体认证链路

```
┌─────────┐     ┌────────────────┐     ┌──────────────┐     ┌──────────┐
│ Client  │────▶│  kbpd-gateway  │────▶│  kbpd-auth   │────▶│ kbpd-upms│
│         │     │  (透传 Header) │     │ (OAuth2 Svr) │     │ (用户查询)│
└─────────┘     └────────────────┘     └──────┬───────┘     └──────────┘
                                              │
                                        ┌─────▼──────┐
                                        │   Redis    │
                                        │ (Token存储) │
                                        └────────────┘
```

**关键说明**：当前 Gateway **不执行 JWT 验证**，仅做 HTTP 反向代理透传。JWT 校验由下游业务服务的 Resource Server 完成。

### 3.1.2 OAuth2 授权码模式流程

```
Client                Auth Server              UPMS (Dubbo)         Redis
  │                       │                        │                  │
  │  1. GET /authorize    │                        │                  │
  │  (client_id, redirect_uri, scope)              │                  │
  │──────────────────────>│                        │                  │
  │                       │                        │                  │
  │  2. 302 → /oauth2/login                       │                  │
  │<──────────────────────│                        │                  │
  │                       │                        │                  │
  │  3. POST /oauth2/login (username+password+tenantId)               │
  │──────────────────────>│                        │                  │
  │                       │  4. findByUsername     │                  │
  │                       │  (tenantId, username)  │                  │
  │                       │───────────────────────>│                  │
  │                       │  5. SysUserDTO         │                  │
  │                       │<───────────────────────│                  │
  │                       │                        │                  │
  │                       │  6. 密码校验 + 构建 UserDetails           │
  │                       │  7. 存储 Authorization                    │
  │                       │───────────────────────────────────────── >│
  │                       │                        │                  │
  │  8. 302 → redirect_uri?code=xxx               │                  │
  │<──────────────────────│                        │                  │
  │                       │                        │                  │
  │  9. POST /token (code + client_secret)         │                  │
  │──────────────────────>│                        │                  │
  │                       │  10. 签发 JWT (RSA2048)                    │
  │                       │  11. 存储 Token                            │
  │                       │───────────────────────────────────────── >│
  │                       │                        │                  │
  │  12. JWT Response     │                        │                  │
  │<──────────────────────│                        │                  │
```

### 3.1.3 用户类型区分

OAuth2 Client 通过 `user_type` 字段区分认证通道：

| 用户类型 | 枚举值 | 认证方式 | UserDetails 类 | RPC 接口 |
|---------|--------|---------|---------------|---------|
| TO_B（系统用户） | `TO_B` ("1") | 用户名+密码 | `SysUserDetails` | `IRemoteUserService` |
| TO_C（会员） | `TO_C` ("2") | 手机号+密码 | `MemberDetails` | `IRemoteMemberService` |

`PwdUserDetailsService` 根据 `userType` 路由到不同的 RPC 接口获取用户信息。

## 3.2 Token 结构

### 3.2.1 JWT Claims 定义

| Claim | 类型 | 适用用户类型 | 说明 |
|-------|------|------------|------|
| `sub` | String | 全部 | Subject（用户名） |
| `userId` | Long | TO_B | 系统用户 ID |
| `username` | String | TO_B | 登录名 |
| `deptId` | Long | TO_B | 所属部门 ID |
| `authorities` | Set\<String\> | TO_B | 角色/权限编码集合 |
| `dataScope` | String | TO_B | 数据权限范围 |
| `memberId` | Long | TO_C | 会员 ID |

Claim Key 定义在 `kbpd-common-core` 的 `JwtClaimConstants` 中。

### 3.2.2 Token 生命周期

| Token 类型 | 默认有效期 | 存储 | 说明 |
|-----------|-----------|------|------|
| Access Token | 12 小时 | Redis（SELF_CONTAINED JWT） | 支持按 Client 覆盖 |
| Refresh Token | 30 天 | Redis | 支持按 Client 覆盖 |
| Authorization Code | 10 分钟 | Redis | OAuth2 授权码 |
| User Consent | 10 分钟 | Redis | 用户授权同意 |

**签名算法**：RSA 2048-bit，密钥对存储在 Redis `AUTH:jwk` 键中，启动时若不存在则自动生成。

### 3.2.3 Token 刷新

客户端使用 Refresh Token 调用 `/oauth2/token`（grant_type=refresh_token）获取新的 Access Token，无需用户重新登录。

## 3.3 RBAC 权限模型

### 3.3.1 数据模型

```
┌──────────┐     ┌──────────────┐     ┌──────────┐
│ sys_user │────<│ sys_user_role │>────│ sys_role │
│          │     │ (user_id,     │     │          │
│ dept_id  │     │  role_id)     │     │ ds_type  │
└──────────┘     └──────────────┘     │ ds_scope │
                                      └────┬─────┘
                                           │
                                      ┌──────────────┐     ┌──────────┐
                                      │ sys_role_menu │>────│ sys_menu │
                                      │ (role_id,     │     │          │
                                      │  menu_id)     │     │ menu_type│
                                      └──────────────┘     │permission│
                                                           └──────────┘
```

### 3.3.2 权限层级

| 层级 | menu_type | 说明 | 示例 |
|------|-----------|------|------|
| 目录 | 0 (CATALOG) | 菜单分组 | "系统管理" |
| 菜单 | 1 (MENU) | 页面级 | "用户管理" |
| 按钮 | 2 (BUTTON) | 操作级 | `sys_user_add` |

### 3.3.3 数据权限范围（DataScope）

| 范围 | ds_type 值 | 说明 |
|------|-----------|------|
| ALL | 0 | 全部数据 |
| CUSTOM | 1 | 自定义（通过 ds_scope 指定部门） |
| DEPT_AND_CHILD | 2 | 本部门及子部门 |
| DEPT_ONLY | 3 | 仅本部门 |
| SELF | 4 | 仅本人数据 |

### 3.3.4 权限流转链路

```
数据库 (sys_user_role, sys_role_menu)
    │
    ▼
PwdUserDetailsService.loadUserByUsername()
    │  收集用户的 roleCode / permission 集合
    ▼
SysUserDetails.authorities
    │
    ▼
AuthorizationServiceConfig.jwtTokenCustomizer()
    │  写入 JWT claims["authorities"]
    ▼
下游服务 ResourceServerConfiguration
    │  JwtAuthenticationConverter 读取 claims
    │  前缀为空（setAuthorityPrefix("")）
    ▼
SecurityContextHolder → @PreAuthorize("hasAuthority('sys_user_add')")
    │
    ▼
SecurityUtils.getRoles() / getUserId() / getDeptId()
```

### 3.3.5 菜单作用域

| 作用域 | 说明 |
|--------|------|
| SYSTEM | 平台级菜单，所有租户可见 |
| TENANT | 租户专属菜单 |

## 3.4 多租户隔离

### 3.4.1 隔离策略

采用**共享数据库 + 租户字段**方案：所有租户数据存储在同一数据库中，通过 `tenant_id` 字段隔离。

**选择理由**：项目初期租户数量有限，共享数据库降低运维复杂度，租户字段隔离满足数据安全要求。

### 3.4.2 租户上下文传播

```
OAuth2 Client (sys_oauth_client_details.tenant_id)
    │
    ▼
DBRegisteredClientRepository → ClientSettings 中存储 tenantId
    │
    ▼
登录页 → hidden field 传递 tenantId
    │
    ▼
TenantAwareAuthenticationFilter.attemptAuthentication()
    │  从表单提取 tenantId，构建 ExtendAuthenticationToken
    ▼
PwdUserDetailsService.loadUserByUsername(username, tenantId, userType)
    │  tenantId 作为查询条件传入 RPC 调用
    ▼
UPMS 服务按 tenantId 过滤用户数据
```

### 3.4.3 数据层租户隔离

- 所有业务表 PO 继承 `TenantCreatedPO`，包含 `tenant_id` 字段
- 当前 MyBatis-Plus 未配置 `TenantLineInnerInterceptor`，查询需手动添加 `tenant_id` 条件

> **规划中**：后续将引入 MyBatis-Plus 租户拦截器实现自动过滤，减少人工遗漏风险。

## 3.5 Resource Server（下游服务鉴权）

### 3.5.1 配置方式

业务服务通过 `@EnableResourceServer` 注解启用 JWT 校验，该注解导入 `ResourceServerConfiguration`。

**当前状态**：

| 服务 | 是否启用 | 说明 |
|------|---------|------|
| kbpd-upms | ✅ 已启用 | 使用 `@EnableResourceServer` |
| kbpd-member | ❌ 未启用 | 使用空的 `MemberSecurityConfig` |
| kbpd-gateway | N/A | 纯代理，不校验 Token |

### 3.5.2 环境差异

- **dev 环境**：`ResourceServerConfiguration` 通过 `@Profile("!dev")` 禁用，开发阶段不校验 JWT
- **非 dev 环境**：启用 JWT 校验 + 白名单路径放行

### 3.5.3 白名单配置

通过 `kbpd.security.whitelistPaths` 属性配置无需鉴权的路径，各服务在 `application.yml` 中按需指定。

## 3.6 SecurityUtils 工具类

下游服务通过 `SecurityUtils`（`kbpd-common-security`）获取当前用户上下文：

| 方法 | 返回值 | 来源 |
|------|--------|------|
| `getUserId()` | Long | JWT claim `userId` |
| `getUsername()` | String | `Authentication.getName()` |
| `getRoles()` | Collection | `Authentication.getAuthorities()` |
| `getDeptId()` | Long | JWT claim `deptId` |
| `getMemberId()` | Long | JWT claim `memberId` |
| `getTokenAttributes()` | Map | JWT 全部 claims |

## 3.7 规划状态

| 能力 | 状态 | 说明 |
|------|------|------|
| OAuth2 授权码模式 | ✅ 已实现 | Auth Server 完整流程 |
| JWT 签发与验证 | ✅ 已实现 | RSA2048，Redis 存储 |
| RBAC 权限模型 | ✅ 已实现 | 用户→角色→菜单/按钮 |
| 多租户登录隔离 | ✅ 已实现 | OAuth2 Client 绑定租户 |
| Gateway JWT 鉴权 | 🟡 规划中 | 当前透传，不校验 |
| MyBatis-Plus 租户拦截器 | 🟡 规划中 | 当前查询手动过滤 |
| Member 服务 Resource Server | 🟡 规划中 | 未启用 JWT 校验 |
| Gateway 限流熔断 | 🟡 规划中 | Sentinel 依赖已引入，规则未配置 |
