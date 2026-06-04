## MODIFIED Requirements

### Requirement: 本分组及以下数据权限（dsType = GROUP_AND_CHILD）

当角色的数据权限策略为 GROUP_AND_CHILD 时，系统 SHALL 追加用户所在分组及其递归子分组的过滤条件。

#### Scenario: 角色拥有本分组及以下权限
- **WHEN** 用户主角色的 dsType 为 GROUP_AND_CHILD，用户所在分组 ID 为 1
- **THEN** 系统递归查询分组 ID 1 的所有子分组
- **AND** 查询追加 `WHERE group_id IN (1, 子分组1, 子分组2, ...)`

### Requirement: 本分组数据权限（dsType = GROUP_ONLY）

当角色的数据权限策略为 GROUP_ONLY 时，系统 SHALL 仅追加用户所在分组的过滤条件。

#### Scenario: 角色拥有本分组权限
- **WHEN** 用户主角色的 dsType 为 GROUP_ONLY，用户所在分组 ID 为 1
- **THEN** 查询追加 `WHERE group_id = 1`

### Requirement: 自定义数据权限（dsType = CUSTOM）

当角色的数据权限策略为 CUSTOM 时，系统 SHALL 根据角色的 dsScope 字段追加分组过滤条件。

#### Scenario: 角色拥有自定义数据权限
- **WHEN** 用户主角色的 dsType 为 CUSTOM，dsScope 包含分组 ID 列表 "1,2,3"
- **THEN** 查询追加 `WHERE group_id IN (1, 2, 3)`

### Requirement: 多角色数据权限合并

当用户拥有多个角色时，系统 SHALL 取权限范围最大的角色策略。

#### Scenario: 用户拥有多个角色
- **WHEN** 用户同时拥有 dsType=SELF 和 dsType=GROUP_ONLY 的两个角色
- **THEN** 系统采用 GROUP_ONLY（权限范围更大）策略
- **AND** dsType 优先级排序为：ALL > CUSTOM > GROUP_AND_CHILD > GROUP_ONLY > SELF
