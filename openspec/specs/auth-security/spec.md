# Capability: auth-security

## Purpose

定义完整的认证鉴权链路：多租户感知的安全参数获取 → 基于真实用户数据的密码认证 → JWT claims 增强 → UserContext 传播 → 权限校验（功能权限 + 数据权限），覆盖从登录到运行时鉴权的全链路。

## Requirements

### Requirement: TenantAwareAuthenticationFilter 从 ClientSettings 获取可信租户参数

`TenantAwareAuthenticationFilter` MUST 通过 `RegisteredClientRepository` 查询已注册的 OAuth2 Client，从 `ClientSettings` 中获取可信的 tenantId 和 userType，不得信任前端表单提交的 tenantId 和 userType 参数。

#### Scenario: 使用 ClientSettings 中的可信参数构造认证 Token
- **WHEN** 用户提交登录表单（POST /oauth2/login），包含 username、password、clientId
- **THEN** Filter MUST 使用 clientId 调用 `RegisteredClientRepository.findByClientId(clientId)` 查询 Client 配置
- **AND** 从 ClientSettings 中提取 tenantId 和 userType
- **AND** 使用服务端获取的 tenantId 和 userType 构造 `ExtendAuthenticationToken`

#### Scenario: clientId 对应的 Client 不存在
- **WHEN** 用户提交登录表单中的 clientId 在已注册客户端中不存在
- **THEN** MUST 抛出 `AuthenticationServiceException`
- **AND** 不得继续认证流程

#### Scenario: 前端篡改 tenantId 不影响认证结果
- **WHEN** 攻击者修改前端表单中的 tenantId 为其他租户的 ID
- **THEN** 认证流程 MUST 忽略前端提交的 tenantId
- **AND** 使用 ClientSettings 中的 tenantId 完成认证

#### Scenario: 前端篡改 userType 不影响认证结果
- **WHEN** 攻击者修改前端表单中的 userType（如将 C端用户改为 B端）
- **THEN** 认证流程 MUST 忽略前端提交的 userType
- **AND** 使用 ClientSettings 中的 userType 完成认证

### Requirement: DBRegisteredClientRepository.findById 不抛异常

`DBRegisteredClientRepository.findById(id)` MUST 返回有效的 `RegisteredClient`，不得抛出 `UnsupportedOperationException`。

#### Scenario: findById 返回正确的 RegisteredClient
- **WHEN** Spring Authorization Server 内部调用 `findById(id)`
- **THEN** MUST 返回与 `findByClientId(id)` 相同的结果
- **AND** 不得抛出任何异常

### Requirement: SysUserDTO 携带构造 UserDetails 所需的全部字段

`SysUserDTO`（UPMS API）MUST 包含以下字段：id、username、password（哈希值）、groupId、lockFlag、permissions、roles。其中 password 为数据库中存储的密码哈希（带前缀如 `{bcrypt}`），lockFlag 反映用户的锁定状态（`"0"` 正常，`"1"` 锁定，与数据库 `lock_flag` 字段对齐）。

#### Scenario: SysUserDTO 包含认证所需的全部信息
- **WHEN** UPMS 的 `RemoteUserService.findByUsername(tenantId, username)` 被调用
- **AND** 数据库中存在该用户
- **THEN** 返回的 `SysUserDTO` MUST 包含 id、username、password（哈希值）、groupId、lockFlag、permissions、roles 字段
- **AND** password 字段为数据库中存储的完整哈希值（含编码前缀）

#### Scenario: 用户不存在时返回 null
- **WHEN** UPMS 的 `RemoteUserService.findByUsername(tenantId, username)` 被调用
- **AND** 数据库中不存在该用户
- **THEN** MUST 返回 null

### Requirement: MemberInfoDTO 携带构造 UserDetails 所需的全部字段

`MemberInfoDTO`（Member API）MUST 包含以下字段：id、mobile、password（哈希值）、enabled、permissions、roles。其中 mobile 作为会员的登录标识，password 为数据库中的密码哈希。

