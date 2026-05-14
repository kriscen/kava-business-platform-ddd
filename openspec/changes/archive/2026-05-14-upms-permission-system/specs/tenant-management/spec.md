## ADDED Requirements

### Requirement: 租户 CRUD 管理
系统 SHALL 提供平台管理员管理租户的完整 CRUD 接口（创建、查询、更新、删除）。

#### Scenario: 创建租户
- **WHEN** 平台管理员提交租户创建请求，包含 name、code、tenantDomain 等
- **THEN** 系统创建租户记录
- **AND** 返回新创建的租户 ID

#### Scenario: 查询租户列表
- **WHEN** 平台管理员查询租户列表（支持分页）
- **THEN** 返回所有租户的列表信息

#### Scenario: 更新租户信息
- **WHEN** 平台管理员更新租户的 name、websiteName 等字段
- **THEN** 系统更新租户记录

#### Scenario: 删除租户
- **WHEN** 平台管理员删除租户
- **THEN** 系统软删除租户记录（设置 del_flag）
- **AND** 租户下用户无法再登录

### Requirement: 租户菜单分配
平台管理员 SHALL 能为租户分配可用的 SYSTEM_TENANT 菜单范围。

#### Scenario: 为租户分配菜单
- **WHEN** 平台管理员为租户分配 SYSTEM_TENANT 菜单 ID 列表
- **THEN** 系统更新 sys_tenant.menu_id 字段
- **AND** 租户管理员此后可看到并使用这些菜单

#### Scenario: 缩小租户菜单范围
- **WHEN** 平台管理员减少租户的菜单分配
- **THEN** 已分配但不在新范围内的菜单变为不可见
- **AND** 租户下引用了被移除菜单的角色需要清理关联（可标记异常或由管理员手动处理）

#### Scenario: 分配非 SYSTEM_TENANT 菜单
- **WHEN** 平台管理员尝试为租户分配 scope 为 SYSTEM 的菜单
- **THEN** 系统拒绝操作并返回错误

### Requirement: 新租户自动初始化必备角色
创建租户时，系统 SHALL 自动创建一个"租户管理员"角色，关联该租户所有已分配菜单的权限。

#### Scenario: 创建租户时自动创建管理员角色
- **WHEN** 平台管理员创建租户并分配了菜单
- **THEN** 系统自动创建角色 code 为 `tenant_admin` 的角色
- **AND** 该角色关联租户所有已分配的 SYSTEM_TENANT 菜单
- **AND** 该角色的 dsType 为 ALL（租户全数据可见）

### Requirement: 租户管理员初始用户绑定
创建租户时，系统 SHALL 支持指定一个初始管理员用户，并将其绑定到自动创建的租户管理员角色。

#### Scenario: 创建租户时指定管理员
- **WHEN** 平台管理员创建租户，并指定了初始管理员的 username 和 password
- **THEN** 系统创建该用户记录
- **AND** 将用户绑定到租户管理员角色
- **AND** 该用户可立即登录并管理租户

#### Scenario: 创建租户时未指定管理员
- **WHEN** 平台管理员创建租户，未指定初始管理员
- **THEN** 系统仅创建租户和管理员角色，不创建用户
- **AND** 管理员角色暂时无用户绑定，后续可手动分配
