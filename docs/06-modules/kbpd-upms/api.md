# kbpd-upms -- API 接口文档

## HTTP REST 接口

所有接口的基础路径：`/api/{version}/sys/<resource>/`，其中 `{version}` 取自 `app.config.api-version`。

通用响应包装：`JsonResult<T>`

---

### 用户管理 `/api/{version}/sys/user/`

| 方法 | 路径 | 入参 | 返回值 | 说明 |
|---|---|---|---|---|
| GET | `/page` | `SysUserAdapterListQuery` (query) | `JsonResult<PagingInfo<SysUserListResponse>>` | 分页查询用户 |
| GET | `/{id}` | `id` (path) | `JsonResult<SysUserDetailResponse>` | 用户详情 |
| POST | — | `SysUserRequest` (body) | `JsonResult<Long>` | 创建用户，返回新 ID |
| PUT | — | `SysUserRequest` (body，含 id) | `JsonResult<Void>` | 更新用户 |
| DELETE | — | `List<Long>` (body) | `JsonResult<Void>` | 批量删除用户 |

### 角色管理 `/api/{version}/sys/role/`

| 方法 | 路径 | 入参 | 返回值 | 说明 |
|---|---|---|---|---|
| GET | `/page` | `SysRoleAdapterListQuery` (query) | `JsonResult<PagingInfo<SysRoleListResponse>>` | 分页查询角色 |
| GET | `/{id}` | `id` (path) | `JsonResult<SysRoleDetailResponse>` | 角色详情 |
| POST | — | `SysRoleRequest` (body) | `JsonResult<Long>` | 创建角色 |
| PUT | — | `SysRoleRequest` (body，含 id) | `JsonResult<Void>` | 更新角色 |
| DELETE | — | `List<Long>` (body) | `JsonResult<Void>` | 批量删除角色 |

### 菜单管理 `/api/{version}/sys/menu/`

| 方法 | 路径 | 入参 | 返回值 | 说明 |
|---|---|---|---|---|
| GET | `/page` | `SysMenuAdapterListQuery` (query) | `JsonResult<PagingInfo<SysMenuListResponse>>` | 分页查询菜单 |
| GET | `/{id}` | `id` (path) | `JsonResult<SysMenuDetailResponse>` | 菜单详情 |
| POST | — | `SysMenuRequest` (body) | `JsonResult<Long>` | 创建菜单 |
| PUT | `/{id}` | `id` (path) + `SysMenuRequest` (body) | `JsonResult<Boolean>` | 更新菜单 |
| DELETE | — | `List<Long>` (body) | `JsonResult<Boolean>` | 批量删除菜单 |

### 部门管理 `/api/{version}/sys/dept/`

| 方法 | 路径 | 入参 | 返回值 | 说明 |
|---|---|---|---|---|
| GET | `/page` | `SysDeptAdapterListQuery` (query) | `JsonResult<PagingInfo<SysDeptListResponse>>` | 分页查询部门 |
| GET | `/{id}` | `id` (path) | `JsonResult<SysDeptDetailResponse>` | 部门详情 |
| POST | — | `SysDeptRequest` (body) | `JsonResult<Long>` | 创建部门 |
| PUT | `/{id}` | `id` (path) + `SysDeptRequest` (body) | `JsonResult<Boolean>` | 更新部门 |
| DELETE | — | `List<Long>` (body) | `JsonResult<Boolean>` | 批量删除部门 |

### 租户管理 `/api/{version}/sys/tenant/`

| 方法 | 路径 | 入参 | 返回值 | 说明 |
|---|---|---|---|---|
| GET | `/page` | `SysTenantAdapterListQuery` (query) | `JsonResult<PagingInfo<SysTenantListResponse>>` | 分页查询租户 |
| GET | `/{id}` | `id` (path) | `JsonResult<SysTenantDetailResponse>` | 租户详情 |
| POST | — | `SysTenantRequest` (body) | `JsonResult<Long>` | 创建租户 |
| PUT | `/{id}` | `id` (path) + `SysTenantRequest` (body) | `JsonResult<Boolean>` | 更新租户 |
| DELETE | — | `List<Long>` (body) | `JsonResult<Boolean>` | 批量删除租户 |

### 地区管理 `/api/{version}/sys/area/`

| 方法 | 路径 | 入参 | 返回值 | 说明 |
|---|---|---|---|---|
| GET | `/page` | `SysAreaAdapterListQuery` (query) | `JsonResult<PagingInfo<SysAreaListResponse>>` | 分页查询地区 |
| GET | `/tree` | `SysAreaAdapterListQuery` (query) | `JsonResult<List<Tree<Long>>>` | 树形地区数据 |
| GET | `/{id}` | `id` (path) | `JsonResult<SysAreaDetailResponse>` | 地区详情 |
| POST | — | `SysAreaRequest` (body) | `JsonResult<Long>` | 创建地区 |
| PUT | `/{id}` | `id` (path) + `SysAreaRequest` (body) | `JsonResult<Boolean>` | 更新地区 |
| DELETE | — | `List<Long>` (body) | `JsonResult<Boolean>` | 批量删除地区 |

