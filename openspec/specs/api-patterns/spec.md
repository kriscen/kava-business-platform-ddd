# Capability: api-patterns

## Purpose

定义跨实体的 API 查询和展示模式：ListQuery 值对象过滤、下拉选择器端点、响应关联名称补充，为前端 CRUD 页面提供统一的数据查询和展示规范。

## Requirements

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
- **THEN** SysMenuListQuery MUST 包含以下可选过滤字段：menuName（String）、type（Integer）、level（String）

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

### Requirement: 角色下拉列表端点
系统 SHALL 提供 `GET /api/v1/sys/role/dropdown` 端点，返回当前租户下所有角色的精简列表（id、roleName、roleCode），不分页，用于用户表单的角色多选下拉。

#### Scenario: 查询角色下拉列表
- **WHEN** 前端调用 `GET /api/v1/sys/role/dropdown`
- **THEN** 返回当前租户下所有角色（仅 id、roleName、roleCode）
- **AND** 不分页，按 roleName 排序

#### Scenario: 当前租户无角色
- **WHEN** 前端调用 `GET /api/v1/sys/role/dropdown` 且当前租户无角色
- **THEN** 返回空列表

### Requirement: 分组树下拉端点
系统 SHALL 提供 `GET /api/v1/sys/group/tree` 端点，返回当前租户下所有分组的树形结构（id、name、pid、children），用于用户表单的分组单选。

#### Scenario: 查询分组树
- **WHEN** 前端调用 `GET /api/v1/sys/group/tree`
- **THEN** 返回当前租户下所有分组的树形结构
- **AND** 每个节点包含 id、name、pid、children

#### Scenario: 当前租户无分组
- **WHEN** 前端调用 `GET /api/v1/sys/group/tree` 且当前租户无分组
- **THEN** 返回空列表

### Requirement: 租户下拉列表端点
系统 SHALL 提供 `GET /api/v1/sys/tenant/dropdown` 端点，返回所有租户的精简列表（id、name、code、status），不分页，用于平台管理员表单的租户选择。

#### Scenario: 查询租户下拉列表
- **WHEN** 前端调用 `GET /api/v1/sys/tenant/dropdown`
- **THEN** 返回所有租户（id、name、code、status）
- **AND** 不分页，按 name 排序

#### Scenario: 系统无租户
- **WHEN** 前端调用 `GET /api/v1/sys/tenant/dropdown` 且系统无租户
- **THEN** 返回空列表

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

### Requirement: 角色详情响应补充菜单名称
角色详情响应 SHALL 在返回 menuIds 的同时，补充 `menuNames` 字段（菜单名称列表），用于前端展示角色已分配的权限。

#### Scenario: 角色详情返回菜单名称
- **WHEN** 前端调用 `GET /api/v1/sys/role/{id}`
- **THEN** 响应包含 `menuNames` 字段，与 `menuIds` 一一对应
- **AND** 当 menuIds 为空时 menuNames 为空列表

### Requirement: 分组响应补充父级名称
分组详情和列表响应 SHALL 包含 `parentName` 字段，用于前端展示分组层级路径。

#### Scenario: 分组详情返回父级名称
- **WHEN** 前端调用 `GET /api/v1/sys/group/{id}` 且该分组有父级
- **THEN** 响应包含 `parentName` 字段，值为父级分组名称

#### Scenario: 分组详情为顶级分组
- **WHEN** 前端调用 `GET /api/v1/sys/group/{id}` 且该分组 pid 为 null
- **THEN** `parentName` 为 null

### Requirement: 菜单响应补充父级名称
菜单详情和列表响应 SHALL 包含 `parentName` 字段，用于前端展示菜单层级路径。

#### Scenario: 菜单详情返回父级名称
- **WHEN** 前端调用 `GET /api/v1/sys/menu/{id}` 且该菜单有父级
- **THEN** 响应包含 `parentName` 字段，值为父级菜单名称

#### Scenario: 菜单详情为顶级菜单
- **WHEN** 前端调用 `GET /api/v1/sys/menu/{id}` 且该菜单 pid 为 null
- **THEN** `parentName` 为 null
