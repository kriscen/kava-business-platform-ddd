### Requirement: ListQuery 值对象包含实际过滤字段
ListQuery 值对象 MUST 包含对应实体的实际过滤条件字段，MUST NOT 仅有分页参数。

#### Scenario: SysUserListQuery 包含用户过滤字段
- **WHEN** 查询用户列表
- **THEN** SysUserListQuery MUST 包含以下可选过滤字段：username（String）、phone（String）、lockFlag（Integer）、deptId（Long）
- **AND** 所有过滤字段为可选（nullable）

#### Scenario: SysRoleListQuery 包含角色过滤字段
- **WHEN** 查询角色列表
- **THEN** SysRoleListQuery MUST 包含以下可选过滤字段：roleName（String）、roleCode（String）

#### Scenario: SysDeptListQuery 包含部门过滤字段
- **WHEN** 查询部门列表
- **THEN** SysDeptListQuery MUST 包含以下可选过滤字段：deptName（String）

#### Scenario: SysMenuListQuery 包含菜单过滤字段
- **WHEN** 查询菜单列表
- **THEN** SysMenuListQuery MUST 包含以下可选过滤字段：menuName（String）、type（Integer）、scope（String）

#### Scenario: SysTenantListQuery 包含租户过滤字段
- **WHEN** 查询租户列表
- **THEN** SysTenantListQuery MUST 包含以下可选过滤字段：tenantName（String）

### Requirement: Repository 实现使用 ListQuery 过滤字段
Repository 实现 MUST 使用 ListQuery 中的过滤字段构建查询条件。

#### Scenario: 用户列表查询按条件过滤
- **WHEN** SysUserListQuery 中 username 不为空
- **THEN** Repository 实现 MUST 在 SQL 查询中添加 username LIKE 条件
- **WHEN** SysUserListQuery 中 lockFlag 不为空
- **THEN** Repository 实现 MUST 在 SQL 查询中添加 lockFlag = 条件

#### Scenario: 角色列表查询按条件过滤
- **WHEN** SysRoleListQuery 中 roleName 不为空
- **THEN** Repository 实现 MUST 在 SQL 查询中添加 roleName LIKE 条件
