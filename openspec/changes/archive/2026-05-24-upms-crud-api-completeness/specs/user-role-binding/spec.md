## MODIFIED Requirements

### Requirement: 查询用户详情时返回关联角色 ID 列表
查询用户详情时，系统 SHALL 返回该用户关联的 roleIds 列表及 roleNames 列表。用户列表响应 SHALL 同时包含 roleIds 列表（当前缺失）。

#### Scenario: 查询用户详情
- **WHEN** 管理员查询用户详情
- **THEN** 返回用户基本信息及关联的 roleIds 列表
- **AND** 返回 roleNames 列表，与 roleIds 一一对应

### Requirement: 用户列表响应包含关联名称
用户列表每条记录 SHALL 包含 deptName、tenantName 字段，用于前端列表直接展示。

#### Scenario: 用户列表返回关联名称
- **WHEN** 管理员查询用户列表
- **THEN** 每条记录包含 deptName、tenantName
- **AND** 每条记录包含 roleIds 列表
