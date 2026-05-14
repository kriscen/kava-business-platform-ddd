## ADDED Requirements

### Requirement: DataScope 注解标记
系统 SHALL 提供 `@DataScope` 注解，用于标记需要数据权限行级过滤的 Mapper 方法或 Service 方法。

#### Scenario: 方法标记了 @DataScope
- **WHEN** Mapper 方法标注了 `@DataScope`
- **THEN** 执行 SQL 时，拦截器根据当前用户角色的 dsType 策略自动追加行级过滤条件

#### Scenario: 方法未标记 @DataScope
- **WHEN** Mapper 方法未标注 `@DataScope`
- **THEN** 不追加数据权限过滤条件（仅租户隔离仍然生效）

### Requirement: 全部数据权限（dsType = ALL）
当角色的数据权限策略为 ALL 时，系统 SHALL 不追加额外的行级过滤条件。

#### Scenario: 角色拥有全部数据权限
- **WHEN** 用户主角色的 dsType 为 ALL
- **THEN** 查询不追加部门或人员过滤条件
- **AND** 在租户范围内可查看所有数据

### Requirement: 自定义数据权限（dsType = CUSTOM）
当角色的数据权限策略为 CUSTOM 时，系统 SHALL 根据角色的 dsScope 字段追加部门过滤条件。

#### Scenario: 角色拥有自定义数据权限
- **WHEN** 用户主角色的 dsType 为 CUSTOM，dsScope 包含部门 ID 列表 "1,2,3"
- **THEN** 查询追加 `WHERE dept_id IN (1, 2, 3)`

### Requirement: 本部门及以下数据权限（dsType = DEPT_AND_CHILD）
当角色的数据权限策略为 DEPT_AND_CHILD 时，系统 SHALL 追加用户所在部门及其递归子部门的过滤条件。

#### Scenario: 角色拥有本部门及以下权限
- **WHEN** 用户主角色的 dsType 为 DEPT_AND_CHILD，用户所在部门 ID 为 1
- **THEN** 系统递归查询部门 ID 1 的所有子部门
- **AND** 查询追加 `WHERE dept_id IN (1, 子部门1, 子部门2, ...)`

### Requirement: 本部门数据权限（dsType = DEPT_ONLY）
当角色的数据权限策略为 DEPT_ONLY 时，系统 SHALL 仅追加用户所在部门的过滤条件。

#### Scenario: 角色拥有本部门权限
- **WHEN** 用户主角色的 dsType 为 DEPT_ONLY，用户所在部门 ID 为 1
- **THEN** 查询追加 `WHERE dept_id = 1`

### Requirement: 仅本人数据权限（dsType = SELF）
当角色的数据权限策略为 SELF 时，系统 SHALL 追加当前用户 ID 的过滤条件。

#### Scenario: 角色拥有仅本人权限
- **WHEN** 用户主角色的 dsType 为 SELF，用户 ID 为 100
- **THEN** 查询追加 `WHERE creator = 100`（或 `WHERE user_id = 100`，根据表结构决定）

### Requirement: 多角色数据权限合并
当用户拥有多个角色时，系统 SHALL 取权限范围最大的角色策略。

#### Scenario: 用户拥有多个角色
- **WHEN** 用户同时拥有 dsType=SELF 和 dsType=DEPT_ONLY 的两个角色
- **THEN** 系统采用 DEPT_ONLY（权限范围更大）策略
- **AND** dsType 优先级排序为：ALL > CUSTOM > DEPT_AND_CHILD > DEPT_ONLY > SELF

### Requirement: 平台管理员跳过数据权限过滤
平台管理员执行查询时，系统 SHALL 不追加数据权限过滤条件。

#### Scenario: 平台管理员查询带 @DataScope 的数据
- **WHEN** 平台管理员执行标记了 `@DataScope` 的查询
- **THEN** 不追加任何部门/人员过滤条件
