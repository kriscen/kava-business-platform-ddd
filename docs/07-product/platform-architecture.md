# 平台架构概念

## 整体架构

```
┌──────────────────────────────────────────────────────────┐
│                        Kava 平台                          │
│                                                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐               │
│  │  Gateway  │  │   Auth   │  │   UPMS   │               │
│  │  纯路由   │  │ OAuth2   │  │ 用户权限  │               │
│  └──────────┘  └──────────┘  └──────────┘               │
│                     │                                    │
│  ┌──────────┐  ┌────▼─────┐                              │
│  │  Member   │  │  Nacos   │                              │
│  │  会员服务  │  │ 注册配置  │                              │
│  └──────────┘  └──────────┘                              │
│                                                          │
│  ┌─────────────────────────────────────────────────┐     │
│  │            公共服务层 (kbpd-common)               │     │
│  │  Core | Database | Security | Cache | Web       │     │
│  └─────────────────────────────────────────────────┘     │
└──────────────────────────────────────────────────────────┘
         ▲                  ▲                  ▲
         │ HTTP API         │ HTTP API         │ HTTP API
         │ Bearer Token 透传│ Bearer Token 透传│ Bearer Token 透传
    ┌────┴─────┐      ┌────┴─────┐      ┌────┴─────┐
    │ 家庭 A    │      │ 家庭 B    │      │ 家庭 C    │
    │ App(s)   │      │ App(s)   │      │ App(s)   │
    └──────────┘      └──────────┘      └──────────┘
    (独立开发的垂直产品)  (独立开发的垂直产品)  (独立开发的垂直产品)
```

## Gateway 设计决策

Gateway 保持**纯路由**功能，不承担鉴权职责（刻意设计，非缺失）：

- **职责**：请求路由转发、Bearer Token 透传到下游微服务
- **不负责**：JWT 验证、租户上下文注入、API 鉴权
- **鉴权位置**：各下游微服务通过 `@EnableResourceServer` 自行验证 JWT 并构建用户上下文

这样设计的原因：
1. 各微服务独立验证 JWT，无单点故障
2. 微服务间 Dubbo RPC 调用也需要鉴权，Gateway 无法覆盖
3. 保持 Gateway 轻量，专注流量路由

## 租户模型

### 核心概念

- **租户（Tenant）= 家庭（Family）**：平台的基本组织单位
- 每个租户拥有独立的：成员列表、权限配置、App 注册信息、业务数据
- 租户创建后由租户管理员（家庭管理员）管理

### 租户生命周期

```
创建租户 → 配置家庭信息 → 邀请成员 → 注册 App → 日常使用
                                                      ↓
                                            停用/冻结/删除
```

### 数据隔离策略

| 策略 | 说明 |
|------|------|
| 隔离方式 | 共享数据库 + 租户字段过滤（tenant_id） |
| 查询隔离 | 所有 SQL 查询必须包含 tenant_id 条件 |
| Domain 隔离 | Domain 层的聚合根必须携带租户 ID |
| PO 层隔离 | 数据库 PO 继承 `TenantCreatedPO`，自动携带 tenant_id |
| 超级权限 | 仅平台管理员可跨租户操作，且仅限租户元数据 |

## App 模型

### App 与 OAuth2 Client 的关系

当前阶段 **App = OAuth2 Client**（`SysOauthClientEntity`），一个注册的 OAuth2 Client 就代表一个 App：

```
家庭（租户）
├── App A ──→ OAuth2 Client A (client_id_a, tenant_id=1, user_type=C)
├── App B ──→ OAuth2 Client B (client_id_b, tenant_id=1, user_type=C)
└── App C ──→ OAuth2 Client C (client_id_c, tenant_id=1, user_type=C)

App A/B/C 的用户 → 都是该家庭的 Member
```

### OAuth2 Client 的关键字段

| 字段 | 说明 |
|------|------|
| `tenant_id` | 归属的租户（家庭），决定用户归属哪个家庭 |
| `user_type` | 用户类型：`1`=B端（管理员），`2`=C端（App 终端用户） |
| `client_id` | App 的唯一标识 |
| `client_secret` | App 的认证密钥 |
| `authorized_grant_types` | 支持的授权类型 |
| `web_server_redirect_uri` | 回调地址 |

