## MODIFIED Requirements

### Requirement: 创建角色时绑定菜单
创建角色时，系统 SHALL 支持同时指定该角色关联的菜单 ID 列表，并正确写入 `sys_role_menu` 中间表。menuIds 允许为空。

#### Scenario: 创建角色并绑定菜单成功
- **WHEN** 管理员提交角色创建请求，包含 roleName、roleCode、menuIds 列表
- **THEN** 系统创建角色记录，并将所有 menuIds 写入 sys_role_menu 表
- **AND** 返回新创建的角色 ID

#### Scenario: 创建角色时未指定菜单
- **WHEN** 管理员提交角色创建请求，menuIds 为空列表或 null
- **THEN** 系统创建角色记录，不写入 sys_role_menu 表
- **AND** 返回新创建的角色 ID

#### Scenario: 创建角色时菜单 ID 包含不属于当前租户的菜单
- **WHEN** 管理员提交角色创建请求，menuIds 包含其他租户的自有菜单
- **THEN** 系统拒绝创建并返回权限错误
