# Capability: real-user-authentication

## Purpose

定义 Auth 模块中基于真实用户数据（B端 SysUser / C端 Member）进行密码认证的完整流程。涵盖 DTO 字段要求和 PwdUserDetailsService 的行为规范。

## Requirements

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
- **THEN** MUST 使用 DTO 中的 id、username、password、deptId、enabled、permissions 构造 `SysUserDetails`
- **AND** password 字段 MUST 为 DTO 中的原始哈希值，不得修改或替换
- **AND** permissions 列表 MUST 转换为 `GrantedAuthority` 集合

#### Scenario: C端用户认证 — 使用真实 MemberInfoDTO
- **WHEN** `loadUserByUsername(username, tenantId, userType)` 被调用且 userType 为 TO_C
- **AND** `remoteMemberService.findMemberByMobile(tenantId, username)` 返回非 null 的 `MemberInfoDTO`
- **THEN** MUST 使用 DTO 中的 id、mobile、password、enabled 构造 `MemberDetails`
- **AND** password 字段 MUST 为 DTO 中的原始哈希值

#### Scenario: 用户不存在时抛出 UsernameNotFoundException
- **WHEN** `loadUserByUsername` 被调用
- **AND** RPC 返回 null
- **THEN** MUST 抛出 `UsernameNotFoundException`

#### Scenario: 未知 userType 时抛出 UsernameNotFoundException
- **WHEN** `loadUserByUsername` 被调用且 userType 既不是 TO_B 也不是 TO_C
- **THEN** MUST 抛出 `UsernameNotFoundException`
