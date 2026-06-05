# Capability: group-management

## Purpose

分组子域完整管理：DomainService CRUD 委托、树形结构验证（自引用/循环引用/子节点/用户引用）、树形查询，保障分组从创建到删除的全生命周期正确性。

## Requirements

### Requirement: Group DomainService CRUD 委托
SysGroupService SHALL 注入 ISysGroupRepository，将 CRUD 操作委托转发至 Repository，并在创建和更新时执行树形验证规则（自引用检查、循环引用检查），在删除前执行约束检查（子节点检查、用户引用检查）。

#### Scenario: 创建分组
- **WHEN** 调用 `SysGroupService.create(entity)`
- **AND** pid 为 null 或通过自引用和循环引用检查
- **THEN** 委托 `ISysGroupRepository.create(entity)` 并返回 `SysGroupId`

#### Scenario: 创建分组 — 自引用检查失败
- **WHEN** 调用 `SysGroupService.create(entity)` 且 pid 等于 id
- **THEN** 抛出 `UpmsBizException(GROUP_PID_SELF_REFERENCE)`

#### Scenario: 创建分组 — 循环引用检查失败
- **WHEN** 调用 `SysGroupService.create(entity)` 且 pid 形成循环引用
- **THEN** 抛出 `UpmsBizException(GROUP_PID_CIRCULAR)`

#### Scenario: 更新分组
- **WHEN** 调用 `SysGroupService.update(entity)`
- **AND** pid 为 null 或通过自引用和循环引用检查
- **THEN** 委托 `ISysGroupRepository.update(entity)` 并返回 `Boolean`

#### Scenario: 更新分组 — 自引用检查失败
- **WHEN** 调用 `SysGroupService.update(entity)` 且 pid 等于 id
- **THEN** 抛出 `UpmsBizException(GROUP_PID_SELF_REFERENCE)`

#### Scenario: 更新分组 — 循环引用检查失败
- **WHEN** 调用 `SysGroupService.update(entity)` 且 pid 形成循环引用
- **THEN** 抛出 `UpmsBizException(GROUP_PID_CIRCULAR)`

#### Scenario: 分页查询分组
- **WHEN** 调用 `SysGroupService.queryPage(query)`
- **THEN** 委托 `ISysGroupRepository.queryPage(query)` 并返回 `PagingInfo<SysGroupEntity>`

#### Scenario: 按ID查询分组
- **WHEN** 调用 `SysGroupService.queryById(id)`
- **THEN** 委托 `ISysGroupRepository.queryById(id)` 并返回 `SysGroupEntity`

#### Scenario: 批量删除分组
- **WHEN** 调用 `SysGroupService.removeBatchByIds(ids)`
- **AND** 所有 id 均无子分组且无用户引用
- **THEN** 委托 `ISysGroupRepository.removeBatchByIds(ids)` 并返回 `Boolean`

#### Scenario: 批量删除分组 — 存在子节点
- **WHEN** 调用 `SysGroupService.removeBatchByIds(ids)` 且某个 id 存在子分组
- **THEN** 抛出 `UpmsBizException(GROUP_HAS_CHILDREN)`

#### Scenario: 批量删除分组 — 存在用户引用
- **WHEN** 调用 `SysGroupService.removeBatchByIds(ids)` 且某个 id 被用户引用
- **THEN** 抛出 `UpmsBizException(GROUP_REFERENCED_BY_USER)`

### Requirement: 分组父节点自引用检查
SysGroupService SHALL 在创建和更新分组时校验 pid 不得等于自身 id。

#### Scenario: 创建/更新分组时 pid 为 null
- **WHEN** 调用 `SysGroupService.create(entity)` 或 `update(entity)` 且 entity 的 pid 为 null
- **THEN** 跳过自引用和循环引用检查，正常执行

### Requirement: 分组父节点循环引用检查
SysGroupService SHALL 在创建和更新分组时校验 pid 不得形成循环引用。通过加载当前租户所有分组，构建 childToParent 映射，从 pid 向上遍历父链检测循环。

#### Scenario: 更新分组时 pid 是当前节点的子孙节点
- **WHEN** 调用 `SysGroupService.update(entity)` 且 pid 是 entity 的某个子孙节点
- **THEN** 抛出 `UpmsBizException(GROUP_PID_CIRCULAR)`

#### Scenario: 创建/更新分组时 pid 不形成循环
- **WHEN** 调用 `SysGroupService.create(entity)` 或 `update(entity)` 且 pid 不为 null 且不形成循环
- **THEN** 正常执行

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

### Requirement: Group DomainService 树形查询
SysGroupService SHALL 提供 `queryTree()` 方法，将所有分组按 pid 组装为树形结构返回，用于前端分组树展示和选择器。

#### Scenario: 查询分组树 — 有层级数据
- **WHEN** 调用 `SysGroupService.queryTree()`
- **THEN** 将所有分组按 pid 组装为树形结构
- **AND** 返回 `List<SysGroupEntity>`，每个 entity 的 children 为直接子节点

#### Scenario: 查询分组树 — 无数据
- **WHEN** 调用 `SysGroupService.queryTree()` 且无分组数据
- **THEN** 返回空列表

### Requirement: Group tree HTTP 端点
系统 SHALL 提供 `GET /api/v1/sys/group/tree` 端点，返回当前租户下分组树形结构。

#### Scenario: 前端请求分组树
- **WHEN** 前端调用 `GET /api/v1/sys/group/tree`
- **THEN** 返回当前租户下分组树形结构（id、name、pid、children）

### Requirement: Group tree 复用树构建逻辑
Group tree 端点 SHALL 复用与 Area 相同的树构建工具（`TreeBuilder`），不引入新的树构建实现。

#### Scenario: Group tree 使用 TreeBuilder
- **WHEN** 实现 Group tree 查询
- **THEN** 复用 `TreeBuilder.build()` 方法
- **AND** 与 Area 的树构建使用相同的工具类和节点转换模式
