# Capability: authentication-flow

## Purpose

定义 Auth 模块中完整的认证流程：多租户感知的安全参数获取 → 基于真实用户数据的密码认证 → JWT access token claims 增强。确保认证链路端到端的安全性和正确性。

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

`SysUserDTO`（UPMS API）MUST 包含以下字段：id、username、password（哈希值）、deptId、lockFlag、permissions、roles。其中 password 为数据库中存储的密码哈希（带前缀如 `{bcrypt}`），lockFlag 反映用户的锁定状态（`"0"` 正常，`"1"` 锁定，与数据库 `lock_flag` 字段对齐）。

#### Scenario: SysUserDTO 包含认证所需的全部信息
- **WHEN** UPMS 的 `RemoteUserService.findByUsername(tenantId, username)` 被调用
- **AND** 数据库中存在该用户
- **THEN** 返回的 `SysUserDTO` MUST 包含 id、username、password（哈希值）、deptId、lockFlag、permissions、roles 字段
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
- **THEN** MUST 使用 DTO 中的 id、username、password、deptId、enabled 构造 `SysUserDetails`
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
