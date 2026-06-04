## MODIFIED Requirements

### Requirement: ListQuery 值对象包含实际过滤字段

ListQuery 值对象 MUST 包含对应实体的实际过滤条件字段，MUST NOT 仅有分页参数。

#### Scenario: SysUserListQuery 包含用户过滤字段
- **WHEN** 查询用户列表
- **THEN** SysUserListQuery MUST 包含以下可选过滤字段：username（String）、phone（String）、lockFlag（Integer）、groupId（Long）
- **AND** 所有过滤字段为可选（nullable）

#### Scenario: SysGroupListQuery 包含分组过滤字段
- **WHEN** 查询分组列表
- **THEN** SysGroupListQuery MUST 包含以下可选过滤字段：groupName（String）

#### Scenario: SysMenuListQuery 包含菜单过滤字段
- **WHEN** 查询菜单列表
- **THEN** SysMenuListQuery MUST 包含以下可选过滤字段：menuName（String）、type（Integer）、level（String）