#### Scenario: MemberInfoDTO 包含认证所需的全部信息
- **WHEN** Member 的 `RemoteMemberService.findMemberByMobile(tenantId, mobile)` 被调用
- **AND** 数据库中存在该会员
- **THEN** 返回的 `MemberInfoDTO` MUST 包含 id、mobile、password（哈希值）、enabled、permissions、roles 字段

#### Scenario: 会员不存在时返回 null
- **WHEN** Member 的 `RemoteMemberService.findMemberByMobile(tenantId, mobile)` 被调用
- **AND** 数据库中不存在该会员
- **THEN** MUST 返回 null

### Requirement: PwdUserDetailsService 使用真实 DTO 构造 UserDetails

`PwdUserDetailsService` MUST 使用 RPC 返回的真实 DTO 构造 `SysUserDetails` 或 `MemberDetails`，不得使用硬编码用户信息。

#### Scenario: B端用户认证 — 使用真实 SysUserDTO
- **WHEN** `loadUserByUsername(username, tenantId, userType)` 被调用且 userType 为 TO_B
- **AND** `remoteUserService.findByUsername(tenantId, username)` 返回非 null 的 `SysUserDTO`
- **THEN** MUST 使用 DTO 中的 id、username、password、groupId、enabled 构造 `SysUserDetails`
- **AND** password 字段 MUST 为 DTO 中的原始哈希值，不得修改或替换
- **AND** DTO 中的 roles 列表 MUST 转换为 `GrantedAuthority` 集合

#### Scenario: C端用户认证 — 使用真实 MemberInfoDTO
- **WHEN** `loadUserByUsername(username, tenantId, userType)` 被调用且 userType 为 TO_C
- **AND** `remoteMemberService.findMemberByMobile(tenantId, username)` 返回非 null 的 `MemberInfoDTO`
- **THEN** MUST 使用 DTO 中的 id、mobile、password、enabled 构造 `MemberDetails`
- **AND** password 字段 MUST 为 DTO 中的原始哈希值

#### Scenario: 用户不存在时抛出 UsernameNotFoundException
- **WHEN** `loadUserByUsername` 被调用且 RPC 返回 null
- **THEN** MUST 抛出 `UsernameNotFoundException`

#### Scenario: 未知 userType 时抛出 UsernameNotFoundException
- **WHEN** `loadUserByUsername` 被调用且 userType 既不是 TO_B 也不是 TO_C
- **THEN** MUST 抛出 `UsernameNotFoundException`

### Requirement: JWT 必须包含 tenantId claim

JWT access token 中 MUST 包含 `tenantId` claim，值为当前用户的租户 ID。

#### Scenario: B端用户 JWT 包含 tenantId
- **WHEN** B端用户（SysUserDetails）成功认证并生成 JWT access token
- **THEN** JWT payload MUST 包含 claim `tenantId`，值为认证时从 ClientSettings 获取的租户 ID

#### Scenario: C端用户 JWT 包含 tenantId
- **WHEN** C端用户（MemberDetails）成功认证并生成 JWT access token
- **THEN** JWT payload MUST 包含 claim `tenantId`，值为认证时从 ClientSettings 获取的租户 ID

### Requirement: JWT 必须包含 userType claim

JWT access token 中 MUST 包含 `userType` claim，值为用户类型代码（"1" = B端，"2" = C端）。

#### Scenario: B端用户 JWT 包含 userType
- **WHEN** B端用户成功认证并生成 JWT access token
- **THEN** JWT payload MUST 包含 claim `userType`，值为 "1"

#### Scenario: C端用户 JWT 包含 userType
- **WHEN** C端用户成功认证并生成 JWT access token
- **THEN** JWT payload MUST 包含 claim `userType`，值为 "2"

### Requirement: JWT 使用 roles 替代 authorities

JWT access token 中 MUST 使用 `roles` claim 存放角色代码集合，MUST NOT 使用 `authorities` claim。

