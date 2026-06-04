## MODIFIED Requirements

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
- **THEN** 每条记录包含 `roleIds` 字段

### Requirement: 分组响应补充父级名称

分组详情和列表响应 SHALL 包含 `parentName` 字段，用于前端展示分组层级路径。

#### Scenario: 分组详情返回父级名称
- **WHEN** 前端调用 `GET /api/v1/sys/group/{id}` 且该分组有父级
- **THEN** 响应包含 `parentName` 字段，值为父级分组名称

#### Scenario: 分组详情为顶级分组
- **WHEN** 前端调用 `GET /api/v1/sys/group/{id}` 且该分组 pid 为 null
- **THEN** `parentName` 为 null
