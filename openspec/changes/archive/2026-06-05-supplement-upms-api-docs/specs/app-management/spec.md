## MODIFIED Requirements

### Requirement: App CRUD 管理

系统 SHALL 提供平台管理员管理 App 的完整 CRUD 接口。SysAppEntity MUST 使用 `SysAppId` 值对象作为 ID 类型。前端对接文档 SHALL 包含以下 HTTP REST 端点定义：

- `GET /api/v1/sys/app/page` — 分页查询应用（参数：`appName`、`pageNo`、`pageSize`），返回 `JsonResult<PagingInfo<SysAppListResponse>>`
- `GET /api/v1/sys/app/{id}` — 应用详情，返回 `JsonResult<SysAppDetailResponse>`
- `POST /api/v1/sys/app/` — 创建应用，请求体 `SysAppRequest`，返回 `JsonResult<Long>`
- `PUT /api/v1/sys/app/{id}` — 更新应用，路径 `id` + 请求体 `SysAppRequest`，返回 `JsonResult<Void>`
- `DELETE /api/v1/sys/app/` — 批量删除应用，请求体 `List<Long>`，返回 `JsonResult<Void>`
- `GET /api/v1/sys/app/dropdown` — 应用下拉列表，返回 `JsonResult<List<SysAppDropdownResponse>>`
- `PUT /api/v1/sys/app/{id}/menus` — 绑定应用菜单，路径 `id` + 请求体 `List<Long>`，返回 `JsonResult<Void>`

#### Scenario: 前端查阅 App 分页查询文档
- **WHEN** 前端开发者阅读 upms-api.md 的应用管理章节
- **THEN** 可看到 GET /page 端点的路径、请求参数（appName、pageNo、pageSize）和响应类型

#### Scenario: 前端查阅 App 创建接口文档
- **WHEN** 前端开发者阅读应用管理的创建接口
- **THEN** 可看到 POST 端点的 SysAppRequest 字段（code、name、icon、description、menuIds）和返回类型

### Requirement: App Dropdown 查询

系统 SHALL 提供非分页的 App 下拉列表接口，用于前端表单选择。前端对接文档 SHALL 包含该端点的返回字段定义（id、code、name）。

#### Scenario: 前端查阅 App 下拉接口文档
- **WHEN** 前端开发者阅读应用管理的下拉接口
- **THEN** 可看到 SysAppDropdownResponse 包含 id、code、name 三个字段
