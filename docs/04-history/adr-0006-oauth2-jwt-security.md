# 2024-02-20 安全架构：OAuth2 + JWT

## 状态
已采纳

## 背景

企业级平台需要统一的认证授权机制：

- **多租户安全**：不同租户数据隔离，需验证请求所属租户
- **SSO 需求**：多个微服务需要统一登录入口
- **API 安全**：移动端、Web 端、第三方 API 都需要安全访问
- **Token 管理**：需要支持 Token 刷新、失效等生命周期管理

## 决策

采用 **OAuth2 + JWT** 作为统一认证授权方案：

```
┌─────────┐     ┌────────────┐     ┌────────────┐     ┌──────────┐
│ Client  │────▶│  Gateway   │────▶│ Auth Server │────▶│ Service  │
└─────────┘     │ (JWT Verify)│     │ (Token Issue)│     └──────────┘
                      │              │
                      │         ┌────▼────┐
                      │         │  User   │
                      │         │  Store  │
                      │         └─────────┘
```

**核心组件**：

| 组件 | 职责 |
|------|------|
| kbpd-auth | OAuth2 授权服务器，Token 发放与刷新 |
| kbpd-gateway | JWT 验签、路由拦截、租户解析 |
| kbpd-upms | 用户、角色、权限数据提供 |

**Token 结构**：

```json
{
  "sub": "user_123",
  "tenant_id": "tenant_456",
  "roles": ["admin", "user"],
  "exp": 1709900000
}
```

## 理由

1. **行业标准**：OAuth2 是 RFC 标准，生态成熟
2. **JWT 无状态**：Gateway 可直接验签，无需回源 Auth Server
3. **支持 SSO**：授权码模式支持跨服务单点登录
4. **多租户友好**：Token 中携带 tenant_id，便于网关解析

## 替代方案

| 方案 | 优点 | 缺点 |
|------|------|------|
| Session + Redis | 简单 | 无法跨域，扩展性差 |
| 自定义 Token | 完全可控 | 安全风险，维护成本高 |
| SAML 2.0 | 企业 SSO 成熟 | 复杂，XML 解析开销大 |

## 后果

- **正面**：统一认证，多服务共享同一 Token 体系
- **正面**：JWT 无状态，Gateway 验签性能高
- **约束**：所有外部 API 必须经过 Gateway，内部服务通过 Dubbo RPC
- **约束**：Token 过期时间统一管理（建议 Access Token 1h，Refresh Token 7d）
