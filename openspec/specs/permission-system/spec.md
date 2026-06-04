# Capability: permission-system

## Purpose

定义完整的权限体系：菜单作用域控制（可见性）→ 功能权限（Redis 缓存 + 方法级鉴权）→ 数据权限（行级过滤），覆盖权限从定义到运行时校验的全链路。

## Requirements

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

### Requirement: 权限缓存加载

系统 SHALL 在用户首次访问需要权限校验的接口时，从数据库加载该用户的所有权限字符串并缓存到 Redis。

#### Scenario: 首次访问时缓存权限
- **WHEN** 用户首次访问需要权限校验的接口
- **THEN** 系统查询该用户通过 User → UserRole → Role → RoleMenu → Menu 解析出的所有 permission 字符串
- **AND** 将结果缓存到 Redis，key 为 `perm:user:{userId}`，value 为权限字符串 Set
- **AND** 缓存 TTL 与 access token 有效期一致

#### Scenario: 后续访问使用缓存
- **WHEN** 用户再次访问需要权限校验的接口，且缓存未过期
- **THEN** 系统直接从 Redis 读取权限列表，不查库

#### Scenario: 缓存过期后重新加载
- **WHEN** 用户访问接口时 Redis 缓存已过期
- **THEN** 系统重新从数据库加载权限并更新缓存

### Requirement: 方法级权限校验

系统 SHALL 支持通过 `@PreAuthorize` 注解在方法级别校验用户是否拥有指定权限。

#### Scenario: 用户拥有所需权限
- **WHEN** 方法标注了 `@PreAuthorize("@permissionChecker.hasPermission('sys_user_add'))` 且用户拥有 `sys_user_add` 权限
- **THEN** 方法正常执行

#### Scenario: 用户缺少所需权限
- **WHEN** 方法标注了 `@PreAuthorize("@permissionChecker.hasPermission('sys_user_add'))` 且用户不拥有 `sys_user_add` 权限
- **THEN** 系统返回 403 Forbidden 错误

#### Scenario: 超级管理员跳过权限检查
- **WHEN** 用户拥有超级管理员角色（如 `ROLE_ADMIN`）
- **THEN** 跳过所有权限检查，等同于拥有所有权限

### Requirement: 登录后返回用户菜单树

登录认证成功后，系统 SHALL 返回当前用户可见的菜单树，仅包含用户有权限访问的菜单项。菜单可见性基于 App 购买关系计算。

#### Scenario: B 端用户登录后获取菜单树
- **WHEN** B 端用户登录成功并请求菜单树接口
- **THEN** 系统根据用户的角色 → 菜单关联，构建菜单树结构
- **AND** 只返回用户角色关联的菜单项（目录 + 菜单 + 按钮）
- **AND** 菜单树按 sortOrder 排序，按 parentId 构建层级

#### Scenario: 平台管理员获取菜单树
- **WHEN** 平台管理员请求菜单树接口
- **THEN** 返回所有 level 为 PLATFORM 的菜单
- **AND** 菜单树按 sortOrder 排序，按 parentId 构建层级

#### Scenario: 租户管理员获取菜单树
- **WHEN** 租户管理员请求菜单树接口
- **THEN** 返回该租户已购 App 的 TENANT 菜单并集，且在用户角色关联范围内
- **AND** 菜单树按 sortOrder 排序，按 parentId 构建层级

### Requirement: DataScope 注解标记

系统 SHALL 提供 `@DataScope` 注解，用于标记需要数据权限行级过滤的 Mapper 方法或 Service 方法。

#### Scenario: 方法标记了 @DataScope
- **WHEN** Mapper 方法标注了 `@DataScope`
- **THEN** 执行 SQL 时，拦截器根据当前用户角色的 dsType 策略自动追加行级过滤条件

#### Scenario: 方法未标记 @DataScope
- **WHEN** Mapper 方法未标注 `@DataScope`
- **THEN** 不追加数据权限过滤条件（仅租户隔离仍然生效）

### Requirement: 全部数据权限（dsType = ALL）

当角色的数据权限策略为 ALL 时，系统 SHALL 不追加额外的行级过滤条件。

#### Scenario: 角色拥有全部数据权限
- **WHEN** 用户主角色的 dsType 为 ALL
- **THEN** 查询不追加分组或人员过滤条件
- **AND** 在租户范围内可查看所有数据

### Requirement: 自定义数据权限（dsType = CUSTOM）

当角色的数据权限策略为 CUSTOM 时，系统 SHALL 根据角色的 dsScope 字段追加分组过滤条件。

#### Scenario: 角色拥有自定义数据权限
- **WHEN** 用户主角色的 dsType 为 CUSTOM，dsScope 包含分组 ID 列表 "1,2,3"
- **THEN** 查询追加 `WHERE group_id IN (1, 2, 3)`

### Requirement: 本分组及以下数据权限（dsType = GROUP_AND_CHILD）

当角色的数据权限策略为 GROUP_AND_CHILD 时，系统 SHALL 追加用户所在分组及其递归子分组的过滤条件。

#### Scenario: 角色拥有本分组及以下权限
- **WHEN** 用户主角色的 dsType 为 GROUP_AND_CHILD，用户所在分组 ID 为 1
- **THEN** 系统递归查询分组 ID 1 的所有子分组
- **AND** 查询追加 `WHERE group_id IN (1, 子分组1, 子分组2, ...)`

