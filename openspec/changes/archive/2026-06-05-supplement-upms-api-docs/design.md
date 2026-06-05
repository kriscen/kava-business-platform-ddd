## Context

UPMS 模块已实现 App（应用管理）和 TenantApp（租户应用订阅）两个子域，代码层包含完整的 Controller、DTO、Application Service 和 Domain 逻辑。但前端对接文档 `docs/05-frontend/upms-api.md` 仅覆盖了 14 个早期资源，缺少这两个新增资源的接口说明。

当前文档风格已建立统一模式：每个资源一个表格列出端点、独立的 Request/Response 字段表、查询参数表。

## Goals / Non-Goals

**Goals:**

- 在 `docs/05-frontend/upms-api.md` 中补充 App 和 TenantApp 两个资源的完整接口文档
- 保持与现有文档风格完全一致（表格格式、字段命名、说明方式）
- 覆盖全部 10 个端点和 8 个 DTO 的字段定义

**Non-Goals:**

- 不修改 Auth 模块的 API 文档（已完整）
- 不修改 `docs/06-modules/kbpd-upms/api.md`（Dubbo RPC 文档，独立维护）
- 不涉及代码变更
- 不修改已有资源的文档内容

## Decisions

### D1：追加到现有文件的 OAuth 客户端章节之后

Because 文档按资源维度组织，新增资源直接追加在文档末尾（OAuth 客户端之后）即可保持一致性。无需引入新的分节逻辑。

### D2：SysApp 的 page 查询参数采用 @RequestParam 而非 AdapterListQuery

Because `SysAppController.getPage` 使用 `@RequestParam String appName` 而非 `SysAppAdapterListQuery`，与其他资源的查询参数风格不同。文档中需如实反映这一点：查询参数为 `appName`、`pageNo`、`pageSize`，而非统一的 AdapterListQuery。

### D3：TenantApp 端点路径为非标准 REST 风格

Because `SysTenantAppController` 使用 `/subscribe`、`/unsubscribe`、`/tenant/{tenantId}` 而非标准 CRUD 路径，文档中需如实记录路径格式。

## Risks / Trade-offs

- 无技术风险，纯文档变更
