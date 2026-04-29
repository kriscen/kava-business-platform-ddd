## ADDED Requirements

### Requirement: UserContext 统一封装 JWT claims

系统 MUST 提供 `UserContext` 类，封装从 JWT token 提取的完整用户上下文。UserContext MUST 包含以下字段：

- `tenantId` (Long) — 租户 ID，始终存在
- `userType` (String) — 用户类型（"1" = B端，"2" = C端），始终存在
- `userId` (Long) — B端用户 ID，仅 userType="1" 时有值
- `memberId` (Long) — C端会员 ID，仅 userType="2" 时有值
- `username` (String) — 用户名，始终存在
- `deptId` (Long) — 部门 ID，仅 B端用户有值
- `roles` (Set<String>) — 角色集合，始终存在（C端可能为空）

#### Scenario: 从 B端 JWT 构造 UserContext
- **WHEN** JwtAuthenticationToken 的 claims 包含 userType="1"
- **THEN** 构造的 UserContext 中 userId、deptId MUST 为非 null
- **AND** memberId MUST 为 null

#### Scenario: 从 C端 JWT 构造 UserContext
- **WHEN** JwtAuthenticationToken 的 claims 包含 userType="2"
- **THEN** 构造的 UserContext 中 memberId MUST 为非 null
- **AND** userId、deptId MUST 为 null

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