#### Scenario: B端用户 JWT 包含 roles
- **WHEN** B端用户成功认证并生成 JWT access token
- **THEN** JWT payload MUST 包含 claim `roles`，值为角色代码集合（如 ["ROLE_ADMIN"]）
- **AND** MUST NOT 包含 `authorities` claim

#### Scenario: C端用户 JWT 包含空 roles
- **WHEN** C端用户成功认证并生成 JWT access token
- **THEN** JWT payload MUST 包含 claim `roles`，值为空数组 []
- **AND** MUST NOT 包含 `authorities` claim

### Requirement: JwtClaimConstants 新增常量

`JwtClaimConstants` MUST 新增以下常量：`TENANT_ID = "tenantId"`、`USER_TYPE = "userType"`、`ROLES = "roles"`。MUST 保留已有常量不变。

#### Scenario: 常量可用于 claims 读写
- **WHEN** 代码需要读取或写入 JWT 中的 tenantId、userType、roles 字段
- **THEN** MUST 通过 `JwtClaimConstants.TENANT_ID`、`JwtClaimConstants.USER_TYPE`、`JwtClaimConstants.ROLES` 引用

### Requirement: UserContext 统一封装 JWT claims

系统 MUST 提供 `UserContext` 类，封装从 JWT token 提取的完整用户上下文。UserContext MUST 包含以下字段：

- `tenantId` (Long) — 租户 ID，始终存在
- `userType` (String) — 用户类型（"1" = B端，"2" = C端），始终存在
- `userId` (Long) — B端用户 ID，仅 userType="1" 时有值
- `memberId` (Long) — C端会员 ID，仅 userType="2" 时有值
- `username` (String) — 用户名，始终存在
- `groupId` (Long) — 分组 ID，仅 B端用户有值
- `roles` (Set<String>) — 角色集合，始终存在（C端可能为空）

User 聚合根 MUST 使用 `SysUserId` 值对象作为 ID 类型，与其他子域（Role/Menu/Group/Area 等）保持一致的 ID 值对象模式。

#### Scenario: 从 B端 JWT 构造 UserContext
- **WHEN** JwtAuthenticationToken 的 claims 包含 userType="1"
- **THEN** 构造的 UserContext 中 userId、groupId MUST 为非 null
- **AND** memberId MUST 为 null

#### Scenario: 从 C端 JWT 构造 UserContext
- **WHEN** JwtAuthenticationToken 的 claims 包含 userType="2"
- **THEN** 构造的 UserContext 中 memberId MUST 为非 null
- **AND** userId、groupId MUST 为 null

#### Scenario: SysUserId 值对象在 User 聚合根中使用
- **WHEN** User 聚合根（SysUserEntity）需要引用自身 ID
- **THEN** MUST 使用 `SysUserId` 值对象而非裸 Long 类型
- **AND** SysUserId 值对象 MUST 包含 `Long getValue()` 方法
- **AND** SysUserId MUST 提供 `of(Long value)` 静态工厂方法

### Requirement: SecurityUtils 提供 getUserContext 方法

`SecurityUtils` MUST 提供 `getUserContext()` 静态方法，从当前 SecurityContext 的 JwtAuthenticationToken 中构造并返回 UserContext。

#### Scenario: 已认证请求中获取 UserContext
- **WHEN** 当前 SecurityContext 中存在有效的 JwtAuthenticationToken
- **THEN** `getUserContext()` MUST 返回非 null 的 UserContext
- **AND** UserContext 的字段值与 JWT claims 一致

#### Scenario: 未认证请求中获取 UserContext
- **WHEN** 当前 SecurityContext 中无认证信息
- **THEN** `getUserContext()` MUST 返回 null

### Requirement: ResourceServer JWT converter 自动构造 UserContext

`ResourceServerConfiguration` 的 JWT converter MUST 在 JWT 验证成功后，将 UserContext 设置到线程上下文中（通过 UserContextHolder），使 SecurityUtils.getUserContext() 可用。