### 日志管理 `/api/{version}/sys/log/`

| 方法 | 路径 | 入参 | 返回值 | 说明 |
|---|---|---|---|---|
| GET | `/page` | `SysLogAdapterListQuery` (query) | `JsonResult<PagingInfo<SysLogListResponse>>` | 分页查询日志 |
| GET | `/{id}` | `id` (path) | `JsonResult<SysLogDetailResponse>` | 日志详情 |
| POST | — | `SysLogRequest` (body) | `JsonResult<Long>` | 创建日志 |
| PUT | `/{id}` | `id` (path) + `SysLogRequest` (body) | `JsonResult<Boolean>` | 更新日志 |
| DELETE | — | `List<Long>` (body) | `JsonResult<Boolean>` | 批量删除日志 |

### 审计日志 `/api/{version}/sys/audit-log/`

| 方法 | 路径 | 入参 | 返回值 | 说明 |
|---|---|---|---|---|
| GET | `/page` | `SysAuditLogAdapterListQuery` (query) | `JsonResult<PagingInfo<SysAuditLogListResponse>>` | 分页查询审计日志 |
| GET | `/{id}` | `id` (path) | `JsonResult<SysAuditLogDetailResponse>` | 审计日志详情 |
| POST | — | `SysAuditLogRequest` (body) | `JsonResult<Long>` | 创建审计日志 |
| PUT | `/{id}` | `id` (path) + `SysAuditLogRequest` (body) | `JsonResult<Boolean>` | 更新审计日志 |
| DELETE | — | `List<Long>` (body) | `JsonResult<Boolean>` | 批量删除审计日志 |

### 文件管理 `/api/{version}/sys/file/`

| 方法 | 路径 | 入参 | 返回值 | 说明 |
|---|---|---|---|---|
| GET | `/page` | `SysFileAdapterListQuery` (query) | `JsonResult<PagingInfo<SysFileListResponse>>` | 分页查询文件 |
| GET | `/{id}` | `id` (path) | `JsonResult<SysFileDetailResponse>` | 文件详情 |
| POST | — | `SysFileRequest` (body) | `JsonResult<Long>` | 创建文件记录 |
| PUT | `/{id}` | `id` (path) + `SysFileRequest` (body) | `JsonResult<Boolean>` | 更新文件记录 |
| DELETE | — | `List<Long>` (body) | `JsonResult<Boolean>` | 批量删除文件 |

### 文件分组 `/api/{version}/sys/FileGroup-group/`

> **注意：** 当前路径存在拼写问题（`FileGroup-group`），预期应为 `file-group`。

| 方法 | 路径 | 入参 | 返回值 | 说明 |
|---|---|---|---|---|
| GET | `/page` | `SysFileGroupAdapterListQuery` (query) | `JsonResult<PagingInfo<SysFileGroupListResponse>>` | 分页查询文件分组 |
| GET | `/{id}` | `id` (path) | `JsonResult<SysFileGroupDetailResponse>` | 分组详情 |
| POST | — | `SysFileGroupRequest` (body) | `JsonResult<Long>` | 创建分组 |
| PUT | `/{id}` | `id` (path) + `SysFileGroupRequest` (body) | `JsonResult<Boolean>` | 更新分组 |
| DELETE | — | `List<Long>` (body) | `JsonResult<Boolean>` | 批量删除分组 |

### 国际化 `/api/{version}/sys/i18n/`

| 方法 | 路径 | 入参 | 返回值 | 说明 |
|---|---|---|---|---|
| GET | `/page` | `SysI18nAdapterListQuery` (query) | `JsonResult<PagingInfo<SysI18nListResponse>>` | 分页查询 |
| GET | `/{id}` | `id` (path) | `JsonResult<SysI18nDetailResponse>` | 详情 |
| POST | — | `SysI18nRequest` (body) | `JsonResult<Long>` | 创建 |
| PUT | `/{id}` | `id` (path) + `SysI18nRequest` (body) | `JsonResult<Boolean>` | 更新 |
| DELETE | — | `List<Long>` (body) | `JsonResult<Boolean>` | 批量删除 |

### 公共参数 `/api/{version}/sys/public-param/`

