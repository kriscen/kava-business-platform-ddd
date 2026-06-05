## MODIFIED Requirements

### Requirement: 租户购买 App

系统 SHALL 支持为租户订阅 App，通过写入 `sys_tenant_app` 关系记录实现。前端对接文档 SHALL 包含 `POST /api/v1/sys/tenant-app/subscribe` 端点定义，请求体为 `SysTenantAppRequest`（tenantId、appId），返回 `JsonResult<Void>`。

#### Scenario: 前端查阅租户订阅 App 接口文档
- **WHEN** 前端开发者阅读租户应用订阅章节
- **THEN** 可看到 POST /subscribe 端点的路径、请求体字段（tenantId、appId）和响应类型

### Requirement: 租户退订 App

系统 SHALL 支持租户退订 App。前端对接文档 SHALL 包含 `POST /api/v1/sys/tenant-app/unsubscribe` 端点定义，请求体为 `SysTenantAppRequest`（tenantId、appId），返回 `JsonResult<Void>`。

#### Scenario: 前端查阅租户退订 App 接口文档
- **WHEN** 前端开发者阅读退订接口
- **THEN** 可看到 POST /unsubscribe 端点的路径和请求体字段

### Requirement: 查询租户已购 App 列表

系统 SHALL 支持查询指定租户已订阅的 App 列表。前端对接文档 SHALL 包含 `GET /api/v1/sys/tenant-app/tenant/{tenantId}` 端点定义，返回 `JsonResult<List<SysTenantAppListResponse>>`，响应字段包含 id、tenantId、appId、appCode、appName、appIcon、status、gmtCreate。

#### Scenario: 前端查阅租户已购 App 列表接口文档
- **WHEN** 前端开发者阅读租户应用列表接口
- **THEN** 可看到 GET /tenant/{tenantId} 端点和 SysTenantAppListResponse 的完整字段定义