#### Scenario: JWT 验证后 UserContext 可用
- **WHEN** 下游服务收到有效的 JWT 请求并完成验证
- **THEN** UserContextHolder 中 MUST 存在有效的 UserContext
- **AND** `SecurityUtils.getUserContext()` 返回该 UserContext

### Requirement: Dubbo Consumer 端自动传播 UserContext

系统 MUST 提供 Dubbo Consumer Filter，在发起 RPC 调用前，从当前线程的 UserContextHolder 提取 UserContext，写入 Dubbo RpcContext 的 attachment 中。

#### Scenario: Consumer 端调用时传播上下文
- **WHEN** 服务 A 通过 Dubbo 调用服务 B
- **AND** 服务 A 当前线程中存在 UserContext
- **THEN** RpcContext attachment 中 MUST 包含 tenantId、userType、userId（或 memberId）、username、roles
- **AND** 若用户为 B端，attachment 中包含 groupId

#### Scenario: Consumer 端无 UserContext 时不传播
- **WHEN** 服务 A 通过 Dubbo 调用服务 B
- **AND** 服务 A 当前线程中无 UserContext（如定时任务触发的调用）
- **THEN** 不写入 attachment，调用正常执行不报错

### Requirement: Dubbo Provider 端自动恢复 UserContext

系统 MUST 提供 Dubbo Provider Filter，在收到 RPC 调用时，从 RpcContext attachment 中提取用户上下文，构造 UserContext 并设置到 UserContextHolder。

#### Scenario: Provider 端恢复上下文
- **WHEN** 服务 B 收到来自服务 A 的 Dubbo 调用
- **AND** RpcContext attachment 中包含用户上下文信息
- **THEN** UserContextHolder 中 MUST 存在有效的 UserContext
- **AND** 服务 B 的业务代码可通过 `SecurityUtils.getUserContext()` 获取

#### Scenario: Provider 端调用完成后清理上下文
- **WHEN** Dubbo Provider 处理完 RPC 调用
- **THEN** MUST 清理 UserContextHolder，防止线程池复用导致上下文泄漏

### Requirement: Member 服务启用 ResourceServer 认证

Member 服务的 `MemberApplication` MUST 添加 `@EnableResourceServer` 注解，激活 JWT token 验证能力。

#### Scenario: Member 服务验证 JWT token
- **WHEN** Member 服务收到携带有效 JWT token 的请求
- **THEN** Spring Security MUST 成功验证 token 并构造 SecurityContext
- **AND** `SecurityUtils.getUserContext()` 返回有效的 UserContext

#### Scenario: Member 服务拒绝无效 token
- **WHEN** Member 服务收到携带无效或过期 JWT token 的请求
- **THEN** MUST 返回 HTTP 401 Unauthorized

### Requirement: Member 服务配置 JWK Set URI

Member 服务的 `application-dev.yml` MUST 配置 `spring.security.oauth2.resource-server.jwt.jwk-set-uri` 指向 Auth 服务的 JWKS endpoint（`http://localhost:8600/auth/oauth2/jwks`）。

#### Scenario: 配置正确的 JWKS endpoint
- **WHEN** Member 服务启动
- **THEN** NimbusJwtDecoder MUST 从 `http://localhost:8600/auth/oauth2/jwks` 获取公钥用于 JWT 验签

### Requirement: Member 服务提供 Dev 环境安全配置

Member 服务 MUST 提供 `DevSecurityConfig`（`@Profile("dev")`），在开发环境下允许所有请求不经过认证。

#### Scenario: dev 环境跳过认证
- **WHEN** Member 服务以 dev profile 运行
- **THEN** 所有请求 MUST 不经过 JWT 验证，直接放行

#### Scenario: 非 dev 环境启用认证
- **WHEN** Member 服务以非 dev profile 运行
- **THEN** ResourceServerConfiguration MUST 生效，要求 JWT 认证

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
