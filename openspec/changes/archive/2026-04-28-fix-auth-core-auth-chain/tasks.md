## 1. 补全 UPMS API DTO 和 RPC 实现

- [x] 1.1 在 `SysUserDTO` 中增加 username、password、deptId、enabled 字段
- [x] 1.2 在 `RemoteUserService.findByUsername()` 中接通真实查询：调用 `SysUserAppService` 查询用户基本信息+权限，组装完整 `SysUserDTO` 返回；用户不存在时返回 null

## 2. 补全 Member API DTO 和 RPC 实现

- [x] 2.1 在 `MemberInfoDTO` 中增加 mobile、password、enabled 字段
- [x] 2.2 检查 Member 的 `RemoteMemberService` 实现类：如果也是 stub，接通真实查询；如果 Member 模块查询功能尚未完成，先标记 TODO 并确保返回 null（Auth 侧会抛 UsernameNotFoundException）

## 3. 修复 Auth 模块 PwdUserDetailsService

- [x] 3.1 修改 `PwdUserDetailsService.loadUserByUsername()`：B端分支使用真实 `SysUserDTO` 构造 `SysUserDetails`（id、username、password、deptId、enabled、permissions → GrantedAuthority）
- [x] 3.2 C端分支使用真实 `MemberInfoDTO` 构造 `MemberDetails`（id、mobile、password、enabled）
- [x] 3.3 两个分支均在 DTO 为 null 时抛出 `UsernameNotFoundException`，移除所有硬编码用户

## 4. 修复 TenantAwareAuthenticationFilter 租户安全

- [x] 4.1 注入 `RegisteredClientRepository` 到 `TenantAwareAuthenticationFilter`
- [x] 4.2 修改 `attemptAuthentication()`：用 clientId 查询 `RegisteredClient`，从 `ClientSettings` 提取可信的 tenantId 和 userType，用服务端值构造 `ExtendAuthenticationToken`
- [x] 4.3 处理 clientId 无效的情况（Client 不存在时抛 `AuthenticationServiceException`）

## 5. 修复 DBRegisteredClientRepository.findById()

- [x] 5.1 修改 `findById(id)` 方法，委托调用 `findByClientId(id)`

## 6. 验证

- [x] 6.1 编译全项目（`mvn clean install -DskipTests`），确保无编译错误
- [x] 6.2 验证 UPMS 模块可独立启动，`RemoteUserService.findByUsername()` 能返回完整 DTO
- [x] 6.3 验证 Auth 模块可独立启动，`TenantAwareAuthenticationFilter` 能从 ClientSettings 获取可信参数
- [x] 6.4 验证 `PwdUserDetailsService` 不再包含硬编码用户，所有 TODO 已移除
