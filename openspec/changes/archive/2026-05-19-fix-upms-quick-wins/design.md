## Context

UPMS 模块在上一轮权限系统实现后，存在 4 个明显的 bug 和缺失项。本 change 为快速修复，不涉及架构变更。

当前状态：
- 用户响应 DTO 包含 password 字段，API 返回密码哈希
- DBRegisteredClientRepository.findByClientId 缺少 null 检查，客户端不存在时 NPE
- 用户/角色的创建、更新操作涉及主表+关联表写入，无事务保护
- 所有表的 creator、gmtCreate、modifier、gmtModified 字段始终为 null，无自动填充机制

## Goals / Non-Goals

**Goals:**
- 修复密码泄露安全风险
- 修复 OAuth2 客户端查询空指针
- 保证多步写操作的原子性
- 实现审计字段自动填充

**Non-Goals:**
- 不实现方法级权限控制（@PreAuthorize），属于后续专项
- 不处理 DataScope 拦截器空实现问题
- 不处理多角色数据权限合并
- 不改造 DTO 结构或接口签名

## Decisions

### D1: 密码字段使用 @JsonIgnore 而非删除字段

**Because** SysUserRequest（入参）仍需 password 字段接收创建/修改密码请求，SysUserAdapterListQuery（内部查询）也可能用于密码匹配。仅在响应 DTO（SysUserDetailResponse、SysUserListResponse）上加 `@JsonIgnore`，影响最小。

### D2: DBRegisteredClientRepository 增加 null 安全检查

**Because** 当前实现中 `RegisteredClient.withId(clientId)` 将 id 设为 clientId，`findById` 委托给 `findByClientId` 逻辑正确。真正的问题是 `findByClientId` 未检查 RPC 返回值是否为 null。增加 null 判断，客户端不存在时返回 null（符合 Spring Authorization Server 的契约）。

### D3: @Transactional 放在 application service 层而非 domain service 层

**Because** DDD 约束要求 domain 层不得依赖 Spring。`@Transactional` 是 Spring 注解，应放在 application service（SysUserAppService、SysRoleAppService）上。与已有模式一致（SysTenantAppService.createTenant 已采用此方式）。

涉及的方法：
- `SysUserAppService.createUser` — 创建用户 + 保存角色关联
- `SysUserAppService.updateUser` — 删除旧角色关联 + 保存新角色关联 + 更新用户
- `SysUserAppService.removeUserBatchByIds` — 批量删除 + 级联清理
- `SysRoleAppService.createRole` — 创建角色 + 保存菜单关联
- `SysRoleAppService.updateRole` — 删除旧菜单关联 + 保存新菜单关联 + 更新角色
- `SysRoleAppService.removeRoleBatchByIds` — 批量删除 + 级联清理

### D4: MetaObjectHandler 放在 kbpd-common-database，使用 SecurityUtils 获取用户

**Because** kbpd-common-database 已有 MyBatis-Plus 配置，是 MetaObjectHandler 的自然归属。通过项目统一的 `SecurityUtils.getUserId()` 获取当前用户标识，无认证上下文时（系统级操作、定时任务）回退为 "system"。

自动填充字段映射：
- `creator` — insert 时从 SecurityUtils 获取用户标识
- `gmtCreate` — insert 时设为 `LocalDateTime.now()`
- `modifier` — insert 和 update 时从 SecurityUtils 获取用户标识
- `gmtModified` — insert 和 update 时设为 `LocalDateTime.now()`
- `delFlag` — insert 时设为 "0"（未删除）

## Risks / Trade-offs

- **[MetaObjectHandler 依赖 Spring Security]** → kbpd-common-database 运行时依赖 Spring Security，但所有服务模块均已引入，无实际风险。若未来有不需要 Security 的模块使用 database，可通过 `try-catch` 优雅降级
- **[事务范围]** → `@Transactional` 加在 application service 公开方法上，事务范围覆盖整个用例。若后续有更细粒度需求，可调整