| 方法 | 路径 | 入参 | 返回值 | 说明 |
|---|---|---|---|---|
| GET | `/page` | `SysPublicParamAdapterListQuery` (query) | `JsonResult<PagingInfo<SysPublicParamListResponse>>` | 分页查询 |
| GET | `/{id}` | `id` (path) | `JsonResult<SysPublicParamDetailResponse>` | 详情 |
| POST | — | `SysPublicParamRequest` (body) | `JsonResult<Long>` | 创建 |
| PUT | `/{id}` | `id` (path) + `SysPublicParamRequest` (body) | `JsonResult<Boolean>` | 更新 |
| DELETE | — | `List<Long>` (body) | `JsonResult<Boolean>` | 批量删除 |

### 路由配置 `/api/{version}/sys/route-conf/`

| 方法 | 路径 | 入参 | 返回值 | 说明 |
|---|---|---|---|---|
| GET | `/page` | `SysRouteConfAdapterListQuery` (query) | `JsonResult<PagingInfo<SysRouteConfListResponse>>` | 分页查询 |
| GET | `/{id}` | `id` (path) | `JsonResult<SysRouteConfDetailResponse>` | 详情 |
| POST | — | `SysRouteConfRequest` (body) | `JsonResult<Long>` | 创建 |
| PUT | `/{id}` | `id` (path) + `SysRouteConfRequest` (body) | `JsonResult<Boolean>` | 更新 |
| DELETE | — | `List<Long>` (body) | `JsonResult<Boolean>` | 批量删除 |

### OAuth 客户端 `/api/{version}/sys/oauth-client-details/`

| 方法 | 路径 | 入参 | 返回值 | 说明 |
|---|---|---|---|---|
| GET | `/page` | `SysOauthClientAdapterListQuery` (query) | `JsonResult<PagingInfo<SysOauthClientListResponse>>` | 分页查询 |
| GET | `/{id}` | `id` (path) | `JsonResult<SysOauthClientDetailResponse>` | 详情 |
| POST | — | `SysOauthClientRequest` (body) | `JsonResult<Long>` | 创建 |
| PUT | `/{id}` | `id` (path) + `SysOauthClientRequest` (body) | `JsonResult<Boolean>` | 更新 |
| DELETE | — | `List<Long>` (body) | `JsonResult<Boolean>` | 批量删除 |

---

## Dubbo RPC 接口

### IRemoteUserService

- **包路径：** `com.kava.kbpd.upms.api.service.IRemoteUserService`
- **Dubbo 版本：** `1.0`
- **实现类：** `RemoteUserService`（`adapter.rpc`）

| 方法签名 | 参数 | 返回值 | 说明 |
|---|---|---|---|
| `findByUsername` | `String tenantId, String username` | `SysUserDTO` | 根据租户+用户名查询用户（⚠️ 桩实现，返回 null） |
| `loginByPwd` | `String name, String pwd` | `SysUserDTO` | 密码登录（⚠️ 桩实现，返回硬编码 id=1） |

### IRemoteOauthClientService

- **包路径：** `com.kava.kbpd.upms.api.service.IRemoteOauthClientService`
- **Dubbo 版本：** `1.0`
- **实现类：** `RemoteOauthClientService`（`adapter.rpc`）

| 方法签名 | 参数 | 返回值 | 说明 |
|---|---|---|---|
| `queryByClientId` | `String clientId` | `SysOauthClientDTO` | 根据 clientId 查询 OAuth 客户端详情 |

---

## DTO 定义

### SysUserDTO

- **包路径：** `com.kava.kbpd.upms.api.model.dto.SysUserDTO`

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | `Long` | 用户 ID |
| `permissions` | `List<String>` | 权限标识列表 |
| `roles` | `List<String>` | 角色编码列表 |

### SysOauthClientDTO

- **包路径：** `com.kava.kbpd.upms.api.model.dto.SysOauthClientDTO`

| 字段 | 类型 | 说明 |
|---|---|---|
| `clientId` | `String` | 客户端 ID |
| `clientSecret` | `String` | 客户端密钥 |
| `resourceIds` | `String` | 资源 ID |
| `scope` | `String` | 授权范围 |
| `authorizedGrantTypes` | `String[]` | 授权类型 |
| `webServerRedirectUri` | `String` | 回调地址 |
| `authorities` | `String` | 权限 |
| `accessTokenValidity` | `Integer` | Access Token 有效期（秒） |
| `refreshTokenValidity` | `Integer` | Refresh Token 有效期（秒） |
| `additionalInformation` | `String` | 附加信息 |
| `autoapprove` | `String` | 自动授权 |
| `tenantId` | `Long` | 所属租户 |
| `userType` | `String` | 用户类型 |

---

## 接口风格说明

- **User / Role 的 PUT** 不带 `/{id}` 路径参数，ID 包含在请求体中
- **其余资源的 PUT** 均使用 `/{id}` 路径参数
- **批量删除** 统一使用 DELETE 方法 + 请求体 `List<Long>` IDs
- **分页查询** 使用 `@ModelAttribute` 绑定查询参数
- **创建 / 更新** 使用 `@RequestBody` 接收 JSON
