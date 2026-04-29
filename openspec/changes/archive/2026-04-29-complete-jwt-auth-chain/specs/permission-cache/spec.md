## DEFERRED — 移至 UPMS 权限领域设计

### Reason

权限缓存属于 UPMS 模块的领域职责。Auth 模块只负责认证（验证身份 + 签发 JWT），不应承担权限缓存管理。权限的缓存策略、查询接口、刷新机制应与 UPMS 的权限 domain 一起设计，确保：
- 权限数据归属 UPMS 管理
- 缓存 key 设计与 UPMS 领域模型一致
- 缓存刷新事件由 UPMS 权限变更驱动

当前 change scope 仅限于：JWT 携带 roles（用于粗粒度鉴权），细粒度权限由后续 UPMS change 实现。

---

### Deferred Requirements（供后续 reference）

<details>
<summary>原 spec requirements（已 defer）</summary>

### Requirement: B端用户登录时预热权限缓存

B端用户登录成功后，系统 MUST 将该用户的细粒度权限集合写入 Redis 缓存。缓存 key 格式为 `auth:perms:{tenantId}:{userId}`，value 为权限代码集合的 JSON 序列化。

#### Scenario: B端用户登录后权限写入 Redis
- **WHEN** B端用户（SysUserDetails）成功完成认证流程
- **THEN** 系统 MUST 将该用户的权限集合写入 Redis
- **AND** key 为 `auth:perms:{tenantId}:{userId}`
- **AND** value 为权限代码列表（如 ["user:read", "user:write"]）

#### Scenario: C端用户登录不触发权限缓存
- **WHEN** C端用户（MemberDetails）成功完成认证流程
- **THEN** 系统 MUST NOT 写入权限缓存

### Requirement: 权限缓存设置 TTL

权限缓存 MUST 设置 TTL（默认 30 分钟），过期后自动清除。

#### Scenario: 缓存自动过期
- **WHEN** 权限缓存写入 Redis
- **THEN** MUST 设置 TTL，默认值为 1800 秒（30 分钟）

### Requirement: 权限缓存可被下游服务查询

下游服务 MUST 能通过 `UserContext.getPermissions()` 获取当前用户的权限集合。首次调用时从 Redis 查询并缓存到 UserContext 中。

#### Scenario: 缓存命中时返回权限
- **WHEN** 调用 `UserContext.getPermissions()` 且 Redis 中存在对应 key
- **THEN** MUST 返回 Redis 中存储的权限集合

#### Scenario: 缓存未命中时返回空集合
- **WHEN** 调用 `UserContext.getPermissions()` 且 Redis 中不存在对应 key
- **THEN** MUST 返回空集合（不应抛出异常或触发 RPC 查询）

### Requirement: 权限缓存可主动刷新

系统 MUST 提供手动清除指定用户权限缓存的能力，供管理员修改权限后调用。

#### Scenario: 手动清除权限缓存
- **WHEN** 管理员修改了某用户的权限
- **AND** 调用缓存清除方法
- **THEN** Redis 中该用户的权限缓存 key MUST 被删除
- **AND** 下次该用户请求时 `getPermissions()` 返回空集合

</details>
