# Capability: menu-domain-service

## Purpose

菜单子域 DomainService 层：封装菜单聚合的 CRUD 委托及集合查询逻辑，为 ApplicationService 提供菜单领域的统一操作入口。

## Requirements

### Requirement: Menu DomainService CRUD 委托

SysMenuService SHALL 注入 ISysMenuRepository，并将所有 CRUD 操作委托转发至 Repository。

#### Scenario: 创建菜单
- **WHEN** 调用 `SysMenuService.create(entity)`
- **THEN** 委托 `ISysMenuRepository.create(entity)` 并返回 `SysMenuId`

#### Scenario: 更新菜单
- **WHEN** 调用 `SysMenuService.update(entity)`
- **THEN** 委托 `ISysMenuRepository.update(entity)` 并返回 `Boolean`

#### Scenario: 分页查询菜单
- **WHEN** 调用 `SysMenuService.queryPage(query)`
- **THEN** 委托 `ISysMenuRepository.queryPage(query)` 并返回 `PagingInfo<SysMenuEntity>`

#### Scenario: 按ID查询菜单
- **WHEN** 调用 `SysMenuService.queryById(id)`
- **THEN** 委托 `ISysMenuRepository.queryById(id)` 并返回 `SysMenuEntity`

#### Scenario: 批量删除菜单
- **WHEN** 调用 `SysMenuService.removeBatchByIds(ids)`
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
