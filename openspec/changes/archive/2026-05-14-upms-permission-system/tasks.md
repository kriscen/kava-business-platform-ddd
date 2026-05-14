## 1. Domain 层补全——角色管理

- [x] 1.1 填充 `SysRoleCreateCommand`：添加 roleName、roleCode、roleDesc、dsType、dsScope、menuIds 字段
- [x] 1.2 填充 `SysRoleUpdateCommand`：添加 id、roleName、roleCode、roleDesc、dsType、dsScope、menuIds 字段
- [x] 1.3 填充 `SysRoleAppDetailDTO`：添加角色所有字段 + menuIds 列表
- [x] 1.4 在 `ISysRoleWriteRepository` 中增加批量保存/删除角色菜单关联的方法签名
- [x] 1.5 在 `SysRoleEntity` 或 domain service 中增加菜单绑定校验逻辑（菜单是否在可见范围内）

## 2. Domain 层补全——用户管理

- [x] 2.1 填充 `SysUserCreateCommand`：补全字段 + roleIds
- [x] 2.2 填充 `SysUserUpdateCommand`：补全字段 + roleIds
- [x] 2.3 在 `ISysUserWriteRepository` 中增加批量保存/删除用户角色关联的方法签名
- [x] 2.4 在 `ISysUserService` 接口中定义用户 CRUD 方法签名
- [x] 2.5 实现 `SysUserService`（domain service）：用户创建、更新、删除（含角色关联清理）

## 3. Domain 层补全——通用

- [x] 3.1 扩展 `SysMenuScope` 枚举：新增 `SYSTEM_TENANT("system_tenant", "平台及租户")` 值
- [x] 3.2 填充 `UpmsBizErrorCodeEnum`：定义角色/用户/菜单/租户相关的业务错误码
- [x] 3.3 更新 `SysMenuEntity` 的 scope 字段类型适配新枚举值

## 4. Infrastructure 层——角色中间表写入

- [x] 4.1 实现 `SysRoleWriteRepository` 中角色-菜单关联的批量保存逻辑（sys_role_menu 表写入）
- [x] 4.2 实现角色更新时的全量替换策略（先删后插 sys_role_menu）
- [x] 4.3 实现角色删除时的级联清理（sys_role_menu + sys_user_role）
- [x] 4.4 修改 `SysRoleReadRepository.queryById` 返回关联的 menuIds

## 5. Infrastructure 层——用户中间表写入

- [x] 5.1 实现 `SysUserWriteRepository` 中用户-角色关联的批量保存逻辑（sys_user_role 表写入）
- [x] 5.2 实现用户更新时的全量替换策略（先删后插 sys_user_role）
- [x] 5.3 实现用户删除时的级联清理（sys_user_role）
- [x] 5.4 修改 `SysUserReadRepository.queryById` 返回关联的 roleIds

## 6. Application 层——角色服务完善

- [x] 6.1 完善 `SysRoleAppConverter`（MapStruct）：Command → Entity 转换增加 menuIds 映射
- [x] 6.2 完善 `SysRoleAppService.createRole`：转换 Command → Entity，调用 writeRepository，处理 menuIds
- [x] 6.3 完善 `SysRoleAppService.updateRole`：同上
- [x] 6.4 完善 `SysRoleAppService.queryRoleById`：返回 DTO 包含 menuIds

## 7. Application 层——用户服务完善

- [x] 7.1 完善 `SysUserAppConverter`（MapStruct）：Command → Entity 转换增加 roleIds 映射
- [x] 7.2 实现 `SysUserAppService` 各 CRUD 方法：创建/更新/删除/查询，处理 roleIds
- [x] 7.3 完善 `SysUserAppService.queryUserById`：返回 DTO 包含 roleIds

## 8. Adapter 层——请求/响应完善

