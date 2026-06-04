## MODIFIED Requirements

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

### Requirement: Dubbo Consumer 端自动传播 UserContext

系统 MUST 提供 Dubbo Consumer Filter，在发起 RPC 调用前，从当前线程的 UserContextHolder 提取 UserContext，写入 Dubbo RpcContext 的 attachment 中。

#### Scenario: Consumer 端调用时传播上下文
- **WHEN** 服务 A 通过 Dubbo 调用服务 B
- **AND** 服务 A 当前线程中存在 UserContext
- **THEN** RpcContext attachment 中 MUST 包含 tenantId、userType、userId（或 memberId）、username、roles
- **AND** 若用户为 B端，attachment 中包含 groupId
