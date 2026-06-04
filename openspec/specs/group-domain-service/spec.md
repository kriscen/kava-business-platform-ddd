# Capability: group-domain-service

## Purpose

分组子域 DomainService 层：封装分组聚合的 CRUD 委托逻辑，为 ApplicationService 提供分组领域的统一操作入口。

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