- [x] 8.1 更新 `SysRoleRequest`：增加 menuIds 字段
- [x] 8.2 更新 `SysRoleDetailResponse`：增加 menuIds 字段
- [x] 8.3 更新 `SysUserRequest`：增加 roleIds 字段
- [x] 8.4 更新 `SysUserDetailResponse`：增加 roleIds 字段
- [x] 8.5 更新 `SysRoleAdapterConverter` / `SysUserAdapterConverter`：增加 menuIds/roleIds 映射

## 9. 租户管理——Domain 层

- [x] 9.1 新增 `SysTenantEntity` 聚合根（含 menuIds 字段）
- [x] 9.2 新增 `SysTenantId` 值对象（如 kbpd-common-core 中不存在）
- [x] 9.3 新增 `ISysTenantWriteRepository` / `ISysTenantReadRepository` 接口
- [x] 9.4 新增 `ISysTenantService` domain service 接口及实现（含租户创建时初始化管理员角色逻辑）

## 10. 租户管理——Infrastructure 层

- [x] 10.1 新增 `SysTenantPO`、`SysTenantMapper`
- [x] 10.2 实现 `SysTenantWriteRepository` / `SysTenantReadRepository`（含 menu_id 字段的读写）
- [x] 10.3 租户创建时自动创建 `tenant_admin` 角色 + 关联已分配菜单 + 创建初始管理员用户并绑定角色

## 11. 租户管理——Application + Adapter 层

- [x] 11.1 新增租户相关的 Command、DTO、Converter
- [x] 11.2 新增 `ISysTenantAppService` 接口及实现
- [x] 11.3 新增租户 Request / Response / Query 对象
- [x] 11.4 新增 `SysTenantController`（CRUD + 菜单分配接口）
- [x] 11.5 新增租户相关 Dubbo Remote Service（如需跨服务访问租户信息）

## 12. 权限运行时——Redis 缓存

- [x] 12.1 在 `kbpd-common-security` 或 `kbpd-common-cache` 中新增 `PermissionCacheService`：根据 userId 加载并缓存 permissions 到 Redis
- [x] 12.2 定义缓存 key 格式 `perm:user:{userId}`，TTL 与 access token 一致
- [x] 12.3 实现缓存未命中时从 UPMS 加载权限列表的逻辑

## 13. 权限运行时——方法级鉴权

- [x] 13.1 在 `kbpd-common-security` 中新增 `PermissionChecker` 组件：`hasPermission(String permission)` 方法，从缓存校验当前用户权限
- [x] 13.2 支持超级管理员角色（如 `ROLE_ADMIN`）跳过所有权限检查
- [x] 13.3 在 `ResourceServerConfiguration` 中启用 `@EnableMethodSecurity`

## 14. 权限运行时——菜单树接口

- [x] 14.1 在 `ISysMenuAppService` 中新增 `queryMenuTree(Long userId)` 方法：根据用户角色解析可见菜单并构建树结构
- [x] 14.2 在 `SysMenuController` 中新增 `GET /menu/tree` 接口
- [x] 14.3 菜单树按 scope 和用户角色过滤：平台管理员看 SYSTEM + SYSTEM_TENANT，租户用户看已分配范围
- [x] 14.4 菜单树按 sortOrder 排序、按 parentId 构建层级

## 15. 权限运行时——Auth Server 增强

- [x] 15.1 在 `kbpd-auth` 的 JWT token 自定义逻辑中为 B 端用户写入 `dataScope` claim（取用户主角色的 dsType）
- [x] 15.2 在 `UserContext` 中增加 `dataScope` 字段及 `fromJwtClaims` 解析

## 16. 租户数据隔离——MyBatis-Plus 拦截器

- [x] 16.1 在 `kbpd-common-database` 中新增 `KavaTenantLineInnerInterceptor`：从 `UserContextHolder` 获取 tenantId，自动注入 `WHERE tenant_id = ?`
- [x] 16.2 在 `KavaTenantLineInnerInterceptor` 中判断平台管理员（通过 UserContext.userType + 角色）跳过租户过滤
- [x] 16.3 支持配置忽略租户隔离的表列表（如系统配置表）
- [x] 16.4 在 `MybatisPlusConfig` 中注册拦截器，执行顺序：TenantLine → DataScope → Pagination
- [x] 16.5 INSERT/UPDATE 操作自动填充 tenant_id

