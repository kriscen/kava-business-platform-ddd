## ADDED Requirements

### Requirement: Dept DomainService 树形查询
SysDeptService SHALL 提供 `queryTree()` 方法，将所有部门按 pid 组装为树形结构返回，用于前端部门树展示和选择器。

#### Scenario: 查询部门树 — 有层级数据
- **WHEN** 调用 `SysDeptService.queryTree()`
- **THEN** 将所有部门按 pid 组装为树形结构
- **AND** 返回 `List<SysDeptEntity>`，每个 entity 的 children 为直接子节点

#### Scenario: 查询部门树 — 无数据
- **WHEN** 调用 `SysDeptService.queryTree()` 且无部门数据
- **THEN** 返回空列表

### Requirement: Dept tree HTTP 端点
系统 SHALL 提供 `GET /api/v1/sys/dept/tree` 端点，返回当前租户下部门树形结构。

#### Scenario: 前端请求部门树
- **WHEN** 前端调用 `GET /api/v1/sys/dept/tree`
- **THEN** 返回当前租户下部门树形结构（id、name、pid、children）
