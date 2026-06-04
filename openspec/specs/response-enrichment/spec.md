# Capability: response-enrichment

## Purpose

在 CRUD 响应对象中补充关联实体的名称字段，使前端能直接展示关联信息，无需 N+1 客户端查询。

## Requirements

### Requirement: 用户响应补充关联名称
用户详情和列表响应 SHALL 包含以下关联名称字段：`groupName`（分组名称）、`tenantName`（租户名称）。用户详情响应 SHALL 额外包含 `roleNames`（角色名称列表）。

#### Scenario: 用户详情返回关联名称
- **WHEN** 前端调用 `GET /api/v1/sys/user/{id}`
- **THEN** 响应包含 `groupName`、`tenantName`、`roleNames` 字段
- **AND** 当关联 ID 存在但对应记录已删除时，名称字段返回 null

#### Scenario: 用户列表返回分组名称和租户名称
- **WHEN** 前端调用 `GET /api/v1/sys/user/page`
- **THEN** 每条记录包含 `groupName`、`tenantName` 字段
- **AND** 不包含 `roleNames`（列表场景不展示角色详情）

#### Scenario: 用户列表响应包含 roleIds
- **WHEN** 前端调用 `GET /api/v1/sys/user/page`
- **THEN** 每条记录包含 `roleIds` 字段（当前缺失，需补上）

### Requirement: 角色详情响应补充菜单名称
角色详情响应 SHALL 在返回 menuIds 的同时，补充 `menuNames` 字段（菜单名称列表），用于前端展示角色已分配的权限。

#### Scenario: 角色详情返回菜单名称
- **WHEN** 前端调用 `GET /api/v1/sys/role/{id}`
- **THEN** 响应包含 `menuNames` 字段，与 `menuIds` 一一对应
- **AND** 当 menuIds 为空时 menuNames 为空列表

### Requirement: 分组响应补充父级名称
分组详情和列表响应 SHALL 包含 `parentName` 字段，用于前端展示分组层级路径。

#### Scenario: 分组详情返回父级名称
- **WHEN** 前端调用 `GET /api/v1/sys/group/{id}` 且该分组有父级
- **THEN** 响应包含 `parentName` 字段，值为父级分组名称

#### Scenario: 分组详情为顶级分组
- **WHEN** 前端调用 `GET /api/v1/sys/group/{id}` 且该分组 pid 为 null
- **THEN** `parentName` 为 null

### Requirement: 菜单响应补充父级名称
菜单详情和列表响应 SHALL 包含 `parentName` 字段，用于前端展示菜单层级路径。

#### Scenario: 菜单详情返回父级名称
- **WHEN** 前端调用 `GET /api/v1/sys/menu/{id}` 且该菜单有父级
- **THEN** 响应包含 `parentName` 字段，值为父级菜单名称

#### Scenario: 菜单详情为顶级菜单
- **WHEN** 前端调用 `GET /api/v1/sys/menu/{id}` 且该菜单 pid 为 null
- **THEN** `parentName` 为 null
