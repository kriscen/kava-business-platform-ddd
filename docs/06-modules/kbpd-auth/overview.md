# kbpd-auth — OAuth2 认证授权服务

## 模块定位

`kbpd-auth` 是平台的统一认证授权中心，基于 **Spring Authorization Server** 实现 OAuth2 / OIDC 协议。它不遵循 DDD 分层架构（与其他业务模块不同），而是一个独立的 Spring Boot 应用，专注于认证协议处理。

**核心职责**：

- 提供标准 OAuth2 Authorization Code + Password 授权流程
- 管理 OAuth2 Client 注册信息（从 UPMS 服务通过 Dubbo RPC 加载）
- 签发和存储 JWT Access Token / Refresh Token
- 支持多租户、双用户类型（B 端系统用户 / C 端会员）的认证路由
- 提供登录、授权确认页面（Thymeleaf 模板渲染）

**不属于本模块的职责**：

- 用户/会员的 CRUD 管理 → `kbpd-upms` / `kbpd-member`
- Token 校验与鉴权 → 各 Resource Server 自行通过 JWT 公钥验证
- API 路由转发 → `kbpd-gateway`

## 技术选型

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Authorization Server | 随 Spring Boot 3.3.x | OAuth2 / OIDC 协议实现 |
| Apache Dubbo | 3.3.x | 远程调用 UPMS / Member 服务获取用户和客户端信息 |
| Redis | — | Token、Authorization、Consent 存储 |
| Thymeleaf | — | 登录 / 授权确认 / 错误页面渲染 |
| JWT (RSA 2048) | — | Access Token 签发 |

## 目录结构

```
kbpd-auth/
├── pom.xml
└── src/main/java/com/kava/kbpd/auth/
    ├── AuthApplication.java                          # Spring Boot 入口，@EnableDubbo
    ├── config/
    │   ├── AuthorizationServiceConfig.java           # OAuth2 Authorization Server 核心配置
    │   ├── DefaultSecurityConfig.java                # 默认 Security 过滤链 + 白名单
    │   └── KbpdAuthProperties.java                   # 自定义配置属性（Token TTL、白名单等）
    ├── constants/
    │   └── AuthConstants.java                        # URL / 属性常量
    ├── controller/
    │   └── OauthController.java                      # 登录 / 授权确认 / 错误页面
    ├── enums/
    │   └── AuthRedisKeyType.java                     # Redis Key 类型枚举
    ├── model/
    │   ├── ExtendAuthenticationToken.java            # 扩展 AuthenticationToken（含 tenantId、userType）
    │   ├── SysUserDetails.java                       # B 端系统用户 UserDetails
    │   ├── MemberDetails.java                        # C 端会员 UserDetails
    │   └── PwdLoginRequest.java                      # 密码登录请求 DTO
    └── oauth2/
        ├── component/
        │   ├── TenantAwareAuthenticationFilter.java          # 租户感知的认证过滤器
        │   ├── CustomerAuthenticationProvider.java           # 自定义认证 Provider
        │   ├── PwdUserDetailsService.java                   # 多租户用户加载（Dubbo RPC）
        │   ├── DBRegisteredClientRepository.java            # DB-backed Client 注册仓库
        │   ├── RedisAuthorizationService.java               # Redis 存储 OAuth2Authorization
        │   ├── RedisAuthorizationConsentService.java        # Redis 存储 OAuth2AuthorizationConsent
        │   ├── AuthenticationSuccessEventHandler.java       # Token 签发成功处理
        │   ├── AuthenticationFailureEventHandler.java       # Token 签发失败处理
        │   └── CustomLoginUrlAuthenticationEntryPoint.java  # 登录入口（携带 clientId）
        ├── jackson/                                         # OAuth2 对象 Redis 序列化
        │   ├── CustomerOauth2Module.java                    # OAuth2 对象 Jackson Module
        │   ├── CustomerUserDetailsModule.java               # UserDetails Jackson Module
        │   └── ...Mixin.java / ...Deserializer.java         # 各类的 Mixin 与反序列化器
        └── service/
            └── PwdUserDetailsService.java                   # 按用户类型路由加载
```

## 架构设计

### 双 Security 过滤链

