# Capability: unified-user-context

## Purpose

定义统一的用户上下文（UserContext）机制，封装从 JWT token 提取的用户信息，并通过 SecurityUtils、UserContextHolder 和 Dubbo Filter 实现在 HTTP 请求和 RPC 调用链中的自动传播与恢复。

## Requirements

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
