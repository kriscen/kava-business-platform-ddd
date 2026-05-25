# kbpd-upms -- API 接口文档

> **前端对接**请参考 [UPMS 前端对接文档](../../08-frontend/upms-api.md)，包含 HTTP REST 接口、请求/响应字段和查询参数。
>
> 本文档保留后端内部接口定义（Dubbo RPC）。

---

## Dubbo RPC 接口

### IRemoteUserService

- **包路径：** `com.kava.kbpd.upms.api.service.IRemoteUserService`
- **Dubbo 版本：** `1.0`
- **实现类：** `RemoteUserService`（`adapter.rpc`）

| 方法签名 | 参数 | 返回值 | 说明 |
|---|---|---|---|
| `findByUsername` | `String tenantId, String username` | `SysUserDTO` | 根据租户+用户名查询用户，返回含 roles、permissions、dataScope 的完整 DTO |
| `loginByPwd` | `String name, String pwd` | `SysUserDTO` | 密码登录（桩实现，返回硬编码 id=1） |

### IRemoteOauthClientService

- **包路径：** `com.kava.kbpd.upms.api.service.IRemoteOauthClientService`
- **Dubbo 版本：** `1.0`
- **实现类：** `RemoteOauthClientService`（`adapter.rpc`）

| 方法签名 | 参数 | 返回值 | 说明 |
|---|---|---|---|
| `queryByClientId` | `String clientId` | `SysOauthClientDTO` | 根据 clientId 查询 OAuth 客户端详情 |

### IRemoteTenantService

- **包路径：** `com.kava.kbpd.upms.api.service.IRemoteTenantService`
- **Dubbo 版本：** `1.0`
- **实现类：** `RemoteTenantService`（`adapter.rpc`）

| 方法签名 | 参数 | 返回值 | 说明 |
|---|---|---|---|
| `checkTenantStatus` | `Long tenantId` | `TenantStatusDTO` | 查询租户有效状态，综合到期+显式状态返回最终结果 |

---

## Dubbo RPC DTO

### SysUserDTO

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | `Long` | 用户 ID |
| `username` | `String` | 用户名 |
| `password` | `String` | 密码（加密） |
| `deptId` | `Long` | 部门 ID |
| `tenantId` | `Long` | 租户 ID |
| `lockFlag` | `String` | 锁定标记 |
| `permissions` | `List<String>` | 权限标识列表 |
| `roles` | `List<String>` | 角色编码列表 |
| `dataScope` | `String` | 数据权限范围 |

### SysOauthClientDTO

| 字段 | 类型 | 说明 |
|---|---|---|
| `clientId` | `String` | 客户端 ID |
| `clientSecret` | `String` | 客户端密钥 |
| `scope` | `String` | 授权范围 |
| `authorizedGrantTypes` | `String[]` | 授权类型 |
| `webServerRedirectUri` | `String` | 回调地址 |
| `accessTokenValidity` | `Integer` | Access Token 有效期（秒） |
| `refreshTokenValidity` | `Integer` | Refresh Token 有效期（秒） |
| `autoapprove` | `String` | 自动授权 |
| `tenantId` | `Long` | 所属租户 |
| `userType` | `String` | 用户类型 |

### TenantStatusDTO

| 字段 | 类型 | 说明 |
|---|---|---|
| `status` | `String` | 租户有效状态码 |
| `expired` | `Boolean` | 租户是否已到期 |

---

## 接口风格说明

- **User / Role 的 PUT** 不带 `/{id}` 路径参数，ID 包含在请求体中；返回 `JsonResult<Boolean>`
- **User / Role 的 DELETE** 返回 `JsonResult<Boolean>`
- **其余资源的 PUT / DELETE** 均使用 `/{id}` 路径参数，返回 `JsonResult<Void>`
- **批量删除** 统一使用 DELETE 方法 + 请求体 `List<Long>` IDs
- **分页查询** 使用 query string 传参（`@ModelAttribute`）
- **创建 / 更新** 使用 JSON body（`@RequestBody`）
- **所有下拉接口** 不分页，返回完整列表
- **树形接口**（Menu/Dept/Area）返回嵌套 `children` 结构
