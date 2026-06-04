## Purpose

定义角色与菜单的绑定管理：创建/更新角色时关联菜单，支持中间表的写入和删除。

## Requirements

### Requirement: 创建角色时绑定菜单
创建角色时，系统 SHALL 支持同时指定该角色关联的菜单 ID 列表，并正确写入 `sys_role_menu` 中间表。menuIds 允许为空。租户角色绑定的菜单 MUST 属于该租户已购 App 范围内的 TENANT 级菜单。

#### Scenario: 创建角色并绑定菜单成功
- **WHEN** 管理员提交角色创建请求，包含 roleName、roleCode、menuIds 列表
- **THEN** 系统创建角色记录，并将所有 menuIds 写入 sys_role_menu 表
- **AND** 返回新创建的角色 ID

#### Scenario: 创建角色时未指定菜单
- **WHEN** 管理员提交角色创建请求，menuIds 为空列表或 null
- **THEN** 系统创建角色记录，不写入 sys_role_menu 表
- **AND** 返回新创建的角色 ID

#### Scenario: 租户管理员绑定已购 App 范围内的菜单
- **WHEN** 租户管理员创建角色，menuIds 中的菜单均属于 (kava-base ∪ 已购App) 通过 sys_app_menu 关联的 TENANT 级菜单
- **THEN** 系统接受操作并创建角色

#### Scenario: 租户管理员绑定未购 App 的菜单
- **WHEN** 租户管理员创建角色，menuIds 中包含未购买 App 的 TENANT 级菜单
- **THEN** 系统拒绝操作并返回菜单超出可分配范围的错误

#### Scenario: 平台管理员绑定 PLATFORM 级菜单
- **WHEN** 平台管理员创建角色，menuIds 中仅包含 level 为 PLATFORM 的菜单
- **THEN** 系统接受操作并创建角色

### Requirement: 更新角色时重新绑定菜单
更新角色时，系统 SHALL 支持修改角色的菜单关联，采用全量替换策略（先删后插）。租户角色菜单范围校验与创建时一致。

#### Scenario: 更新角色菜单成功
- **WHEN** 管理员更新角色，提交新的 menuIds 列表
- **THEN** 系统删除该角色在 sys_role_menu 中的所有旧记录，插入新的关联记录
- **AND** 返回更新成功

#### Scenario: 更新角色时清空菜单绑定
- **WHEN** 管理员更新角色，menuIds 为空列表
- **THEN** 系统删除该角色在 sys_role_menu 中的所有记录
- **AND** 角色变为无菜单关联状态

#### Scenario: 租户管理员更新角色时绑定超出范围的菜单
- **WHEN** 租户管理员更新角色，menuIds 中包含未购 App 的 TENANT 级菜单
- **THEN** 系统拒绝操作并返回菜单超出可分配范围的错误

### Requirement: 删除角色时清理菜单关联
删除角色时，系统 SHALL 自动清理 sys_role_menu 中该角色的所有关联记录。

#### Scenario: 批量删除角色
- **WHEN** 管理员批量删除角色
- **THEN** 系统删除角色记录，同时清理 sys_role_menu 和 sys_user_role 中所有关联记录

### Requirement: 查询角色详情时返回关联菜单 ID 列表
查询角色详情时，系统 SHALL 返回该角色关联的 menuIds 列表及 menuNames 列表。

#### Scenario: 查询角色详情
- **WHEN** 管理员查询角色详情
- **THEN** 返回角色基本信息及关联的 menuIds 列表
- **AND** 返回 menuNames 列表，与 menuIds 一一对应
