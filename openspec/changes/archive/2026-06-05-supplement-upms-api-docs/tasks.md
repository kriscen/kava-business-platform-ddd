## 1. 补充应用管理（App）接口文档

- [x] 1.1 在 `docs/05-frontend/upms-api.md` 的「OAuth 客户端」章节之后追加「应用管理 `/api/v1/sys/app/`」端点表格，包含 7 个端点（page/detail/create/update/delete/dropdown/updateAppMenus）
- [x] 1.2 在「请求体字段」章节追加 `SysAppRequest` 字段定义表（id、code、name、icon、description、menuIds）
- [x] 1.3 在「响应体字段」章节追加 `SysAppListResponse`、`SysAppDetailResponse`、`SysAppDropdownResponse` 字段定义表
- [x] 1.4 在「查询参数」章节补充 App 资源查询支持的过滤参数（appName）

## 2. 补充租户应用订阅（TenantApp）接口文档

- [x] 2.1 在应用管理章节之后追加「租户应用订阅 `/api/v1/sys/tenant-app/`」端点表格，包含 3 个端点（subscribe/unsubscribe/getByTenantId）
- [x] 2.2 在「请求体字段」章节追加 `SysTenantAppRequest` 字段定义表（tenantId、appId）
- [x] 2.3 在「响应体字段」章节追加 `SysTenantAppListResponse` 字段定义表

## 3. 验证

- [x] 3.1 对照 SysAppController.java 和 SysTenantAppController.java 逐端点验证文档的路径、HTTP 方法、参数和返回值一致
- [x] 3.2 验证文档格式与现有章节风格一致（表格列、字段说明方式、Markdown 语法）
