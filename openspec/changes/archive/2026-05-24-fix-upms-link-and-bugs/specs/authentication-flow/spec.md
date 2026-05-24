## MODIFIED Requirements

### Requirement: PwdUserDetailsService 使用真实 DTO 构造 UserDetails

`PwdUserDetailsService` MUST 使用 RPC 返回的真实 DTO 构造 `SysUserDetails` 或 `MemberDetails`，不得使用硬编码用户信息。

`RemoteUserService.loginByPwd(tenantId, username, password)` MUST 实现真实的用户查询和密码校验：调用 `ISysUserReadRepository.findByUsername(username)` 查询用户，使用 `BCryptPasswordEncoder.matches()` 校验密码，校验成功返回用户 ID，校验失败抛出 `AuthenticationServiceException`，用户不存在返回 null。

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

#### Scenario: loginByPwd 密码校验成功
- **WHEN** `RemoteUserService.loginByPwd(tenantId, username, password)` 被调用
- **AND** 数据库中存在该用户且密码匹配
- **THEN** MUST 返回用户 ID（Long 类型）

#### Scenario: loginByPwd 密码校验失败
- **WHEN** `RemoteUserService.loginByPwd(tenantId, username, password)` 被调用
- **AND** 数据库中存在该用户但密码不匹配
- **THEN** MUST 抛出 `AuthenticationServiceException`

#### Scenario: loginByPwd 用户不存在
- **WHEN** `RemoteUserService.loginByPwd(tenantId, username, password)` 被调用
- **AND** 数据库中不存在该用户
- **THEN** MUST 返回 null

#### Scenario: 用户不存在时抛出 UsernameNotFoundException
- **WHEN** `loadUserByUsername` 被调用且 RPC 返回 null
- **THEN** MUST 抛出 `UsernameNotFoundException`

#### Scenario: 未知 userType 时抛出 UsernameNotFoundException
- **WHEN** `loadUserByUsername` 被调用且 userType 既不是 TO_B 也不是 TO_C
- **THEN** MUST 抛出 `UsernameNotFoundException`
