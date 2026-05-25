# kbpd-auth -- API 接口文档

> **前端对接**请参考 [Auth 前端对接文档](../../08-frontend/auth-api.md)，包含登录流程、OAuth2 端点和 JWT Token 结构。
>
> 本文档保留后端内部接口定义（Dubbo RPC 依赖、静态资源配置）。

---

## Dubbo RPC 依赖接口

本模块作为 Dubbo Consumer 调用以下远程接口：

### IRemoteUserService（来自 kbpd-upms-api）

| 方法 | 说明 |
|------|------|
| `findByUsername(username)` | B 端用户查询，返回用户信息 |

### IRemoteMemberService（来自 kbpd-member-api）

| 方法 | 说明 |
|------|------|
| `findMemberByMobile(mobile)` | C 端会员查询，返回会员信息 |

### IRemoteOauthClientService（来自 kbpd-upms-api）

| 方法 | 说明 |
|------|------|
| `getByClientId(clientId)` | 根据 Client ID 查询 OAuth2 客户端配置 |

---

## 静态资源与白名单

以下路径绕过 Security 过滤链（`WebSecurityCustomizer` 配置）：

| 路径 | 说明 |
|------|------|
| `/webjars/**` | WebJars 资源 |
| `/doc.html` | Knife4j 文档页 |
| `/swagger-resources/**` | Swagger 资源 |
| `/v3/api-docs/**` | OpenAPI 3.0 文档 |
| `/swagger-ui/**` | Swagger UI |
| `/assets/**` | 静态资源（CSS、JS 等） |

此外，可通过 `kbpd.auth.whitelist-paths` 配置额外白名单路径。
