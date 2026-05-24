## ADDED Requirements

### Requirement: Menu tree 端点返回完整树形结构
`GET /api/v1/sys/menu/tree` 端点 SHALL 返回完整的树形结构，每个节点包含 `children` 字段。当前 `SysMenuListResponse` 缺少 `children` 字段，导致 App 层构建的树结构在 Adapter 层被丢弃。

#### Scenario: Menu tree 端点返回带 children 的树
- **WHEN** 前端调用 `GET /api/v1/sys/menu/tree`
- **THEN** 返回树形结构，每个节点包含 id、name、permission、pid、icon、path、component、visible、sortOrder、menuType、keepAlive、embedded、scope、children
- **AND** children 为该节点的直接子节点列表，递归嵌套
- **AND** 叶子节点的 children 为空列表

#### Scenario: Menu tree 用于角色编辑（全量菜单树）
- **WHEN** 前端打开角色编辑页面，需要展示可分配的完整菜单树
- **THEN** 端点返回当前租户下所有菜单的树形结构（不限当前用户权限）
