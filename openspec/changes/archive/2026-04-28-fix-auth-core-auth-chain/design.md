## Context

Auth 模块基于 Spring Authorization Server 搭建了完整的 OAuth2 授权服务器架构，包括双 Security Filter Chain、Redis 存储层、JWT 定制、Jackson 序列化等。但认证的核心路径（用户验证）使用硬编码，租户/用户类型参数直接信任前端提交，客户端仓库部分方法不可用。UPMS 和 Member 的 RPC 接口返回的 DTO 缺少关键字段（password、enabled 等），无法构造完整的 UserDetails。

当前依赖链：

```
前端登录表单 → TenantAwareAuthenticationFilter → CustomerAuthenticationProvider → PwdUserDetailsService
                                                                      ↓
                                                          remoteUserService.findByUsername() → SysUserDTO（字段不足）
                                                          remoteMemberService.findMemberByMobile() → MemberInfoDTO（字段不足）
```

## Goals / Non-Goals

**Goals:**
- 接通 Auth 模块的真实用户认证链路，使用 UPMS/Member 数据库中的用户数据完成 OAuth2 认证
- 消除 TenantAwareAuthenticationFilter 中的前端参数篡改风险
- 修复 DBRegisteredClientRepository.findById() 的运行时异常
- 确保 DTO 设计合理，Auth 模块能拿到构造 UserDetails 所需的全部信息

**Non-Goals:**
- 不实现 Token Introspection / Revocation 端点（后续独立 change）
- 不实现第三方登录（微信/GitHub 等）
- 不实现动态登录页/授权页
- 不修改 UPMS/Member 的 domain 层业务逻辑，仅补全 DTO 字段和 RPC 实现
- 不改变 OAuth2 Client 的管理方式（仍由 UPMS 数据库管理，auth 只读）

## Decisions

### Decision 1: DTO 字段补全方案 — 扩展现有 DTO 而非新建

**选择**：在现有 `SysUserDTO` 和 `MemberInfoDTO` 上增加字段。

**Because** 这些 DTO 本身就是为 RPC 认证场景设计的，只是初始版本字段不全。新建 DTO 会增加类型转换成本和维护负担。新增字段（username、password、deptId、enabled）都是认证必需的，不存在"部分场景不需要"的情况。

**Alternative**：新建专用 `SysUserAuthDTO` 仅包含认证所需字段。被否决，因为会导致同一接口两个 DTO，增加复杂性。

### Decision 2: TenantAwareAuthenticationFilter — 从 ClientSettings 获取可信参数

**选择**：Filter 注入 `RegisteredClientRepository`，用 `clientId` 查询已注册 Client，从 `ClientSettings` 中取出 `tenantId` 和 `userType`，用服务端可信值构造 `ExtendAuthenticationToken`。

**Because** OAuth2 Client 的配置（包括 tenantId、userType）已在 `DBRegisteredClientRepository.findByClientId()` 中写入 ClientSettings，这是服务端管理的可信数据源。前端提交的 tenantId/userType 仅作为表单展示用，不应参与认证逻辑。

```
修改前：前端 tenantId/userType → ExtendAuthenticationToken → 认证（可篡改）
修改后：前端 clientId → 查 ClientSettings → 服务端 tenantId/userType → ExtendAuthenticationToken（不可篡改）
```

**Alternative**：在 Filter 中额外调用 UPMS RPC 校验 tenantId/userType/clientId 三者匹配。被否决，因为增加了不必要的 RPC 调用，且 ClientSettings 已经是可信数据源。

### Decision 3: DBRegisteredClientRepository.findById() — 委托给 findByClientId()

**选择**：`findById(id)` 直接调用 `findByClientId(id)`。

**Because** 在 `DBRegisteredClientRepository.findByClientId()` 中，`RegisteredClient` 的 id 就是用的 `clientDetails.getClientId()`（见第55行 `withId(clientDetails.getClientId())`），所以内部 id 等于 clientId，可以直接委托。

### Decision 4: 密码传输 — DTO 返回哈希值，不做密码比对服务

**选择**：RPC 接口返回数据库中存储的密码哈希值（如 `{bcrypt}$2a$10$...`），由 Auth 侧的 `PasswordEncoder` 完成比对。

**Because** 这是 Spring Security 的标准做法——`UserDetailsService` 返回包含密码哈希的 UserDetails，由 `AuthenticationProvider` 调用 `PasswordEncoder.matches()` 比对。Auth 模块已配置 `PasswordEncoderFactories.createDelegatingPasswordEncoder()`，支持 `{bcrypt}`、`{noop}` 等前缀。

**注意**：password 字段在 DTO 中传输哈希值，需要确保 Dubbo RPC 通道的安全（内网通信、不暴露给外部）。当前 Nacos + Dubbo 默认为内网通信，风险可控。

### Decision 5: RemoteUserService.findByUsername() 实现路径

**选择**：在 UPMS adapter 层的 RPC 实现中，调用 `SysUserAppService` 查询用户基本信息 + 权限，组装完整 `SysUserDTO`。

**Because** UPMS 模块已有完整的 DDD 结构和 `SysUserAppService`，只需在 RPC 适配层做一次查询和 DTO 转换，不需要修改 domain 层。

## Risks / Trade-offs

- **[RPC 传输密码哈希]** → 缓解：Dubbo RPC 为内网通信，Auth 和 UPMS/Member 部署在同一内网。后续如需加强可引入 Dubbo TLS 加密
- **[findByUsername 查询可能需多表 JOIN]** → 缓解：UPMS 已有用户+角色+权限的查询链路，复用现有 AppService 即可
- **[Member 模块可能尚未完成用户查询功能]** → 缓解：如果 Member 的 RPC 实现也是 stub，本 change 需要同步接通；若 Member 模块结构不完整，先标记 TODO 并确保 Auth 侧代码不依赖硬编码
- **[ClientSettings 中 tenantId/userType 类型为 Object]** → 缓解：`DBRegisteredClientRepository` 写入时是 Long/Integer 类型，取出时需要类型转换，注意处理 null
