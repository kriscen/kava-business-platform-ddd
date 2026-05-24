## Tasks

### Task 1: 新增错误码
- [x] 新增 4 个菜单校验错误码

在 `UpmsBizErrorCodeEnum` 中新增 4 个菜单校验错误码：

- `MENU_PID_SELF_REFERENCE`（A00303）：菜单父节点不能为自身
- `MENU_PID_CIRCULAR`（A00304）：菜单父节点不能形成循环引用
- `MENU_HAS_CHILDREN`（A00305）：菜单存在子菜单，无法删除
- `MENU_REFERENCED_BY_ROLE`（A00306）：菜单已被角色引用，无法删除

**文件**: `kbpd-upms/kbpd-upms-types/src/main/java/com/kava/kbpd/upms/types/exception/UpmsBizErrorCodeEnum.java`

---

### Task 2: ISysMenuRepository 新增查询方法
- [x] 新增 queryByPid 和 existsRoleReference 方法

在 `ISysMenuRepository` 接口中新增：
- `List<SysMenuEntity> queryByPid(SysMenuId pid)` — 查询指定父节点的直接子菜单
- `boolean existsRoleReference(SysMenuId menuId)` — 检查菜单是否被角色引用

**文件**: `kbpd-upms/kbpd-upms-domain/src/main/java/com/kava/kbpd/upms/domain/repository/ISysMenuRepository.java`

---

### Task 3: SysMenuRepository 实现新增方法
- [x] 实现 queryByPid 和 existsRoleReference

在 `SysMenuRepository` 中：
- 实现 `queryByPid`：通过 `SysMenuMapper` 查询 `pid = ?` 的记录
- 实现 `existsRoleReference`：注入 `SysRoleMenuMapper`，查询 `sys_role_menu` 表中 `menu_id = ?` 的 count > 0

**文件**: `kbpd-upms/kbpd-upms-infrastructure/src/main/java/com/kava/kbpd/upms/infrastructure/adapter/repository/SysMenuRepository.java`

---

### Task 4: SysMenuService 补充校验逻辑
- [x] 实现 validatePid、validateBeforeDelete，修改 create/update/removeBatchByIds

在 `SysMenuService` 中实现：

1. **extracted `validatePid` 方法**：
   - pid 为 null 时跳过（顶级菜单）
   - pid 等于 id 时抛出 MENU_PID_SELF_REFERENCE
   - 调用 `queryAll()` 构建父子关系图，沿 pid 链向上遍历检查是否会回到当前节点，若会则抛出 MENU_PID_CIRCULAR

2. **extracted `validateBeforeDelete` 方法**：
   - 对每个待删除 id 调用 `queryByPid`，结果非空则抛出 MENU_HAS_CHILDREN
   - 对每个待删除 id 调用 `existsRoleReference`，结果为 true 则抛出 MENU_REFERENCED_BY_ROLE

3. **修改 create 方法**：sortOrder 为 null 时设为 0，然后调用 `validatePid`，最后委托 repository
4. **修改 update 方法**：调用 `validatePid`，然后委托 repository
5. **修改 removeBatchByIds 方法**：先调用 `validateBeforeDelete`，然后委托 repository

**文件**: `kbpd-upms/kbpd-upms-domain/src/main/java/com/kava/kbpd/upms/domain/service/impl/SysMenuService.java`

---

### Task 5: 验证
- [x] 编译通过并验证

- 编译通过：`mvn clean compile -pl kbpd-upms -am`
- 手动检查：创建/修改/删除菜单时，非法 pid 和有引用的删除应正确抛出异常
