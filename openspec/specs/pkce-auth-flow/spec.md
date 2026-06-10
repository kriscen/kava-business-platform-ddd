# Capability: pkce-auth-flow

## Purpose

定义 Kava 平台统一的 Authorization Code + PKCE 认证流程规范，覆盖前端 SPA 发起认证、用户在 Auth 服务登录页认证、授权码交换、Token 获取的完整流程。所有客户端（B端管理后台、C端各家庭 App）MUST 使用此流程。

## Requirements

### Requirement: OAuth2 客户端必须使用 PKCE

所有通过浏览器或移动端接入的 OAuth2 客户端 MUST 使用 Authorization Code + PKCE 流程。`DBRegisteredClientRepository` 构建 `ClientSettings` 时 MUST 设置 `requireProofKey(true)`。

#### Scenario: 客户端发起授权请求时携带 PKCE 参数
- **WHEN** 前端 SPA 发起 `GET /auth/oauth2/authorize` 请求
- **THEN** 请求 MUST 包含 `code_challenge` 参数
- **AND** MUST 包含 `code_challenge_method` 参数，值为 `S256`
- **AND** 请求 MUST 包含 `response_type=code`

#### Scenario: 授权请求缺少 PKCE 参数被拒绝
- **WHEN** 前端发送的授权请求不包含 `code_challenge` 参数
- **THEN** Auth 服务 MUST 拒绝该请求
- **AND** 返回 OAuth2 错误响应

### Requirement: 授权码交换必须验证 PKCE code_verifier

前端使用授权码换取 Token 时，MUST 提供 `code_verifier` 参数。Auth 服务 MUST 验证 `code_verifier` 与授权请求中的 `code_challenge` 匹配。

#### Scenario: PKCE 验证通过，签发 Token
- **WHEN** 前端发送 `POST /auth/oauth2/token` 请求，包含 `grant_type=authorization_code`、`code`、`redirect_uri` 和 `code_verifier`
- **AND** `code_verifier` 经 SHA-256 哈希后与存储的 `code_challenge` 匹配
- **THEN** Auth 服务 MUST 签发 `access_token` 和 `refresh_token`
- **AND** 返回标准 OAuth2 Token Response

#### Scenario: PKCE 验证失败，拒绝 Token 签发
- **WHEN** 前端发送的 `code_verifier` 与 `code_challenge` 不匹配
- **THEN** Auth 服务 MUST 拒绝 Token 签发
- **AND** 返回 `invalid_grant` 错误

### Requirement: 公共客户端不需要 client_secret

使用 PKCE 的客户端 MUST 作为 Public Client 运行，不需要 `client_secret` 进行认证。Token 端点 MUST 支持仅通过 `client_id` + `code_verifier` 完成授权码交换。

#### Scenario: 前端仅用 client_id 和 code_verifier 换取 Token
- **WHEN** 前端发送 `POST /auth/oauth2/token` 请求，包含 `client_id`（在请求体中）、`code`、`redirect_uri`、`code_verifier`
- **AND** 不包含 `client_secret` 或 `Authorization` Header
- **THEN** Auth 服务 MUST 正常处理请求
- **AND** 验证通过后签发 Token

### Requirement: Gateway 配置 CORS 允许前端调用 Token 端点

Gateway MUST 配置全局 CORS 策略，允许前端 SPA 跨域调用 `/auth/oauth2/token` 端点。

#### Scenario: 前端跨域调用 Token 端点成功
- **WHEN** 前端 SPA（origin 为配置的允许域名）发送 `POST /auth/oauth2/token` 请求
- **THEN** Gateway MUST 在响应中包含正确的 CORS Headers
- **AND** `Access-Control-Allow-Origin` MUST 为请求的 Origin（不使用 `*`）
- **AND** `Access-Control-Allow-Methods` MUST 包含 `GET, POST`
- **AND** `Access-Control-Allow-Headers` MUST 包含 `Authorization, Content-Type`

#### Scenario: 非 Allowed Origin 的跨域请求被拒绝
- **WHEN** 前端 SPA 的 Origin 不在配置的允许列表中
- **THEN** Gateway MUST NOT 返回 CORS Headers
- **AND** 浏览器阻止该请求

#### Scenario: 开发环境允许 localhost
- **WHEN** Gateway 以 dev profile 运行
- **THEN** CORS Allowed Origins MUST 包含 `http://localhost:*`
- **AND** 允许本地开发的前端调试对接

### Requirement: 完整的 Auth Code + PKCE 登录流程

系统 MUST 支持以下完整的认证流程：前端重定向到 Auth 服务 → 用户在 Auth 服务登录页输入凭证 → 认证成功后重定向回前端并携带授权码 → 前端用授权码 + PKCE 换取 Token。

#### Scenario: B端用户完整登录流程
- **WHEN** B端管理后台 SPA 需要用户登录
- **THEN** SPA MUST 重定向浏览器到 `GET /auth/oauth2/authorize?client_id={clientId}&response_type=code&redirect_uri={redirectUri}&code_challenge={challenge}&code_challenge_method=S256&scope={scopes}&state={state}`
- **AND** Auth 服务 MUST 展示登录页（根据 client_id 确定 tenantId 和 userType）
- **AND** 用户提交凭证后，Auth 服务 MUST 认证并 302 重定向到 `redirect_uri?code={authCode}&state={state}`
- **AND** SPA MUST 从 URL 提取 code 和 state，验证 state 匹配
- **AND** SPA MUST 发送 `POST /auth/oauth2/token` 用 code + code_verifier 换取 Token

#### Scenario: C端用户完整登录流程
- **WHEN** C端家庭 App 需要用户登录
- **THEN** 流程 MUST 与 B端相同，使用对应的 C端 client_id
- **AND** Auth 服务 MUST 根据 client_id 使用 userType=TO_C 查询 Member 用户

#### Scenario: state 不匹配时前端中止流程
- **WHEN** SPA 收到回调后验证 `state` 参数与发起时不一致
- **THEN** SPA MUST 中止认证流程
- **AND** 不得使用收到的 `code` 换取 Token

### Requirement: Token 刷新使用标准 OAuth2 流程

当 access_token 过期时，前端 MUST 使用 `refresh_token` 获取新的 Token 对，无需用户重新走 Auth Code 流程。

#### Scenario: 使用 refresh_token 刷新 Token
- **WHEN** 前端发送 `POST /auth/oauth2/token` 请求，包含 `grant_type=refresh_token` 和 `refresh_token`
- **AND** refresh_token 在有效期内
- **THEN** Auth 服务 MUST 签发新的 `access_token` 和 `refresh_token`
- **AND** 旧的 refresh_token MUST 失效

#### Scenario: refresh_token 过期后重新登录
- **WHEN** 前端使用的 `refresh_token` 已过期
- **THEN** Auth 服务 MUST 返回 `invalid_grant` 错误
- **AND** 前端 MUST 重新发起 Auth Code + PKCE 流程
