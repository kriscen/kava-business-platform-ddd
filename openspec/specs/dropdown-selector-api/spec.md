# Capability: dropdown-selector-api

## Purpose

为前端表单提供非分页的下拉选择器数据接口，用于用户/角色等表单中关联实体的选择。

## Requirements

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
