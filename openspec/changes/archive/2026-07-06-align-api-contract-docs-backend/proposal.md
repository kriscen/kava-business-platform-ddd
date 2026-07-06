## Why

后端代码已经实现 UPMS 16 个 REST Controller、`JsonResult` 统一响应、`PagingInfo` 分页结构、`KavaMetaObjectHandler` 自动填充等能力，但部分后端模块文档仍保留旧状态，例如 UPMS 文档多处写 14 个资源、MetaObjectHandler 未实现。后端文档需要作为接口契约和服务实现事实来源，避免前端按旧协议继续实现。

Parent change: `../openspec/changes/align-api-contract-docs`

## What Changes

- 校正 UPMS 模块文档中的资源数量、Controller 数量、租户应用订阅和应用管理状态。
- 校正 Common 数据库基础设施文档中 `KavaMetaObjectHandler`、MyBatis-Plus 插件和字段填充状态。
- 校正 Member 文档中已实现 MVP 与未实现业务能力的边界。
- 明确 `docs/05-frontend` 是后端发布给前端的接口契约文档，字段结构以后端代码为准。
- 更新后端 OpenSpec 或文档地图，使后端文档站在后端子项目角度描述服务、DDD、接口、权限、持久化和运行状态。

## Capabilities

### New Capabilities

- `backend-docs-alignment`: 建立后端模块文档与已实现代码状态的一致性要求。

### Modified Capabilities

- `api-patterns`: 明确 `JsonResult` 与 `PagingInfo` 是前后端共享接口契约。
- `tenant-app-subscription`: 确认租户应用订阅属于 UPMS 已实现 REST 能力。
- `member-domain`: 保持 Member MVP 已实现与未实现业务行为的边界清晰。

## Impact

- `docs/00-project-map.md`
- `docs/05-frontend/request-guide.md`
- `docs/05-frontend/upms-api.md`
- `docs/06-modules/kbpd-upms/overview.md`
- `docs/06-modules/kbpd-upms/business-rules.md`
- `docs/06-modules/kbpd-common/overview.md`
- `docs/06-modules/kbpd-member/overview.md`
- `openspec/specs/*` 中与接口契约或文档状态有关的 specs

## Non-Goals

- 不修改 Java 业务代码、Controller、Repository、Mapper 或 SQL。
- 不新增后端接口、错误码或领域规则。
- 不改变 `JsonResult`、`PagingInfo`、租户隔离、权限运行时行为。
- 不维护前端页面文档细节。
