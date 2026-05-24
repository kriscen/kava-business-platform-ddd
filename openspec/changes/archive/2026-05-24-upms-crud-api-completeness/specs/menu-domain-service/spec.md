## MODIFIED Requirements

### Requirement: Menu DomainService CRUD 委托

SysMenuService SHALL 注入 ISysMenuRepository，并将 CRUD 操作委托转发至 Repository。创建和修改操作 SHALL 在委托前执行 pid 校验。删除操作 SHALL 在委托前执行子菜单和角色引用检查。

**变更说明**：Application 层的 `SysMenuCreateCommand` 和 `SysMenuUpdateCommand` 当前为空壳（无字段），需补全字段映射，使 Controller 层的 Create/Update 请求能正确传递到 Domain 层。

#### Scenario: 创建菜单 — Command 字段完整传递
- **WHEN** 前端调用 `POST /api/v1/sys/menu/` 提交 name、permission、pid、icon、path、component、visible、sortOrder、menuType、keepAlive、embedded、scope 字段
- **THEN** SysMenuCreateCommand SHALL 包含所有上述字段
- **AND** AdapterConverter 将 Request 映射为 Command
- **AND** AppConverter 将 Command 映射为 SysMenuEntity
- **AND** DomainService 执行校验后委托 Repository 持久化

#### Scenario: 更新菜单 — Command 字段完整传递
- **WHEN** 前端调用 `PUT /api/v1/sys/menu/{id}` 提交 name、permission 等字段
- **THEN** SysMenuUpdateCommand SHALL 包含所有上述字段加 id
- **AND** 映射链路完整传递到 Domain 层

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

#### Scenario: 更新菜单
- **WHEN** 调用 `SysMenuService.update(entity)`
- **THEN** 执行与 create 相同的 pid 校验规则，通过后委托 `ISysMenuRepository.update(entity)` 并返回 `Boolean`

#### Scenario: 批量删除菜单 — 校验通过
- **WHEN** 调用 `SysMenuService.removeBatchByIds(ids)` 且所有菜单均无子菜单且无角色引用
- **THEN** 委托 `ISysMenuRepository.removeBatchByIds(ids)` 并返回 `Boolean`
