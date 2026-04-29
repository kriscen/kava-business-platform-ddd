## ADDED Requirements

### Requirement: JWT 必须包含 tenantId claim

JWT access token 中 MUST 包含 `tenantId` claim，值为当前用户的租户 ID。租户信息从 `ExtendAuthenticationToken` 的 tenantId 字段获取。

#### Scenario: B端用户 JWT 包含 tenantId
- **WHEN** B端用户（SysUserDetails）成功认证并生成 JWT access token
- **THEN** JWT payload MUST 包含 claim `tenantId`，值为认证时从 ClientSettings 获取的租户 ID

#### Scenario: C端用户 JWT 包含 tenantId
- **WHEN** C端用户（MemberDetails）成功认证并生成 JWT access token
- **THEN** JWT payload MUST 包含 claim `tenantId`，值为认证时从 ClientSettings 获取的租户 ID

### Requirement: JWT 必须包含 userType claim

JWT access token 中 MUST 包含 `userType` claim，值为用户类型代码（"1" = B端，"2" = C端）。用户类型从 `ExtendAuthenticationToken` 的 userType 字段获取。

#### Scenario: B端用户 JWT 包含 userType
- **WHEN** B端用户成功认证并生成 JWT access token
- **THEN** JWT payload MUST 包含 claim `userType`，值为 "1"

#### Scenario: C端用户 JWT 包含 userType
- **WHEN** C端用户成功认证并生成 JWT access token
- **THEN** JWT payload MUST 包含 claim `userType`，值为 "2"

### Requirement: JWT 使用 roles 替代 authorities

JWT access token 中 MUST 使用 `roles` claim 存放角色代码集合（如 ["ROLE_ADMIN", "ROLE_USER"]），MUST NOT 使用 `authorities` claim。`roles` 仅包含角色，不包含细粒度权限。

#### Scenario: B端用户 JWT 包含 roles
- **WHEN** B端用户成功认证并生成 JWT access token
- **THEN** JWT payload MUST 包含 claim `roles`，值为角色代码集合（如 ["ROLE_ADMIN"]）
- **AND** MUST NOT 包含 `authorities` claim

#### Scenario: C端用户 JWT 包含空 roles
- **WHEN** C端用户成功认证并生成 JWT access token
- **THEN** JWT payload MUST 包含 claim `roles`，值为空数组 []
- **AND** MUST NOT 包含 `authorities` claim

### Requirement: JwtClaimConstants 新增常量

`JwtClaimConstants` MUST 新增以下常量：`TENANT_ID = "tenantId"`、`USER_TYPE = "userType"`、`ROLES = "roles"`。MUST 保留已有常量不变（USER_ID、USERNAME、DEPT_ID、DATA_SCOPE、AUTHORITIES、MEMBER_ID）。

#### Scenario: 常量可用于 claims 读写
- **WHEN** 代码需要读取或写入 JWT 中的 tenantId、userType、roles 字段
- **THEN** MUST 通过 `JwtClaimConstants.TENANT_ID`、`JwtClaimConstants.USER_TYPE`、`JwtClaimConstants.ROLES` 引用
