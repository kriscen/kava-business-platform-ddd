## MODIFIED Requirements

### Requirement: Menu DomainService CRUD 委托

SysMenuService SHALL 注入 ISysMenuRepository，并将 CRUD 操作委托转发至 Repository。创建和修改操作 SHALL 在委托前执行 pid 校验。删除操作 SHALL 在委托前执行子菜单和角色引用检查。

Application 层的 `SysMenuCreateCommand` 和 `SysMenuUpdateCommand` SHALL 包含完整的字段映射（name、permission、pid、icon、path、component、visible、sortOrder、menuType、keepAlive、embedded、**level**），使 Controller 层的 Create/Update 请求能正确传递到 Domain 层。

#### Scenario: 创建菜单 — Command 字段完整传递
- **WHEN** 前端调用 `POST /api/v1/sys/menu/` 提交 name、permission、pid、icon、path、component、visible、sortOrder、menuType、keepAlive、embedded、**level** 字段
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

## REMOVED Requirements

### Requirement: 菜单作用域三值枚举
**Reason**: scope 三值模型（SYSTEM/TENANT/SYSTEM_TENANT）替换为 level 两值模型（PLATFORM/TENANT）。共享菜单拆成两条记录。
**Migration**: SysMenuScope 枚举替换为 SysMenuLevel 枚举，SYSTEM→PLATFORM，TENANT→TENANT，SYSTEM_TENANT 记录拆分为两条。
