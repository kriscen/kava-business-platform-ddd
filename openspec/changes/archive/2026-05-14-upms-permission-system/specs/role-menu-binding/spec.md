## ADDED Requirements

### Requirement: 创建角色时绑定菜单
创建角色时，系统 SHALL 支持同时指定该角色关联的菜单 ID 列表，并正确写入 `sys_role_menu` 中间表。

#### Scenario: 创建角色并绑定菜单成功
- **WHEN** 管理员提交角色创建请求，包含 roleName、roleCode、menuIds 列表
- **THEN** 系统创建角色记录，并将所有 menuIds 写入 sys_role_menu 表
- **AND** 返回新创建的角色 ID

#### Scenario: 创建角色时未指定菜单
- **WHEN** 管理员提交角色创建请求，menuIds 为空列表
- **THEN** 系统创建角色记录，不写入 sys_role_menu 表
- **AND** 返回新创建的角色 ID

#### Scenario: 创建角色时菜单 ID 包含不属于当前租户的菜单
- **WHEN** 管理员提交角色创建请求，menuIds 包含其他租户的自有菜单
- **THEN** 系统拒绝创建并返回权限错误

### Requirement: 更新角色时重新绑定菜单
更新角色时，系统 SHALL 支持修改角色的菜单关联，采用全量替换策略（先删后插）。

#### Scenario: 更新角色菜单成功
- **WHEN** 管理员更新角色，提交新的 menuIds 列表
- **THEN** 系统删除该角色在 sys_role_menu 中的所有旧记录，插入新的关联记录
- **AND** 返回更新成功

#### Scenario: 更新角色时清空菜单绑定
- **WHEN** 管理员更新角色，menuIds 为空列表
- **THEN** 系统删除该角色在 sys_role_menu 中的所有记录
- **AND** 角色变为无菜单关联状态

### Requirement: 删除角色时清理菜单关联
删除角色时，系统 SHALL 自动清理 sys_role_menu 中该角色的所有关联记录。

#### Scenario: 批量删除角色
- **WHEN** 管理员批量删除角色
- **THEN** 系统删除角色记录，同时清理 sys_role_menu 和 sys_user_role 中所有关联记录

### Requirement: 查询角色详情时返回关联菜单 ID 列表
查询角色详情时，系统 SHALL 返回该角色关联的 menuIds 列表。

#### Scenario: 查询角色详情
- **WHEN** 管理员查询角色详情
- **THEN** 返回角色基本信息及关联的 menuIds 列表
