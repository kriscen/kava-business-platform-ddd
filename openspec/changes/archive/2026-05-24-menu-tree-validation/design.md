## Context

SysMenuEntity 使用 `pid` 字段实现树形结构（邻接表模型），但 SysMenuService 当前是纯 CRUD 薄壳，没有对 pid 进行任何校验。sys_role_menu 关系表记录角色与菜单的绑定，但删除菜单时不会检查是否有角色引用。已有 UpmsBizErrorCodeEnum 中 MENU 相关错误码仅有 MENU_NOT_FOUND 和 MENU_SCOPE_INVALID。

## Goals / Non-Goals

**Goals:**
- 创建/修改菜单时防止 pid 自引用和循环引用
- 删除菜单时防止破坏树完整性（有子节点）和引用完整性（有角色绑定）
- sortOrder 为 null 时自动填充默认值

**Non-Goals:**
- 不做菜单层级深度限制（如最大 N 层）
- 不做批量移动子树的级联更新
- 不做菜单类型（目录/菜单/按钮）与 pid 的关联校验
- 不修改 SysRoleService 的现有逻辑

## Decisions

### 1. 环检测策略：全量查询 + 内存遍历

**决策**：通过 `queryAll()` 拉取当前租户下所有菜单，在内存中构建父子关系图，遍历检测环。

**Because** 菜单数量通常在百级别（权限表不会很大），全量加载成本可接受。相比 SQL 递归查询（需要数据库支持 CTE），内存方案更通用、更易测试。

**替代方案**：SQL 递归 CTE 查询 ancestor 链。需要 MySQL 8+ 且测试更困难，对当前规模过度设计。

### 2. 角色-菜单引用检查：在 MenuRepository 中注入 SysRoleMenuMapper

**决策**：给 ISysMenuRepository 新增 `existsRoleReference(SysMenuId menuId)` 方法，在 SysMenuRepository 实现中直接注入 SysRoleMenuMapper 查询 `sys_role_menu` 表的 count。

**Because** sys_role_menu 是基础设施层的关联表，没有独立的 domain 实体。从 Menu 侧查询"有没有角色引用我"是菜单删除的前置校验，放在 MenuRepository 最自然。Domain 层的 ISysMenuRepository 只声明 boolean 返回值，不暴露底层表结构。

**替代方案**：在 SysMenuService 中注入 ISysRoleWriteRepository 调用其方法。但 ISysRoleWriteRepository 当前没有按 menuId 查询的方法，且跨聚合读应通过 DomainService，增加了不必要的跨域耦合。

### 3. 子菜单检查：ISysMenuRepository.queryByPid

**决策**：给 ISysMenuRepository 新增 `queryByPid(SysMenuId pid)` 方法，返回指定父节点下的直接子菜单列表。删除前调用检查列表是否为空。

**Because** 只需检查直接子节点即可，无需递归。如果允许级联删除会需要递归查询，但当前设计是"有子节点就拒绝删除"。

### 4. 校验逻辑放在 DomainService

**决策**：树校验和引用检查逻辑放在 SysMenuService（DomainService）中，不在 Entity 中。

**Because** 环检测需要知道整棵树的全貌（需要 Repository 查询），这超出了单个 Entity 的职责范围。DomainService 天然适合编排跨 Repo 校验。

## Risks / Trade-offs

- **[queryAll 性能]** → 菜单表在百级别，可接受。如果未来菜单增长到万级需改为增量查询方案，但权限表不会到这个量级。
- **[并发安全]** → 创建时检查 pid 不成环、删除时检查无子节点，存在竞态窗口（两个请求同时删除同一父节点的不同子节点后，父节点变成可删）。Because 当前是单实例部署且操作频率极低，不加分布式锁。如果将来需要，可在 AppService 层加 `@Transactional` + 乐观锁。