### App 注册流程

```
家庭管理员                    Kava 后台                     UPMS
   │                           │                            │
   │── 注册新 App ──────────→ │                            │
   │  (App名称、回调地址等)     │── 创建 OAuth2 Client ────→│
   │                           │    绑定 tenant_id,         │
   │                           │    设置 user_type=C        │
   │←── 返回 client_id/secret │                            │
```

> **注意**：当前 `SysOauthClient` 同时承担了 App 业务属性和认证配置。未来如果 App 概念变得更复杂（如需要 App 名称、图标、独立生命周期管理），可能需要拆分为独立的 `SysApp` 实体。

## OAuth2 登录流程

### 当前实现：密码模式（Password Grant）

当前阶段使用密码模式完成第一步的流程串联，后续会扩展更多 OAuth2 授权类型。

```
客户端/App                    Auth 服务                     UPMS/Member
   │                           │                            │
   │── POST /oauth2/login ───→│                            │
   │   (username, password,    │                            │
   │    client_id)             │── 查询 Client 配置 ──────→│
   │                           │←── tenant_id, user_type ──│
   │                           │                            │
   │                           │── 校验用户凭据 ──────────→│
   │                           │    (B端→sys_user           │
   │                           │     C端→member)            │
   │                           │←── 用户信息+权限 ─────────│
   │                           │                            │
   │                           │── 签发 JWT ──              │
   │                           │   (含 tenant_id, user_id,  │
   │                           │    user_type, roles 等)    │
   │←── access_token ─────────│                            │
   │    + refresh_token        │                            │
```

### 关键设计点

1. **Client 决定租户和用户类型**：登录请求携带 `client_id`，Auth 服务从 Client 配置中提取 `tenant_id` 和 `user_type`
2. **用户类型分流**：`user_type=1`(B端) 查 `sys_user` 表；`user_type=2`(C端) 查 `member` 服务
3. **JWT 携带完整上下文**：Token 中包含 tenantId、userId/memberId、userType、roles 等，下游服务直接使用

> 详细的认证链路和 Token 结构请参考 [auth-chain.md](auth-chain.md)。

## 用户模型

### 单账号单租户

```
User 1 ──────→ Tenant A（家庭 A）
User 2 ──────→ Tenant A（家庭 A）
User 3 ──────→ Tenant B（家庭 B）

✗ 不支持：User 1 同时属于 Tenant A 和 Tenant B
```

### 用户与认证方式

| 用户类型 | 认证方式 | Token 特点 |
|---------|---------|-----------|
| 平台管理员 | Kava 后台登录（通过 B端 Client） | userType=1，无租户上下文或全局权限 |
| 租户管理员 | Kava 后台登录（通过 B端 Client） | userType=1，Token 绑定 tenant_id |
| App 终端用户 | App 登录（通过 C端 Client） | userType=2，Token 绑定 tenant_id + member_id |

## 模块职责与产品关系

| 模块 | 产品角色 | 当前状态 |
|------|---------|---------|
| kbpd-gateway | 请求路由转发、Bearer Token 透传 | 已就绪（纯路由设计） |
| kbpd-auth | OAuth2 授权服务器、统一认证中心 | 密码模式已支持，多租户认证已支持 |
| kbpd-upms | 用户管理、角色权限、租户管理、OAuth2 Client 管理 | DDD 结构搭建中 |
| kbpd-member | 会员（家庭成员）管理，C端用户数据 | DDD 结构搭建中 |
| kbpd-common | 共享基础设施（安全、数据库、缓存等） | JWT 验证、UserContext 传播已实现 |

## 扩展方向

1. **更多 OAuth2 授权类型**：支持授权码模式、客户端凭证模式等
2. **App 市场**：家庭可浏览和接入预制 App 模板
3. **平台级服务**：消息推送、数据分析、文件存储等共享服务
4. **计费系统**：基于租户的订阅或用量计费（商业模式确定后建设）
5. **开放 API**：为 App 开发者提供标准化的平台能力 API
