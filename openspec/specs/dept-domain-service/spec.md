# Capability: dept-domain-service

## Purpose

部门子域 DomainService 层：封装部门聚合的 CRUD 委托逻辑，为 ApplicationService 提供部门领域的统一操作入口。

## Requirements

### Requirement: Dept DomainService CRUD 委托
SysDeptService SHALL 注入 ISysDeptRepository，将 CRUD 操作委托转发至 Repository，并在创建和更新时执行树形验证规则（自引用检查、循环引用检查），在删除前执行约束检查（子节点检查、用户引用检查）。

#### Scenario: 创建部门
- **WHEN** 调用 `SysDeptService.create(entity)`
- **AND** pid 为 null 或通过自引用和循环引用检查
- **THEN** 委托 `ISysDeptRepository.create(entity)` 并返回 `SysDeptId`

#### Scenario: 创建部门 — 自引用检查失败
- **WHEN** 调用 `SysDeptService.create(entity)` 且 pid 等于 id
- **THEN** 抛出 `UpmsBizException(DEPT_PID_SELF_REFERENCE)`

#### Scenario: 创建部门 — 循环引用检查失败
- **WHEN** 调用 `SysDeptService.create(entity)` 且 pid 形成循环引用
- **THEN** 抛出 `UpmsBizException(DEPT_PID_CIRCULAR)`

#### Scenario: 更新部门
- **WHEN** 调用 `SysDeptService.update(entity)`
- **AND** pid 为 null 或通过自引用和循环引用检查
- **THEN** 委托 `ISysDeptRepository.update(entity)` 并返回 `Boolean`

#### Scenario: 更新部门 — 自引用检查失败
- **WHEN** 调用 `SysDeptService.update(entity)` 且 pid 等于 id
- **THEN** 抛出 `UpmsBizException(DEPT_PID_SELF_REFERENCE)`

#### Scenario: 更新部门 — 循环引用检查失败
- **WHEN** 调用 `SysDeptService.update(entity)` 且 pid 形成循环引用
- **THEN** 抛出 `UpmsBizException(DEPT_PID_CIRCULAR)`

#### Scenario: 分页查询部门
- **WHEN** 调用 `SysDeptService.queryPage(query)`
- **THEN** 委托 `ISysDeptRepository.queryPage(query)` 并返回 `PagingInfo<SysDeptEntity>`

#### Scenario: 按ID查询部门
- **WHEN** 调用 `SysDeptService.queryById(id)`
- **THEN** 委托 `ISysDeptRepository.queryById(id)` 并返回 `SysDeptEntity`

#### Scenario: 批量删除部门
- **WHEN** 调用 `SysDeptService.removeBatchByIds(ids)`
- **AND** 所有 id 均无子部门且无用户引用
- **THEN** 委托 `ISysDeptRepository.removeBatchByIds(ids)` 并返回 `Boolean`

#### Scenario: 批量删除部门 — 存在子节点
- **WHEN** 调用 `SysDeptService.removeBatchByIds(ids)` 且某个 id 存在子部门
- **THEN** 抛出 `UpmsBizException(DEPT_HAS_CHILDREN)`

#### Scenario: 批量删除部门 — 存在用户引用
- **WHEN** 调用 `SysDeptService.removeBatchByIds(ids)` 且某个 id 被用户引用
- **THEN** 抛出 `UpmsBizException(DEPT_REFERENCED_BY_USER)`

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
