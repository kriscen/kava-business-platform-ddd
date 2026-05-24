## Why

SysMenuService 当前是纯 CRUD 薄壳，没有树形结构校验。用户可以创建循环引用的菜单树（把自己或子节点设为父节点），也可以删除仍有子菜单或已被角色引用的菜单，导致权限数据不一致。Menu 是权限体系骨架，需要基本的树完整性保障才能支撑 MVP。

## What Changes

- 菜单创建/修改时校验 pid 合法性：不允许自引用、不允许形成环
- 菜单删除时检查是否有子菜单，有则拒绝
- 菜单删除时检查是否有角色引用该菜单（通过 sys_role_menu 表），有则拒绝
- sortOrder 为 null 时设置默认值 0
- 新增对应错误码：MENU_PID_SELF_REFERENCE、MENU_PID_CIRCULAR、MENU_HAS_CHILDREN、MENU_REFERENCED_BY_ROLE
- ISysMenuRepository 新增 queryByPid(SysMenuId pid) 方法用于查询子菜单
- ISysMenuService 新增 existsRoleReference(SysMenuId id) 校验方法（需注入 RoleWriteRepository 或通过 MenuRepository 直接查 sys_role_menu）

## Capabilities

### New Capabilities

无新增 capability。

### Modified Capabilities

- `menu-domain-service`: 在现有 CRUD 委托基础上，补充树形结构校验和删除安全检查的业务规则。现有 spec 仅描述了透传委托行为，需要新增 tree-validation 相关 requirement。

## Impact

- `kbpd-upms/kbpd-upms-domain/src/main/java/.../domain/service/impl/SysMenuService.java` — 核心改动，补充校验逻辑
- `kbpd-upms/kbpd-upms-domain/src/main/java/.../domain/repository/ISysMenuRepository.java` — 新增 queryByPid 方法
- `kbpd-upms/kbpd-upms-infrastructure/src/main/java/.../adapter/repository/SysMenuRepository.java` — 实现 queryByPid
- `kbpd-upms/kbpd-upms-infrastructure/src/main/java/.../dao/SysRoleMenuMapper.java` — 可能需要新增 countByMenuId 方法（或在 MenuRepository 中直接注入 SysRoleMenuMapper）
- `kbpd-upms/kbpd-upms-types/src/main/java/.../exception/UpmsBizErrorCodeEnum.java` — 新增 4 个错误码
