## Context

当前 Kava 平台的认证系统基于 Spring Authorization Server 实现，支持 Authorization Code 和 Password 两种 Grant Type。但代码中 `DBRegisteredClientRepository` 未启用 PKCE（`requireProofKey`），Gateway 和 Auth 服务也未配置 CORS。前端对接文档以 Password Grant 为主，与实际设计意图（统一使用 Auth Code + PKCE）不一致。

前端 SPA（B端管理后台、C端各家庭 App）需要通过浏览器完成 OAuth2 认证流程。由于 SPA 无法安全保管 `client_secret`，必须启用 PKCE 使其作为 Public Client 安全地完成授权码交换。

## Goals / Non-Goals

**Goals:**
- 启用 PKCE，使所有 OAuth2 客户端通过 Public Client + PKCE 完成认证
- 补齐 CORS 配置，允许前端 SPA 跨域调用 Token 端点
- 修正前端对接文档，完整描述 Auth Code + PKCE 流程

**Non-Goals:**
- 登录页按 client_id 定制（已决定延后）
- 实现前端 SPA 代码（仅提供对接文档）
- 修改 JWT claims 结构或 Token 签发逻辑
- 重构 Auth 服务架构

## Decisions

### D1: 在 DBRegisteredClientRepository 全局启用 PKCE

在构建 `ClientSettings` 时，普通客户端设置 `.requireProofKey(true)`，工具类客户端（如 Knife4j 调试客户端 `local`）设置 `.requireProofKey(false)`。

**Because**: 所有客户端（B端 SPA、C端 App）都是浏览器或移动端应用，无法安全保管 `client_secret`。PKCE 是 RFC 7636 为 Public Client 设计的标准机制。Spring Authorization Server 原生支持，只需一行配置启用。工具类客户端通过 `kbpd.auth.tool-client-id` 配置识别，它们使用 `client_secret` 认证，不需要 PKCE。

### D2: CORS 配置放在 Gateway 层

在 Spring Cloud Gateway 添加全局 CORS 配置，而非在 Auth 服务单独配置。

**Because**: Gateway 是所有前端请求的统一入口（端口 8500），Token 端点通过 Gateway 路由访问（`/auth/oauth2/token`）。在 Gateway 层统一配置 CORS，保持 Auth 服务职责单一，且未来其他服务的跨域需求也无需重复配置。

**配置范围**：
- Allowed Origins: 从配置文件读取（开发环境允许 localhost）
- Allowed Methods: GET, POST
- Allowed Headers: Authorization, Content-Type
- Max Age: 3600s

### D3: 文档以 Auth Code + PKCE 为唯一主推流程

**Because**: Password Grant 前端直接接触用户密码，安全风险高。Auth Code + PKCE 是 OAuth 2.1 推荐的唯一浏览器端认证方式。统一流程降低前端对接复杂度。

## Risks / Trade-offs

- **已有客户端兼容性**: 启用 PKCE 后，所有客户端的 `/oauth2/authorize` 请求必须携带 `code_challenge` 参数，否则被拒绝。如果存在未实现 PKCE 的旧客户端（如 Knife4j 的 `local`），需要同步更新其配置或将其设为免 PKCE。 → 建议确认 `local` 客户端的使用场景，如果是 Swagger/Knife4j 内部使用，可考虑对其不强制 PKCE，或改用 client_credentials。
- **CORS origin 配置**: 生产环境的 allowed origins 需要在部署时正确配置，不能使用 `*`。 → 通过 Nacos 配置管理，开发环境允许 localhost。
