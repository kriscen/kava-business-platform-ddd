## ADDED Requirements

### Requirement: Area tree 作为 Dept tree 参考实现
Area tree 端点的实现模式（`TreeBuilder` 使用、节点转换、controller 层调用）SHALL 作为 Dept tree 实现的参考模板。

#### Scenario: Dept tree 实现遵循 Area tree 模式
- **WHEN** 开发者实现 Dept tree
- **THEN** 参考 Area 的 `SysAreaService.selectAreaTree()` 实现模式
- **AND** 复用相同的 `TreeBuilder` 工具和 `TreeNode` 接口
