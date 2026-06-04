# Capability: group-tree-validation

## Purpose

分组树形结构验证规则：在创建、更新和删除分组时，校验自引用、循环引用、子节点存在性和用户引用约束，保障分组树的数据完整性。

## Requirements

### Requirement: 分组父节点自引用检查
SysGroupService SHALL 在创建和更新分组时校验 pid 不得等于自身 id。

#### Scenario: 创建分组时 pid 等于自身 id（不可能场景，但保持一致性）
- **WHEN** 调用 `SysGroupService.create(entity)` 且 entity 的 pid 不为 null
- **THEN** 若 pid 与 id 相同，抛出 `UpmsBizException(GROUP_PID_SELF_REFERENCE)`

#### Scenario: 更新分组时 pid 等于自身 id
- **WHEN** 调用 `SysGroupService.update(entity)` 且 entity 的 pid 不为 null
- **THEN** 若 pid 与 id 相同，抛出 `UpmsBizException(GROUP_PID_SELF_REFERENCE)`

#### Scenario: 创建/更新分组时 pid 为 null
- **WHEN** 调用 `SysGroupService.create(entity)` 或 `update(entity)` 且 entity 的 pid 为 null
- **THEN** 跳过自引用和循环引用检查，正常执行

### Requirement: 分组父节点循环引用检查
SysGroupService SHALL 在创建和更新分组时校验 pid 不得形成循环引用。通过加载当前租户所有分组，构建 childToParent 映射，从 pid 向上遍历父链检测循环。

#### Scenario: 更新分组时 pid 是当前节点的子孙节点
- **WHEN** 调用 `SysGroupService.update(entity)` 且 pid 是 entity 的某个子孙节点
- **THEN** 抛出 `UpmsBizException(GROUP_PID_CIRCULAR)`

#### Scenario: 创建分组时 pid 不形成循环
- **WHEN** 调用 `SysGroupService.create(entity)` 且 pid 不为 null 且不形成循环
- **THEN** 正常创建分组

#### Scenario: 更新分组时 pid 不形成循环
- **WHEN** 调用 `SysGroupService.update(entity)` 且 pid 不为 null 且不形成循环
- **THEN** 正常更新分组

### Requirement: 分组删除前子节点检查
SysGroupService SHALL 在批量删除分组前检查每个分组是否存在子节点。若存在子节点，拒绝删除。

#### Scenario: 删除存在子节点的分组
- **WHEN** 调用 `SysGroupService.removeBatchByIds(ids)` 且某个 id 存在子分组
- **THEN** 抛出 `UpmsBizException(GROUP_HAS_CHILDREN)`

#### Scenario: 删除无子节点的分组
- **WHEN** 调用 `SysGroupService.removeBatchByIds(ids)` 且所有 id 均无子分组
- **THEN** 继续执行后续检查和删除

### Requirement: 分组删除前用户引用检查
SysGroupService SHALL 在批量删除分组前检查每个分组是否被用户引用（`sys_user.group_id`）。若存在用户引用，拒绝删除。

#### Scenario: 删除被用户引用的分组
- **WHEN** 调用 `SysGroupService.removeBatchByIds(ids)` 且某个 id 被 `sys_user.group_id` 引用
- **THEN** 抛出 `UpmsBizException(GROUP_REFERENCED_BY_USER)`

#### Scenario: 删除未被用户引用的分组
- **WHEN** 调用 `SysGroupService.removeBatchByIds(ids)` 且所有 id 均无用户引用
- **THEN** 正常执行删除

### Requirement: Group tree 复用树构建逻辑
Group tree 端点 SHALL 复用与 Area 相同的树构建工具（`TreeBuilder`），不引入新的树构建实现。

#### Scenario: Group tree 使用 TreeBuilder
- **WHEN** 实现 Group tree 查询
- **THEN** 复用 `TreeBuilder.build()` 方法
- **AND** 与 Area 的树构建使用相同的工具类和节点转换模式