遵循 Spring Authorization Server 约定，配置两条过滤链：

1. **`authorizationServerSecurityFilterChain`**（`@Order(HIGHEST_PRECEDENCE)`）：处理 OAuth2 协议端点（`/oauth2/authorize`、`/oauth2/token` 等）
2. **`defaultSecurityFilterChain`**（`@Order(10)`）：处理其余请求，包括登录表单、静态资源等

### 多租户认证流程

认证流程中贯穿 `tenantId` 和 `userType` 两个维度：

```
用户访问 /oauth2/authorize
  → CustomLoginUrlAuthenticationEntryPoint 携带 client_id 跳转登录页
  → OauthController 通过 client_id 查询 tenantId、userType，渲染登录表单
  → TenantAwareAuthenticationFilter 提取 tenantId、userType，构建 ExtendAuthenticationToken
  → CustomerAuthenticationProvider 调用 PwdUserDetailsService
  → PwdUserDetailsService 按 userType 路由：
      - TO_B → IRemoteUserService (Dubbo → kbpd-upms)
      - TO_C → IRemoteMemberService (Dubbo → kbpd-member)
  → JWT 自定义 Claims 注入用户信息
```

### Token 存储策略

所有 OAuth2 运行时状态存储在 Redis 中，不依赖数据库：

| 对象 | Redis Key 模式 | TTL |
|------|---------------|-----|
| OAuth2Authorization (state) | `auth:token:{type}:{id}` | `authorizationTimeout`（默认 10 分钟） |
| Authorization Code | 同上 | code 自身的有效期 |
| Access Token | `auth:token:access_token:{value}` | `accessTokenValiditySeconds`（默认 12 小时） |
| Refresh Token | `auth:token:refresh_token:{value}` | `refreshTokenValiditySeconds`（默认 30 天） |
| OAuth2AuthorizationConsent | `auth:consent:{principalName}:{clientId}` | `authorizationConsentTimeout`（默认 10 分钟） |
| JWK 密钥对 | `auth:jwk` | 永不过期 |

### JWT 自定义 Claims

根据认证用户类型，在 Access Token 中注入不同 Claims：

| 用户类型 | Claims |
|----------|--------|
| B 端（SysUserDetails） | `userId`、`username`、`deptId`、`authorities` |
| C 端（MemberDetails） | `memberId` |

### 外部依赖

本模块通过 Dubbo RPC 依赖以下服务：

| RPC 接口 | 所属模块 | 用途 |
|----------|----------|------|
| `IRemoteUserService` | `kbpd-upms-api` | B 端用户查询 |
| `IRemoteMemberService` | `kbpd-member-api` | C 端会员查询 |
| `IRemoteOauthClientService` | `kbpd-upms-api` | OAuth2 Client 配置查询 |

## 配置项

配置前缀：`kbpd.auth`

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `tool-client-id` | List\<String\> | `["local"]` | 工具类 Client ID，返回原始 Token 响应（不包裹 JsonResult） |
| `whitelist-paths` | List\<String\> | — | 白名单路径，无需认证 |
| `authorization-timeout` | Long | 600000 (10min) | Redis 中 Authorization 状态 TTL（ms） |
| `authorization-consent-timeout` | Long | 600000 (10min) | Redis 中 Consent TTL（ms） |
| `refresh-token-validity-seconds` | Integer | 2592000 (30d) | Refresh Token 有效期（秒） |
| `access-token-validity-seconds` | Integer | 43200 (12h) | Access Token 有效期（秒） |

## 运行

```bash
# 启动前确保 Nacos 和 Redis 已运行
cd kbpd-auth && mvn spring-boot:run
```

服务端口：**8600**，Context Path：**/auth**

## 当前开发状态（TODO）

- `PwdUserDetailsService` 返回硬编码用户数据，尚未对接真实 RPC 返回值
- `TenantAwareAuthenticationFilter` 未校验 `userType`、`tenantId`、`clientId` 的匹配关系
- `OauthController` 计划根据 `userType` 和 `tenantId` 动态加载不同的登录/授权页面
- 第三方登录（微信、Gitee、Github、QQ）依赖已声明但注释掉，尚未启用
