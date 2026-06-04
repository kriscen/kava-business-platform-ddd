## MODIFIED Requirements

### Requirement: 菜单作用域两值枚举

系统 SHALL 将 `SysMenuLevel` 枚举定义为两个值：PLATFORM（平台管理级菜单）、TENANT（租户级菜单）。取代原有的三值 `SysMenuScope`。

#### Scenario: 创建平台级菜单
- **WHEN** 平台管理员创建菜单并设置 level 为 PLATFORM
- **THEN** 该菜单仅对平台管理员可见
- **AND** 租户管理员无法看到此菜单

#### Scenario: 创建租户级菜单
- **WHEN** 创建菜单并设置 level 为 TENANT
- **THEN** 该菜单仅对拥有包含此菜单的 App 的租户管理员可见
- **AND** 平台管理员无法看到此菜单

### Requirement: 菜单可见性基于 App 购买关系过滤

查询菜单时，系统 SHALL 根据当前用户身份和租户的 App 购买关系过滤可见菜单。

#### Scenario: 平台管理员查询菜单列表
- **WHEN** 平台管理员查询菜单列表
- **THEN** 返回所有 level 为 PLATFORM 的菜单
- **AND** 不返回 level 为 TENANT 的菜单

#### Scenario: 租户管理员查询菜单列表
- **WHEN** 租户管理员查询菜单列表
- **THEN** 返回该租户已购 App（sys_tenant_app status=ACTIVE）通过 sys_app_menu 关联的 TENANT 菜单并集（自动去重）
- **AND** 不返回 level 为 PLATFORM 的菜单
- **AND** 不返回其他租户的菜单

### Requirement: 角色绑定时校验菜单 level 和 App 购买范围

角色绑定菜单时，系统 SHALL 校验菜单 level 是否匹配角色上下文，且租户角色只能绑定已购 App 范围内的菜单。

#### Scenario: 平台管理员绑定菜单
- **WHEN** 平台管理员创建/更新角色
- **THEN** 可绑定 level 为 PLATFORM 的菜单
- **AND** 不能绑定 level 为 TENANT 的菜单

#### Scenario: 租户管理员绑定已购 App 范围内的菜单
- **WHEN** 租户管理员创建/更新角色，绑定属于 (kava-base ∪ 已购App) 范围内的 TENANT 菜单
- **THEN** 系统接受操作

#### Scenario: 租户管理员绑定未购 App 的菜单
- **WHEN** 租户管理员创建/更新角色，尝试绑定未购买 App 包含的 TENANT 菜单
- **THEN** 系统拒绝操作并返回权限错误

### Requirement: 登录后返回用户菜单树

登录认证成功后，系统 SHALL 返回当前用户可见的菜单树，仅包含用户有权限访问的菜单项。菜单可见性基于 App 购买关系计算。

#### Scenario: 平台管理员获取菜单树
- **WHEN** 平台管理员请求菜单树接口
- **THEN** 返回所有 level 为 PLATFORM 的菜单
- **AND** 菜单树按 sortOrder 排序，按 parentId 构建层级

#### Scenario: 租户管理员获取菜单树
- **WHEN** 租户管理员请求菜单树接口
- **THEN** 返回该租户已购 App 的 TENANT 菜单并集，且在用户角色关联范围内
- **AND** 菜单树按 sortOrder 排序，按 parentId 构建层级

## REMOVED Requirements

### Requirement: 菜单作用域三值枚举
**Reason**: 三值 scope 替换为两值 level（PLATFORM/TENANT），共享菜单拆成两条记录而非使用 SYSTEM_TENANT。
**Migration**: SYSTEM→PLATFORM，TENANT→TENANT，SYSTEM_TENANT 拆分为两条记录。

### Requirement: 租户菜单可见性过滤（旧版）
**Reason**: 旧的可见性基于 sys_tenant.menu_id 逗号字符串分配，替换为基于 sys_tenant_app 购买关系的声明式查询。
**Migration**: 移除 sys_tenant.menu_id，改用 sys_tenant_app + sys_app_menu 查询链路。
