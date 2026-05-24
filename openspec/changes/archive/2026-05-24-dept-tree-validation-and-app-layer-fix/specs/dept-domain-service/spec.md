## MODIFIED Requirements

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
