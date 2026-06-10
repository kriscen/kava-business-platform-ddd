## 1. 启用 PKCE（kbpd-auth）

- [x] 1.1 在 `DBRegisteredClientRepository.java` 的 `findByClientId()` 方法中，构建 `ClientSettings` 时添加 `.requireProofKey(true)`
- [x] 1.2 确认 `local`（Knife4j/Swagger）客户端的使用场景：如为内部调试工具，评估是否需要免除 PKCE 或改用 `client_credentials`

## 2. 添加 CORS 配置（kbpd-gateway）

- [x] 2.1 在 `kbpd-gateway` 的 `application-dev.yml`（或通过 Nacos 配置）中添加 Spring Cloud Gateway 全局 CORS 配置，允许 `http://localhost:*` 的 GET/POST 请求
- [x] 2.2 准备生产环境的 CORS 配置模板（allowed-origins 通过配置文件注入，不硬编码）

## 3. 修正前端对接文档（docs/）

- [x] 3.1 重写 `docs/05-frontend/auth-guide.md`：主推流程从 Password Grant 改为 Authorization Code + PKCE，包含完整的 7 步流程（发起授权 → 登录页认证 → 回调获取 code → PKCE 换 token → 存储 token → 业务请求 → token 刷新）
- [x] 3.2 更新 `docs/05-frontend/auth-api.md`：强调 `authorization_code` 为推荐 Grant Type，标注 PKCE 必需参数（`code_challenge`、`code_challenge_method`、`code_verifier`）
- [x] 3.3 更新 `docs/07-product/auth-chain.md`：登录阶段流程图从 POST /login 改为 Auth Code + PKCE 完整流程

## 4. 验证

- [x] 4.1 启动 Auth 服务，发送不带 `code_challenge` 的 `/oauth2/authorize` 请求，验证被拒绝
- [x] 4.2 使用支持 PKCE 的 OAuth2 调试工具（或 curl 模拟 PKCE 流程），验证完整的 Auth Code + PKCE 流程可走通
- [x] 4.3 验证 CORS：从 localhost 发起跨域 OPTIONS 预检请求到 `/auth/oauth2/token`，确认返回正确的 CORS Headers
