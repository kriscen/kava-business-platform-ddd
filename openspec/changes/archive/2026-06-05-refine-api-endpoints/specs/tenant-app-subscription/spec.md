## MODIFIED Requirements

### Requirement: 租户购买 App

系统 SHALL 支持为租户订阅 App，通过写入 `sys_tenant_app` 关系记录实现。订阅后租户自动获得该 App 包含的菜单能力。前端对接文档 SHALL 包含 `POST /api/v1/sys/tenant/{tenantId}/apps` 端点定义，请求体为 `SysTenantAppRequest`（appId），返回 `JsonResult<Void>`。

#### Scenario: 前端查阅租户订阅 App 接口文档
- **WHEN** 前端开发者阅读租户应用订阅章节
- **THEN** 可看到 POST /sys/tenant/{tenantId}/apps 端点的路径、请求体字段（appId）和响应类型

#### Scenario: 为租户订阅 App
- **WHEN** 平台管理员为租户订阅某个 App
- **THEN** 系统在 sys_tenant_app 中创建记录（tenant_id, app_id, status=ACTIVE）
- **AND** 该租户的菜单查询自动包含该 App 的菜单

#### Scenario: 重复订阅同一 App
- **WHEN** 平台管理员为租户订阅已订阅的 App
- **THEN** 系统拒绝操作并返回已订阅错误

#### Scenario: 订阅不存在的 App
- **WHEN** 平台管理员为租户订阅不存在的 App
- **THEN** 系统拒绝操作并返回 App 不存在错误

#### Scenario: 订阅已停用的 App
- **WHEN** 平台管理员为租户订阅状态为 DISABLED 的 App
- **THEN** 系统拒绝操作并返回 App 不可用错误

### Requirement: 租户退订 App

系统 SHALL 支持租户退订 App，退订后租户失去该 App 的菜单能力。前端对接文档 SHALL 包含 `DELETE /api/v1/sys/tenant/{tenantId}/apps/{appId}` 端点定义，返回 `JsonResult<Void>`。

#### Scenario: 前端查阅租户退订 App 接口文档
- **WHEN** 前端开发者阅读退订接口
- **THEN** 可看到 DELETE /sys/tenant/{tenantId}/apps/{appId} 端点的路径

#### Scenario: 退订 App
- **WHEN** 平台管理员将租户的某个 App 退订
- **THEN** 系统将 sys_tenant_app 中该记录状态变更为 EXPIRED
- **AND** 该租户的菜单查询自动排除该 App 的菜单
- **AND** 已分配给角色的菜单关联可保留，重新订阅后自动恢复

#### Scenario: 退订 kava-base
- **WHEN** 平台管理员尝试退订租户的 kava-base
- **THEN** 系统拒绝操作并返回不可退订错误

### Requirement: 查询租户已购 App 列表

系统 SHALL 支持查询指定租户已订阅的 App 列表。前端对接文档 SHALL 包含 `GET /api/v1/sys/tenant/{tenantId}/apps` 端点定义，返回 `JsonResult<List<SysTenantAppListResponse>>`，响应字段包含 id、tenantId、appId、appCode、appName、appIcon、status、gmtCreate。

#### Scenario: 前端查阅租户已购 App 列表接口文档
- **WHEN** 前端开发者阅读租户应用列表接口
- **THEN** 可看到 GET /sys/tenant/{tenantId}/apps 端点和 SysTenantAppListResponse 的完整字段定义

#### Scenario: 查询租户已购 App
- **WHEN** 管理员查询某租户的已购 App 列表
- **THEN** 返回该租户所有 sys_tenant_app 中 status=ACTIVE 的 App 信息
- **AND** 包含 App 的 code、name、icon、订阅状态、订阅时间

#### Scenario: 租户无已购 App
- **WHEN** 查询租户的已购 App 列表，该租户仅有 kava-base
- **THEN** 返回仅包含 kava-base 的列表
