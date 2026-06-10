## ADDED Requirements

### Requirement: DBRegisteredClientRepository 启用 PKCE

`DBRegisteredClientRepository` 构建 `ClientSettings` 时 MUST 调用 `.requireProofKey(true)`，强制所有 OAuth2 客户端在 Authorization Code 流程中使用 PKCE。

#### Scenario: 新客户端自动启用 PKCE
- **WHEN** `DBRegisteredClientRepository.findByClientId()` 从数据库加载客户端配置并构建 `RegisteredClient`
- **THEN** `ClientSettings` MUST 设置 `requireProofKey` 为 `true`
- **AND** 所有通过该客户端发起的授权请求 MUST 携带 `code_challenge` 参数

#### Scenario: 授权请求未携带 code_challenge 被拒绝
- **WHEN** 客户端发送 `GET /auth/oauth2/authorize` 请求但未携带 `code_challenge` 参数
- **THEN** Spring Authorization Server MUST 拒绝该请求
- **AND** 返回 OAuth2 错误响应，错误码为 `invalid_request`

#### Scenario: 工具类客户端免除 PKCE
- **WHEN** 客户端 ID 在 `kbpd.auth.tool-client-id` 配置列表中（如 Knife4j 调试客户端 `local`）
- **THEN** `ClientSettings` MUST 设置 `requireProofKey` 为 `false`
- **AND** `ClientAuthenticationMethod` MUST 为 `CLIENT_SECRET_BASIC` + `CLIENT_SECRET_POST`
- **AND** 该客户端可使用 Password Grant 且不需要 PKCE

#### Scenario: 普通客户端使用 Public Client 模式
- **WHEN** 客户端 ID 不在 `kbpd.auth.tool-client-id` 配置列表中
- **THEN** `ClientSettings` MUST 设置 `requireProofKey` 为 `true`
- **AND** `ClientAuthenticationMethod` MUST 为 `NONE`
- **AND** Token 端点 MUST 支持仅通过 `client_id` + `code_verifier` 完成授权码交换，不需要 `client_secret`
