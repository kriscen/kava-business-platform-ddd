## Why

UPMS 模块已完成 App（应用管理）和 TenantApp（租户应用订阅）两个子域的实现，包含 10 个 HTTP 端点和完整的 DTO 定义，但前端对接文档 `docs/05-frontend/upms-api.md` 中缺少这两个资源的接口说明。前端开发者无法获取完整的 API 参考来对接。

## What Changes

- 在 `docs/05-frontend/upms-api.md` 中补充**应用管理**（SysApp）的 7 个 HTTP REST 端点定义
- 补充 **SysAppRequest**、**SysAppListResponse**、**SysAppDetailResponse**、**SysAppDropdownResponse** 的字段定义
- 在同文件中补充**租户应用订阅**（SysTenantApp）的 3 个 HTTP REST 端点定义
- 补充 **SysTenantAppRequest**、**SysTenantAppListResponse** 的字段定义
- 补充 App 和 TenantApp 相关的查询参数说明

## Capabilities

### New Capabilities

（无新增能力规格，此次变更为纯文档补充）

### Modified Capabilities

- `app-management`: 补充前端对接文档中的 HTTP REST 接口和 DTO 字段定义
- `tenant-app-subscription`: 补充前端对接文档中的 HTTP REST 接口和 DTO 字段定义

## Impact

- `docs/05-frontend/upms-api.md` — 补充应用管理和租户应用订阅两个资源的接口文档
