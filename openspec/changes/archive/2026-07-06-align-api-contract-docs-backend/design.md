## Context

当前后端代码事实：

- `JsonResult<T>` 字段为 `success/data/errorCode/errorMessage`。
- `PagingInfo<T>` 字段为 `pageNo/pageSize/total/list`。
- UPMS adapter 包含 16 个 REST Controller，包括 App 和 TenantApp。
- `KavaMetaObjectHandler` 已在 common-database 中以 Spring component 形式存在，并通过 auto-configuration 所在模块被引入。
- Member 模块处于 MVP 状态：领域模型、仓储、持久化和 RPC 桩已搭建，应用服务和 HTTP Controller 未实现。

## Goals

- 后端文档与已实现代码状态一致。
- 面向前端的后端接口文档明确当前响应包装和分页结构。
- 后端模块文档站在服务和 DDD 视角，不写前端页面实现细节。

## Non-Goals

- 不改后端业务行为。
- 不补后端测试代码。
- 不修改数据库结构。
- 不替前端维护页面级文档。

## Decisions

### 1. 后端 docs 是契约事实来源

**Decision**: `docs/05-frontend` 继续保留在后端仓库，定位为后端发布给前端的 API 契约。

**Because**: 字段、错误码、分页结构和服务路径由后端实现决定，前端文档应消费这些契约而不是重新定义契约。

### 2. UPMS 状态表按 Controller 和子域更新

**Decision**: UPMS 文档中的资源数量以 adapter Controller 和 API/resource 子域为准，明确 App 与 TenantApp 已实现。

**Because**: 当前代码已经包含 `SysAppController` 和 `SysTenantAppController`，继续写 14 个资源会误导后续联调。

### 3. Common 基础设施状态单独校正

**Decision**: 将 `KavaMetaObjectHandler`、MyBatis-Plus 插件、TenantLine/DataScope/Pagination 状态写入 Common 或 UPMS 文档的基础设施章节。

**Because**: 这些能力跨模块生效，不应被 UPMS 概览错误标记为未实现。

### 4. Member 文档保持 MVP 边界

**Decision**: Member 文档明确已完成 DDD 骨架和持久化 MVP，未实现应用服务、HTTP Controller 和业务行为。

**Because**: 这能避免前端或其他服务误以为 Member 已具备完整业务接口。

## API Contract

| item | backend source |
| --- | --- |
| response wrapper | `kbpd-common-core JsonResult<T>` |
| paging wrapper | `kbpd-common-core PagingInfo<T>` |
| UPMS REST base | `/api/${app.config.api-version}/sys/<resource>` |
| tenant app subscription | `/api/${app.config.api-version}/sys/tenant/{tenantId}/apps` |
| errors | `UpmsBizErrorCodeEnum` and common error handling |

## File Changes

| change_type | files |
| --- | --- |
| modify | `docs/05-frontend/request-guide.md`, `docs/05-frontend/upms-api.md` |
| modify | `docs/06-modules/kbpd-upms/overview.md`, `docs/06-modules/kbpd-upms/business-rules.md` |
| modify | `docs/06-modules/kbpd-common/overview.md` |
| modify | `docs/06-modules/kbpd-member/overview.md` |
| modify | `docs/00-project-map.md` if document titles/descriptions change |
| modify | backend OpenSpec specs for API/documentation alignment if needed |

## Dependencies

- Parent change: `../openspec/changes/align-api-contract-docs`.
- Admin child change consumes the backend contract after this child change confirms final wording.

## Risks

| risk | mitigation |
| --- | --- |
| Docs may state behavior not covered by tests. | Mark unverified behavior as "已实现但需验证" or "DEFERRED" instead of overclaiming. |
| Resource count can be ambiguous if TenantApp is relationship not CRUD resource. | Document both Controller count and business resource count explicitly. |
| ID serialization wording can drift from runtime behavior. | Verify Jackson configuration or current API examples before changing ID precision guidance. |
