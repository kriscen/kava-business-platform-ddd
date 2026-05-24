## MODIFIED Requirements

### Requirement: 查询角色详情时返回关联菜单 ID 列表
查询角色详情时，系统 SHALL 返回该角色关联的 menuIds 列表及 menuNames 列表。

#### Scenario: 查询角色详情
- **WHEN** 管理员查询角色详情
- **THEN** 返回角色基本信息及关联的 menuIds 列表
- **AND** 返回 menuNames 列表，与 menuIds 一一对应
