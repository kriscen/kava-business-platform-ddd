# Capability: backend-docs-alignment

## Purpose

约束后端文档以当前代码实现为事实来源，明确服务边界、接口契约、DDD 分层、持久化、权限和 deferred 项状态，并将 `docs/05-frontend` 作为后端发布给前端的 API 契约文档。

## Requirements

### Requirement: 后端文档与实现状态一致

后端模块文档 SHALL 以当前代码实现为事实来源，准确描述服务边界、接口契约、DDD 分层、持久化、权限和 deferred 项状态。

#### Scenario: UPMS 资源状态校正

- **WHEN** UPMS adapter 中存在 App 和 TenantApp 相关 Controller
- **THEN** UPMS 文档 SHALL 明确这些端点属于当前已实现能力
- **AND** SHALL 避免继续只以过期数量描述整个 UPMS REST 能力

#### Scenario: Common 基础设施状态校正

- **WHEN** common-database 中存在 `KavaMetaObjectHandler`
- **THEN** 后端文档 SHALL 将字段自动填充描述为已实现或已实现待验证
- **AND** SHALL NOT 声明没有对应 Handler

#### Scenario: Member MVP 边界校正

- **WHEN** Member 模块只有领域模型、仓储、持久化和 RPC 桩
- **THEN** Member 文档 SHALL 明确业务行为、应用服务和 HTTP Controller 尚未实现
- **AND** SHALL NOT 暗示可提供完整会员管理 API

### Requirement: 后端接口契约文档作为前端对接事实来源

后端 `docs/05-frontend` SHALL 作为后端发布给前端的 API 契约文档，描述当前后端实际路径、请求体、响应包装、分页结构和错误字段。

#### Scenario: 响应包装契约

- **WHEN** 文档描述普通业务接口响应
- **THEN** SHALL 使用 `success/data/errorCode/errorMessage`
- **AND** SHALL NOT 使用 `code/msg` 表示普通业务接口统一响应

#### Scenario: 分页契约

- **WHEN** 文档描述分页接口响应
- **THEN** SHALL 使用 `list/total/pageNo/pageSize`
- **AND** SHALL NOT 使用 `records/current/size/pages`
