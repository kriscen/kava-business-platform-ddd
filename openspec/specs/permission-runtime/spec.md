## Purpose

定义权限校验运行时：Redis 缓存用户权限、方法级 `@PreAuthorize` 鉴权、登录后菜单树过滤、Dubbo RPC 权限上下文传播。

## Requirements

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

#### Scenario: 用户拥有超级管理员角色
- **WHEN** 用户拥有超级管理员角色（如 `ROLE_ADMIN`）
- **THEN** 跳过所有权限检查，等同于拥有所有权限

### Requirement: 登录后返回用户菜单树
登录认证成功后，系统 SHALL 返回当前用户可见的菜单树，仅包含用户有权限访问的菜单项。

#### Scenario: B 端用户登录后获取菜单树
- **WHEN** B 端用户登录成功并请求菜单树接口
- **THEN** 系统根据用户的角色 → 菜单关联，构建菜单树结构
- **AND** 只返回用户角色关联的菜单项（目录 + 菜单 + 按钮）
- **AND** 菜单树按 sortOrder 排序，按 parentId 构建层级

#### Scenario: 平台管理员获取菜单树
- **WHEN** 平台管理员请求菜单树接口
- **THEN** 返回所有 scope 为 system 和 system_tenant 的菜单

#### Scenario: 租户管理员获取菜单树
- **WHEN** 租户管理员请求菜单树接口
- **THEN** 返回租户已分配的 system_tenant 菜单 + 租户自建的 tenant 菜单，且在用户角色关联范围内

### Requirement: Dubbo RPC 调用传播权限上下文
跨服务 Dubbo 调用时，权限上下文 SHALL 通过 RPC attachment 传播。

#### Scenario: UPMS 调用其他服务
- **WHEN** UPMS 服务通过 Dubbo 调用其他微服务
- **THEN** 当前用户的 userId、tenantId、roles、permissions 通过 RPC attachment 传播到被调用方
- **AND** 被调用方可以执行权限校验
