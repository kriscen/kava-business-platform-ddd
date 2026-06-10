## Why

前端对接文档错误地以 Password Grant（密码模式）为主推流程，但设计上所有客户端（B端管理后台、C端各家庭 App）都应使用 Authorization Code + PKCE 模式。当前代码未启用 PKCE（`requireProofKey`），也未配置 CORS，导致 SPA 无法通过标准 Auth Code 流程完成认证。需要修正文档、补齐 PKCE 和 CORS 配置，使前端能用标准 OAuth2 库对接。

## What Changes

- **启用 PKCE**：在 `DBRegisteredClientRepository` 构建 `ClientSettings` 时添加 `requireProofKey(true)`，所有 OAuth2 客户端必须通过 PKCE 完成授权码交换
- **添加 CORS 配置**：在 Gateway 添加 CORS 全局配置，允许前端 SPA 跨域调用 `/auth/oauth2/token` 端点
- **修正前端对接文档**：将 `docs/05-frontend/auth-guide.md` 从 Password Grant 改为 Authorization Code + PKCE 流程；同步修正 `docs/05-frontend/auth-api.md` 和 `docs/07-product/auth-chain.md`

## Capabilities

### New Capabilities

- `pkce-auth-flow`: 定义 Authorization Code + PKCE 认证流程的完整规范，包括 PKCE 参数要求、公共客户端认证方式、CORS 策略

### Modified Capabilities

- `auth-security`: `DBRegisteredClientRepository` 新增 `requireProofKey(true)` 约束，客户端必须使用 PKCE

## Impact

- **kbpd-auth**: `DBRegisteredClientRepository.java` — 添加 `requireProofKey(true)`
- **kbpd-gateway**: 新增 CORS 全局配置
- **docs/05-frontend/auth-guide.md**: 重写为 Auth Code + PKCE 流程
- **docs/05-frontend/auth-api.md**: 更新 grant type 说明
- **docs/07-product/auth-chain.md**: 更新认证流程图
