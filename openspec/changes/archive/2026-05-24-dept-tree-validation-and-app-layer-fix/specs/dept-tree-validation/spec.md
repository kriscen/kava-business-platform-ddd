## ADDED Requirements

### Requirement: 部门父节点自引用检查
SysDeptService SHALL 在创建和更新部门时校验 pid 不得等于自身 id。

#### Scenario: 创建部门时 pid 等于自身 id（不可能场景，但保持一致性）
- **WHEN** 调用 `SysDeptService.create(entity)` 且 entity 的 pid 不为 null
- **THEN** 若 pid 与 id 相同，抛出 `UpmsBizException(DEPT_PID_SELF_REFERENCE)`

#### Scenario: 更新部门时 pid 等于自身 id
- **WHEN** 调用 `SysDeptService.update(entity)` 且 entity 的 pid 不为 null
- **THEN** 若 pid 与 id 相同，抛出 `UpmsBizException(DEPT_PID_SELF_REFERENCE)`

#### Scenario: 创建/更新部门时 pid 为 null
- **WHEN** 调用 `SysDeptService.create(entity)` 或 `update(entity)` 且 entity 的 pid 为 null
- **THEN** 跳过自引用和循环引用检查，正常执行

### Requirement: 部门父节点循环引用检查
SysDeptService SHALL 在创建和更新部门时校验 pid 不得形成循环引用。通过加载当前租户所有部门，构建 childToParent 映射，从 pid 向上遍历父链检测循环。

#### Scenario: 更新部门时 pid 是当前节点的子孙节点
- **WHEN** 调用 `SysDeptService.update(entity)` 且 pid 是 entity 的某个子孙节点
- **THEN** 抛出 `UpmsBizException(DEPT_PID_CIRCULAR)`

#### Scenario: 创建部门时 pid 不形成循环
- **WHEN** 调用 `SysDeptService.create(entity)` 且 pid 不为 null 且不形成循环
- **THEN** 正常创建部门

#### Scenario: 更新部门时 pid 不形成循环
- **WHEN** 调用 `SysDeptService.update(entity)` 且 pid 不为 null 且不形成循环
- **THEN** 正常更新部门

### Requirement: 部门删除前子节点检查
SysDeptService SHALL 在批量删除部门前检查每个部门是否存在子节点。若存在子节点，拒绝删除。

#### Scenario: 删除存在子节点的部门
- **WHEN** 调用 `SysDeptService.removeBatchByIds(ids)` 且某个 id 存在子部门
- **THEN** 抛出 `UpmsBizException(DEPT_HAS_CHILDREN)`

#### Scenario: 删除无子节点的部门
- **WHEN** 调用 `SysDeptService.removeBatchByIds(ids)` 且所有 id 均无子部门
- **THEN** 继续执行后续检查和删除

### Requirement: 部门删除前用户引用检查
SysDeptService SHALL 在批量删除部门前检查每个部门是否被用户引用（`sys_user.dept_id`）。若存在用户引用，拒绝删除。

#### Scenario: 删除被用户引用的部门
- **WHEN** 调用 `SysDeptService.removeBatchByIds(ids)` 且某个 id 被 `sys_user.dept_id` 引用
- **THEN** 抛出 `UpmsBizException(DEPT_REFERENCED_BY_USER)`

#### Scenario: 删除未被用户引用的部门
- **WHEN** 调用 `SysDeptService.removeBatchByIds(ids)` 且所有 id 均无用户引用
- **THEN** 正常执行删除
