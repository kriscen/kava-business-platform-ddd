## Context

当前 JWT 认证链路存在以下问题：

1. **JWT claims 不完整**: `jwtTokenCustomizer` 只写入 userId、username、deptId、authorities（B端）和 memberId（C端），缺少 tenantId 和 userType。tenantId 和 userType 在认证阶段已存在于 `ExtendAuthenticationToken` 中，但在 token 生成时丢失。
2. **角色与权限混合**: `authorities` 字段混合了角色（ROLE_ADMIN）和细粒度权限（user:read），导致 JWT 膨胀且权限变更需等 token 过期才生效。
3. **下游上下文提取不统一**: 下游服务通过 `SecurityUtils` 手动逐字段读取，无统一上下文对象，且缺少 tenantId 和 userType 提取能力。
4. **Dubbo RPC 上下文丢失**: 服务间 Dubbo 调用时，HTTP SecurityContext 不传播，被调用方无法获取用户身份。
5. **Member 服务未接入认证**: 缺少 `@EnableResourceServer` 和 `jwk-set-uri` 配置。

## Goals / Non-Goals

**Goals:**
- JWT token 携带完整的租户和用户上下文（tenantId、userType、roles）
- 下游服务通过统一 UserContext 对象获取认证信息
- B端权限从 JWT 移出，存入 Redis 缓存，按需查询
- Dubbo RPC 调用自动传播用户上下文
- Member 服务能验证 JWT token

**Non-Goals:**
- Gateway 层不做 JWT 验签（保持纯路由，认证由下游服务自行处理）
- C端角色体系定义（JWT 预留 roles: []，Kava 不定义 C端角色）
- RemoteMemberService 真实实现（依赖 Member DDD 搭建）
- Gateway 全局 Filter 认证
- 权限缓存机制（权限缓存属于 UPMS 权限领域，与 UPMS domain 一起设计）

## Decisions

### Decision 1: 从 ExtendAuthenticationToken 读取 tenantId/userType 写入 JWT

**选择**: 在 `jwtTokenCustomizer` 中判断 `context.getPrincipal() instanceof ExtendAuthenticationToken`，直接从 token 读取 tenantId 和 userType。

**Because**: tenantId 和 userType 已在认证链路中通过 `ExtendAuthenticationToken` 传递，无需修改 `SysUserDetails` 或 `MemberDetails` 增加字段。改动最小，信息来源可信（服务端 ClientSettings）。

### Decision 2: authorities 替换为 roles

**选择**: JWT claims 中用 `roles` 字段替代 `authorities`，仅包含角色代码（如 ROLE_ADMIN）。

**Because**: 角色数量少且稳定，适合放入 JWT。细粒度权限不在 JWT 中携带，由下游服务按需通过 RPC 调用 UPMS 查询。权限缓存策略属于 UPMS 领域职责，将在后续 change 中设计。

### Decision 3: 统一 UserContext 对象 + SecurityUtils 增强

**选择**: 新增 `UserContext` 类（放在 `kbpd-common-core`）封装所有 JWT claims，`SecurityUtils.getUserContext()` 从 JwtAuthenticationToken 构造返回。下游服务统一使用 UserContext。

**Because**: 避免每个消费方手动读取 JWT claims map 并转型。UserContext 根据 userType 明确哪些字段可用，类型安全。

### Decision 4: Dubbo 上下文传播通过 SPI Filter 实现

**选择**: 实现 Dubbo `Filter` 接口（Consumer 端和 Provider 端各一个），通过 SPI 自动激活。Consumer 端从 SecurityContext 提取 UserContext 写入 RpcContext attachment；Provider 端从 attachment 恢复 UserContext 到 ThreadLocal。

**Because**: Dubbo SPI 是标准的扩展机制，业务代码无感知。Filter 放在 `kbpd-common-security` 中，所有引入该模块的服务自动具备上下文传播能力。

### Decision 5: Member 服务仅接入 ResourceServer，不实现业务逻辑

**选择**: 添加 `@EnableResourceServer`、`jwk-set-uri` 配置和 `DevSecurityConfig`。RemoteMemberService 保持返回 null。

**Because**: Member DDD 结构尚未搭建，RemoteMemberService 的实现是独立的模块开发任务，不在本次 JWT 链路完善范围内。

## Risks / Trade-offs

- **[JWT 结构变更]** → 已发出的旧 token 中无 tenantId/roles 字段。因为当前处于开发阶段，无生产 token 需要兼容，可接受不兼容变更。上线后变更需考虑 token 版本策略。
- **[权限缓存一致性]** → ~~TTL 30 分钟内权限变更不会即时反映~~ → 权限缓存已 defer 至 UPMS 权限领域设计。当前阶段下游服务通过角色鉴权，细粒度权限查询留待后续实现。
- **[Dubbo Filter SPI 注册]** → 需要在 `META-INF/dubbo/org.apache.dubbo.rpc.Filter` 文件中注册。如果遗漏注册，Filter 不生效但不会报错。需要在集成测试中验证。
- **[UserContext 构造位置]** → 从 JWT 构造 UserContext 发生在 ResourceServerConfiguration 的 jwtAuthenticationConverter 中。如果服务未启用 @EnableResourceServer，UserContext 不可用。Dubbo Provider 端从 RpcContext 恢复 UserContext 作为替代路径。