### Requirement: 本分组数据权限（dsType = GROUP_ONLY）

当角色的数据权限策略为 GROUP_ONLY 时，系统 SHALL 仅追加用户所在分组的过滤条件。

#### Scenario: 角色拥有本分组权限
- **WHEN** 用户主角色的 dsType 为 GROUP_ONLY，用户所在分组 ID 为 1
- **THEN** 查询追加 `WHERE group_id = 1`

### Requirement: 仅本人数据权限（dsType = SELF）

当角色的数据权限策略为 SELF 时，系统 SHALL 追加当前用户 ID 的过滤条件。

#### Scenario: 角色拥有仅本人权限
- **WHEN** 用户主角色的 dsType 为 SELF，用户 ID 为 100
- **THEN** 查询追加 `WHERE creator = 100`（或 `WHERE user_id = 100`，根据表结构决定）

### Requirement: 多角色数据权限合并

当用户拥有多个角色时，系统 SHALL 取权限范围最大的角色策略。

#### Scenario: 用户拥有多个角色
- **WHEN** 用户同时拥有 dsType=SELF 和 dsType=GROUP_ONLY 的两个角色
- **THEN** 系统采用 GROUP_ONLY（权限范围更大）策略
- **AND** dsType 优先级排序为：ALL > CUSTOM > GROUP_AND_CHILD > GROUP_ONLY > SELF

### Requirement: 平台管理员跳过数据权限过滤

平台管理员执行查询时，系统 SHALL 不追加数据权限过滤条件。

#### Scenario: 平台管理员查询带 @DataScope 的数据
- **WHEN** 平台管理员执行标记了 `@DataScope` 的查询
- **THEN** 不追加任何分组/人员过滤条件

### Requirement: Dubbo RPC 调用传播权限上下文

跨服务 Dubbo 调用时，权限上下文 SHALL 通过 RPC attachment 传播。

#### Scenario: UPMS 调用其他服务
- **WHEN** UPMS 服务通过 Dubbo 调用其他微服务
- **THEN** 当前用户的 userId、tenantId、roles、permissions 通过 RPC attachment 传播到被调用方
- **AND** 被调用方可以执行权限校验

### Requirement: 角色查询租户隔离
角色分页查询时，系统 SHALL 根据当前用户的 tenantId 过滤结果，确保租户间数据隔离。

#### Scenario: 租户管理员查询角色列表
- **WHEN** 租户管理员请求角色分页列表
- **THEN** 系统从 Security Context 获取当前用户的 tenantId
- **AND** 仅返回属于该租户的角色记录

#### Scenario: 平台管理员查询角色列表
- **WHEN** 平台管理员（无 tenantId）请求角色分页列表
- **THEN** 系统返回所有租户的角色记录，不按租户过滤

### Requirement: roleCode 租户内唯一
同一租户内，角色标识 roleCode SHALL 保持唯一。不同租户间允许 roleCode 重复。

#### Scenario: 创建角色时 roleCode 重复
- **WHEN** 管理员创建角色，roleCode 与同租户下已有角色重复
- **THEN** 系统拒绝创建并返回唯一性冲突错误

#### Scenario: 更新角色时 roleCode 与其他角色冲突
- **WHEN** 管理员更新角色 roleCode，新 roleCode 与同租户下其他角色重复
- **THEN** 系统拒绝更新并返回唯一性冲突错误

#### Scenario: 不同租户 roleCode 相同
- **WHEN** 租户 A 和租户 B 各创建一个 roleCode 为 "admin" 的角色
- **THEN** 两个角色均可正常创建，互不影响

### Requirement: 角色列表 DTO 完整返回
角色分页查询接口 SHALL 返回完整的列表展示字段。

#### Scenario: 查询角色列表
- **WHEN** 管理员请求角色分页列表
- **THEN** 每条记录包含 id、roleName、roleCode、roleDesc、dsType、dsScope、gmtCreate、gmtModified

### Requirement: API 层审计字段清理
角色相关的 API 请求和响应对象 SHALL 遵循字段最小暴露原则。

#### Scenario: 创建/更新角色请求不含审计字段
- **WHEN** 管理员提交角色创建或更新请求
- **THEN** Request 对象仅包含业务字段（roleName、roleCode、roleDesc、dsType、dsScope、menuIds、id）
- **AND** 不包含 creator、gmtCreate、modifier、gmtModified、delFlag

#### Scenario: 角色详情响应仅保留时间审计字段
- **WHEN** 管理员查询角色详情
- **THEN** Response 包含 gmtCreate、gmtModified
- **AND** 不包含 creator、modifier、delFlag

#### Scenario: 角色列表响应不含删除标识
- **WHEN** 管理员查询角色列表
- **THEN** 列表 Response 不包含 delFlag 字段

### Requirement: 依赖注入使用构造器注入
Controller 层和 Infrastructure 层 SHALL 使用构造器注入，禁止使用 @Resource 或 @Autowired 字段注入。

#### Scenario: 角色相关 Bean 的依赖注入方式
- **WHEN** 容器初始化 SysRoleController 和 SysRoleReadRepository/SysRoleWriteRepository
- **THEN** 所有依赖通过构造器参数注入
- **AND** 不使用 @Resource 或 @Autowired 字段注入
