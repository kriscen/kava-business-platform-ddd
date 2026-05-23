# Capability: dept-domain-service

## Purpose

部门子域 DomainService 层：封装部门聚合的 CRUD 委托逻辑，为 ApplicationService 提供部门领域的统一操作入口。

## Requirements

### Requirement: Dept DomainService CRUD 委托

SysDeptService SHALL 注入 ISysDeptRepository，并将所有 CRUD 操作委托转发至 Repository。

#### Scenario: 创建部门
- **WHEN** 调用 `SysDeptService.create(entity)`
- **THEN** 委托 `ISysDeptRepository.create(entity)` 并返回 `SysDeptId`

#### Scenario: 更新部门
- **WHEN** 调用 `SysDeptService.update(entity)`
- **THEN** 委托 `ISysDeptRepository.update(entity)` 并返回 `Boolean`

#### Scenario: 分页查询部门
- **WHEN** 调用 `SysDeptService.queryPage(query)`
- **THEN** 委托 `ISysDeptRepository.queryPage(query)` 并返回 `PagingInfo<SysDeptEntity>`

#### Scenario: 按ID查询部门
- **WHEN** 调用 `SysDeptService.queryById(id)`
- **THEN** 委托 `ISysDeptRepository.queryById(id)` 并返回 `SysDeptEntity`

#### Scenario: 批量删除部门
- **WHEN** 调用 `SysDeptService.removeBatchByIds(ids)`
- **THEN** 委托 `ISysDeptRepository.removeBatchByIds(ids)` 并返回 `Boolean`
