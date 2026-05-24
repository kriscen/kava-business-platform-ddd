# Capability: menu-domain-service

## Purpose

菜单子域 DomainService 层：封装菜单聚合的 CRUD 委托及集合查询逻辑，为 ApplicationService 提供菜单领域的统一操作入口。

## Requirements

### Requirement: Menu DomainService CRUD 委托

SysMenuService SHALL 注入 ISysMenuRepository，并将 CRUD 操作委托转发至 Repository。创建和修改操作 SHALL 在委托前执行 pid 校验。删除操作 SHALL 在委托前执行子菜单和角色引用检查。

#### Scenario: 创建菜单 — pid 为 null（顶级菜单）
- **WHEN** 调用 `SysMenuService.create(entity)` 且 entity.pid 为 null
- **THEN** 跳过 pid 校验，委托 `ISysMenuRepository.create(entity)` 并返回 `SysMenuId`

#### Scenario: 创建菜单 — pid 指向自身
- **WHEN** 调用 `SysMenuService.create(entity)` 且 entity.pid 等于 entity.id
- **THEN** 抛出 `UpmsBizException(MENU_PID_SELF_REFERENCE)`

#### Scenario: 创建菜单 — pid 形成环
- **WHEN** 调用 `SysMenuService.create(entity)` 且 entity.pid 指向 entity 自身的某个后代节点
- **THEN** 抛出 `UpmsBizException(MENU_PID_CIRCULAR)`

#### Scenario: 创建菜单 — pid 合法
- **WHEN** 调用 `SysMenuService.create(entity)` 且 entity.pid 不等于 entity.id 且不形成环
- **THEN** 委托 `ISysMenuRepository.create(entity)` 并返回 `SysMenuId`

#### Scenario: 创建菜单 — sortOrder 为 null
- **WHEN** 调用 `SysMenuService.create(entity)` 且 entity.sortOrder 为 null
- **THEN** 将 sortOrder 设为 0 后委托 `ISysMenuRepository.create(entity)`

#### Scenario: 更新菜单
- **WHEN** 调用 `SysMenuService.update(entity)`
- **THEN** 执行与 create 相同的 pid 校验规则，通过后委托 `ISysMenuRepository.update(entity)` 并返回 `Boolean`

#### Scenario: 更新菜单 — pid 指向自身
- **WHEN** 调用 `SysMenuService.update(entity)` 且 entity.pid 等于 entity.id
- **THEN** 抛出 `UpmsBizException(MENU_PID_SELF_REFERENCE)`

#### Scenario: 更新菜单 — pid 形成环
- **WHEN** 调用 `SysMenuService.update(entity)` 且 entity.pid 指向 entity 自身的某个后代节点
- **THEN** 抛出 `UpmsBizException(MENU_PID_CIRCULAR)`

#### Scenario: 分页查询菜单
- **WHEN** 调用 `SysMenuService.queryPage(query)`
- **THEN** 委托 `ISysMenuRepository.queryPage(query)` 并返回 `PagingInfo<SysMenuEntity>`

#### Scenario: 按ID查询菜单
- **WHEN** 调用 `SysMenuService.queryById(id)`
- **THEN** 委托 `ISysMenuRepository.queryById(id)` 并返回 `SysMenuEntity`

#### Scenario: 批量删除菜单 — 存在子菜单
- **WHEN** 调用 `SysMenuService.removeBatchByIds(ids)` 且 ids 中任一菜单在 sys_menu 中有子菜单记录
- **THEN** 抛出 `UpmsBizException(MENU_HAS_CHILDREN)`

#### Scenario: 批量删除菜单 — 存在角色引用
- **WHEN** 调用 `SysMenuService.removeBatchByIds(ids)` 且 ids 中任一菜单在 sys_role_menu 中有角色绑定记录
- **THEN** 抛出 `UpmsBizException(MENU_REFERENCED_BY_ROLE)`

#### Scenario: 批量删除菜单 — 校验通过
- **WHEN** 调用 `SysMenuService.removeBatchByIds(ids)` 且所有菜单均无子菜单且无角色引用
- **THEN** 委托 `ISysMenuRepository.removeBatchByIds(ids)` 并返回 `Boolean`

### Requirement: Menu DomainService 暴露 queryAll 方法

ISysMenuService 接口 SHALL 提供 `queryAll()` 方法，委托 `ISysMenuRepository.queryAll()` 返回所有菜单列表。

#### Scenario: 查询全部菜单
- **WHEN** 调用 `SysMenuService.queryAll()`
- **THEN** 委托 `ISysMenuRepository.queryAll()` 并返回 `List<SysMenuEntity>`

### Requirement: Menu DomainService 暴露 queryByIds 方法

ISysMenuService 接口 SHALL 提供 `queryByIds(List<SysMenuId>)` 方法，委托 `ISysMenuRepository.queryByIds(ids)` 返回指定菜单列表。

#### Scenario: 按ID列表批量查询菜单
- **WHEN** 调用 `SysMenuService.queryByIds(ids)` 且 ids 非空
- **THEN** 委托 `ISysMenuRepository.queryByIds(ids)` 并返回对应的 `List<SysMenuEntity>`

#### Scenario: 空ID列表查询菜单
- **WHEN** 调用 `SysMenuService.queryByIds(ids)` 且 ids 为空列表
- **THEN** 委托 `ISysMenuRepository.queryByIds(ids)` 并返回空列表
