## ADDED Requirements

### Requirement: 创建用户时绑定角色
创建用户时，系统 SHALL 支持同时指定该用户关联的角色 ID 列表，并正确写入 `sys_user_role` 中间表。

#### Scenario: 创建用户并绑定角色成功
- **WHEN** 管理员提交用户创建请求，包含 username、password、roleIds 列表
- **THEN** 系统创建用户记录，并将所有 roleIds 写入 sys_user_role 表
- **AND** 返回新创建的用户 ID

#### Scenario: 创建用户时未指定角色
- **WHEN** 管理员提交用户创建请求，roleIds 为空列表
- **THEN** 系统创建用户记录，不写入 sys_user_role 表
- **AND** 返回新创建的用户 ID

#### Scenario: 创建用户时角色 ID 包含不属于当前租户的角色
- **WHEN** 管理员提交用户创建请求，roleIds 包含其他租户的角色
- **THEN** 系统拒绝创建并返回权限错误

### Requirement: 更新用户时重新绑定角色
更新用户时，系统 SHALL 支持修改用户的角色关联，采用全量替换策略（先删后插）。

#### Scenario: 更新用户角色成功
- **WHEN** 管理员更新用户，提交新的 roleIds 列表
- **THEN** 系统删除该用户在 sys_user_role 中的所有旧记录，插入新的关联记录
- **AND** 返回更新成功

#### Scenario: 更新用户时清空角色绑定
- **WHEN** 管理员更新用户，roleIds 为空列表
- **THEN** 系统删除该用户在 sys_user_role 中的所有记录

### Requirement: 删除用户时清理角色关联
删除用户时，系统 SHALL 自动清理 sys_user_role 中该用户的所有关联记录。

#### Scenario: 批量删除用户
- **WHEN** 管理员批量删除用户
- **THEN** 系统删除用户记录，同时清理 sys_user_role 中所有关联记录

### Requirement: 查询用户详情时返回关联角色 ID 列表
查询用户详情时，系统 SHALL 返回该用户关联的 roleIds 列表。

#### Scenario: 查询用户详情
- **WHEN** 管理员查询用户详情
- **THEN** 返回用户基本信息及关联的 roleIds 列表
