## Why

Auth 模块的 OAuth2 基础架构已搭建完成（Spring Authorization Server、Redis 存储、JWT 定制），但核心认证链路处于不可用状态：`PwdUserDetailsService` 使用硬编码用户（任何用户名 + 密码 `123456` 即可登录），`TenantAwareAuthenticationFilter` 不校验前端提交的 tenantId/userType，存在跨租户冒充和用户类型混淆的安全风险。此外 `DBRegisteredClientRepository.findById()` 直接抛异常，可能在运行时导致 500。需要接通真实认证链路，使 auth 模块真正可用。

## What Changes

- 补全 `SysUserDTO`（UPMS API）字段：增加 username、password、deptId、enabled，使其携带构造 `UserDetails` 所需的全部信息
- 补全 `MemberInfoDTO`（Member API）字段：增加 mobile、password、enabled
- 接通 UPMS `RemoteUserService.findByUsername()` 真实实现：查询数据库并组装完整 DTO 返回
- 接通 Member `RemoteMemberService.findMemberByMobile()` 真实实现（如果 Member 侧 stub）
- 修复 Auth `PwdUserDetailsService`：使用 RPC 返回的真实 DTO 构造 `SysUserDetails` / `MemberDetails`，移除硬编码
- 修复 Auth `TenantAwareAuthenticationFilter`：从 `RegisteredClientRepository` 查询 Client 配置，使用 `ClientSettings` 中存储的可信 tenantId/userType，不再信任前端提交的参数
- 修复 Auth `DBRegisteredClientRepository.findById()`：委托给 `findByClientId()`（因为 id 即 clientId）

## Capabilities

### New Capabilities

- `real-user-authentication`: 使用 UPMS/Member 真实用户数据完成 OAuth2 认证，替代硬编码用户
- `tenant-aware-auth-security`: 基于已注册 OAuth2 Client 配置的可信租户校验，消除前端参数篡改风险

### Modified Capabilities

（无现有 specs 需要修改）

## Impact

**kbpd-upms-api**:
- `SysUserDTO.java` — 增加 username、password、deptId、enabled 字段

**kbpd-upms-adapter**:
- `RemoteUserService.java` — findByUsername() 接通真实数据库查询

**kbpd-member-api**:
- `MemberInfoDTO.java` — 增加 mobile、password、enabled 字段

**kbpd-member-adapter**（如存在）:
- `RemoteMemberService.java` — findMemberByMobile() 接通真实实现

**kbpd-auth**:
- `PwdUserDetailsService.java` — 使用真实 DTO 构造 UserDetails
- `TenantAwareAuthenticationFilter.java` — 注入 RegisteredClientRepository，从 ClientSettings 获取可信参数
- `DBRegisteredClientRepository.java` — findById() 委托 findByClientId()
