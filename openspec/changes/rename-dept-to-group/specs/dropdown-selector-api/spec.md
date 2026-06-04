## MODIFIED Requirements

### Requirement: 分组树下拉端点

系统 SHALL 提供 `GET /api/v1/sys/group/tree` 端点，返回当前租户下所有分组的树形结构（id、name、pid、children），用于用户表单的分组单选。

#### Scenario: 查询分组树
- **WHEN** 前端调用 `GET /api/v1/sys/group/tree`
- **THEN** 返回当前租户下所有分组的树形结构
- **AND** 每个节点包含 id、name、pid、children

#### Scenario: 当前租户无分组
- **WHEN** 前端调用 `GET /api/v1/sys/group/tree` 且当前租户无分组
- **THEN** 返回空列表
