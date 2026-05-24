## ADDED Requirements

### Requirement: Dept tree 复用树构建逻辑
Dept tree 端点 SHALL 复用与 Area 相同的树构建工具（`TreeBuilder`），不引入新的树构建实现。

#### Scenario: Dept tree 使用 TreeBuilder
- **WHEN** 实现 Dept tree 查询
- **THEN** 复用 `TreeBuilder.build()` 方法
- **AND** 与 Area 的树构建使用相同的工具类和节点转换模式