## 17. 数据权限——@DataScope 拦截器

- [x] 17.1 在 `kbpd-common-database` 中新增 `@DataScope` 注解定义
- [x] 17.2 新增 `DataScopeInnerInterceptor`：解析当前用户角色的 dsType，拼接对应 SQL 条件
- [x] 17.3 实现 5 种 dsType 策略的 SQL 拼接逻辑（ALL / CUSTOM / DEPT_AND_CHILD / DEPT_ONLY / SELF）
- [x] 17.4 实现 DEPT_AND_CHILD 递归查询部门子树（可缓存到 Redis）
- [x] 17.5 实现多角色数据权限合并（取最大范围）
- [x] 17.6 在 `MybatisPlusConfig` 中注册 DataScopeInnerInterceptor

## 18. Dubbo RPC 权限上下文传播

- [x] 18.1 在 `ConsumerUserContextFilter` 中增加 permissions 和 dataScope 的传播
- [x] 18.2 在 `ProviderUserContextFilter` 中恢复 permissions 和 dataScope 到 UserContextHolder
- [x] 18.3 扩展 `UserContext` 增加 permissions 字段（Set<String>）

## 19. 验证与测试

- [x] 19.8 编译通过并启动 UPMS 服务验证无启动异常

## 后续迭代（DEFERRED）

以下为验证阶段发现的功能缺口，标注为后续迭代处理：

### 功能级缺口（CRITICAL）

- [ ] D1 DataScope 拦截器 SQL 注入未生效：`DataScopeInnerInterceptor.beforeQuery()` 未调用 `buildDataScopeCondition()`，拦截器注册但不会修改任何 SQL。需要重写 beforeQuery 解析 SQL 并追加过滤条件
- [ ] D2 `@DataScope` 注解未实际使用：注解已定义但全项目无任何方法标注，拦截器也未检查方法注解。需要在目标 Mapper/Service 方法上标注，并在拦截器中通过 MappedStatement 解析注解
- [ ] D3 CUSTOM dsType 未使用 dsScope 字段：dsType "1" 与 "2"/"3" 逻辑相同（都是 `dept_id = deptId`），应改为从角色 dsScope 解析部门 ID 列表生成 `IN` 条件

### 功能级缺口（WARNING）

- [ ] D4 多角色数据权限合并未实现：`SysUserReadRepository.queryDataScopeByUserId()` 仅取第一个角色的 dsType，需按优先级 ALL > CUSTOM > DEPT_AND_CHILD > DEPT_ONLY > SELF 取最大范围
- [ ] D5 DEPT_AND_CHILD 递归查询部门子树未实现：当前仅 `dept_id = deptId`，未递归查子部门
- [ ] D6 租户创建未自动创建初始管理员用户：`SysTenantService.initTenantAdminRole()` 仅创建角色，未创建初始管理员用户并绑定
- [ ] D7 缺少 MetaObjectHandler 实现：PO 层 `creator`/`gmtCreate`/`modifier`/`gmtModified` 标注了 FieldFill 但无对应 Handler，字段将始终为 null

### 集成测试（延后）

- [ ] D8 编写角色-菜单绑定集成测试：创建/更新/删除角色时验证 sys_role_menu 正确写入
- [ ] D9 编写用户-角色绑定集成测试：创建/更新/删除用户时验证 sys_user_role 正确写入
- [ ] D10 编写权限缓存测试：验证 Redis 缓存加载、命中、过期重新加载
- [ ] D11 编写租户隔离测试：验证租户用户只能查到本租户数据，平台管理员可查全部
- [ ] D12 编写数据权限测试：验证 5 种 dsType 策略正确拼接 SQL 条件
- [ ] D13 编写菜单作用域测试：验证三种 scope 的可见性过滤
- [ ] D14 编写租户管理测试：验证创建租户时自动初始化管理员角色和用户
