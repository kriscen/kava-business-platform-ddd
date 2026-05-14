## Why

UPMS 模块具备 RBAC 基础数据模型（User → Role → Menu），但角色-菜单绑定、用户-角色绑定的写入链路断裂（Command 空壳、中间表未写入），权限校验运行时完全缺失，数据权限（部门级行过滤）未实现，平台管理员与租户管理员的权限边界未落地。需要系统性地补全权限体系，使 Kava 平台具备可用的 B 端权限管理能力。

## What Changes

- **补全角色管理 CRUD**：填充 `SysRoleCreateCommand`/`UpdateCommand`、`SysRoleAppDetailDTO`，实现角色-菜单关联表（`sys_role_menu`）的写入逻辑
- **补全用户-角色绑定**：实现用户创建/更新时对 `sys_user_role` 中间表的写入
- **实现权限校验运行时**：服务端 Redis 缓存用户权限字符串，方法级 `@PreAuthorize` 鉴权，登录后返回按权限过滤的菜单树
- **实现多租户数据隔离**：MyBatis-Plus 租户拦截器自动注入 `tenant_id` 过滤条件，平台管理员跳过租户隔离
- **实现数据权限（行级过滤）**：`@DataScope` 注解标记需要行级过滤的查询，根据角色的 `dsType` 策略自动拼接部门/人员过滤条件
- **完善菜单作用域模型**：`SysMenuScope` 从两值（system/tenant）扩展为三值（system / tenant / system_tenant），控制菜单对不同管理员的可见性
- **新增租户管理 CRUD**：平台管理员可创建/编辑租户并分配可用菜单范围
- **新租户初始化**：创建租户时自动生成必备角色（如租户管理员）
- **补充 UPMS 错误码**：填充 `UpmsBizErrorCodeEnum`，覆盖权限相关业务异常

## Capabilities

### New Capabilities

- `role-menu-binding`: 角色与菜单的绑定管理——创建/更新角色时关联菜单，支持中间表的写入和删除
- `user-role-binding`: 用户与角色的绑定管理——创建/更新用户时关联角色，支持中间表的写入和删除
- `permission-runtime`: 权限校验运行时——Redis 缓存用户权限、方法级 `@PreAuthorize` 鉴权、登录后菜单树过滤
- `tenant-data-isolation`: 多租户数据隔离——MyBatis-Plus 拦截器自动注入 `tenant_id` 条件，平台管理员跳过
- `data-scope-filter`: 数据权限行级过滤——`@DataScope` 注解驱动，按角色 dsType 策略过滤部门/人员数据
- `menu-scope`: 菜单作用域控制——三值枚举（system / tenant / system_tenant）控制菜单可见性
- `tenant-management`: 租户管理 CRUD——平台管理员管理租户、分配菜单、初始化必备角色

### Modified Capabilities

（无现有 capability 需要修改，均为新增）

## Impact

- **kbpd-upms**: 所有层（domain/application/infrastructure/adapter）均需修改，填充空壳类并新增权限相关逻辑
- **kbpd-common/kbpd-common-core**: `SysMenuScope` 枚举扩展，可能新增 `@DataScope` 注解定义
- **kbpd-common/kbpd-common-security**: 新增权限缓存工具类、`@PreAuthorize` 集成支持
- **kbpd-common/kbpd-common-database**: 新增租户拦截器、数据权限拦截器
- **kbpd-common/kbpd-common-cache**: 可能需要权限缓存相关配置
- **kbpd-auth**: JWT token 需要写入 `dataScope` claim（`JwtClaimConstants.DATA_SCOPE` 已定义但未使用）
- **docs/01-sql/kbpd-upms.sql**: `sys_menu.scope` 字段值域调整，可能需要新增索引
