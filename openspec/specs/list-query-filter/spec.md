### Requirement: ListQuery 值对象包含实际过滤字段
ListQuery 值对象 MUST 包含对应实体的实际过滤条件字段，MUST NOT 仅有分页参数。

#### Scenario: SysUserListQuery 包含用户过滤字段
- **WHEN** 查询用户列表
- **THEN** SysUserListQuery MUST 包含以下可选过滤字段：username（String）、phone（String）、lockFlag（Integer）、groupId（Long）
- **AND** 所有过滤字段为可选（nullable）

#### Scenario: SysRoleListQuery 包含角色过滤字段
- **WHEN** 查询角色列表
- **THEN** SysRoleListQuery MUST 包含以下可选过滤字段：roleName（String）、roleCode（String）

#### Scenario: SysGroupListQuery 包含分组过滤字段
- **WHEN** 查询分组列表
- **THEN** SysGroupListQuery MUST 包含以下可选过滤字段：groupName（String）

#### Scenario: SysMenuListQuery 包含菜单过滤字段
- **WHEN** 查询菜单列表
- **THEN** SysMenuListQuery MUST 包含以下可选过滤字段：menuName（String）、type（Integer）、scope（String）

#### Scenario: SysTenantListQuery 包含租户过滤字段
- **WHEN** 查询租户列表
- **THEN** SysTenantListQuery MUST 包含以下可选过滤字段：tenantName（String）

#### Scenario: SysI18nListQuery 包含翻译消息过滤字段
- **WHEN** 查询翻译消息列表
- **THEN** SysI18nListQuery MUST 包含以下可选过滤字段：code（String）、language（String）
- **AND** 所有过滤字段为可选（nullable）

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

#### Scenario: 翻译消息列表查询按条件过滤
- **WHEN** SysI18nListQuery 中 code 不为空
- **THEN** Repository 实现 MUST 在 SQL 查询中添加 code LIKE 条件
- **WHEN** SysI18nListQuery 中 language 不为空
- **THEN** Repository 实现 MUST 在 SQL 查询中添加 language = 条件
