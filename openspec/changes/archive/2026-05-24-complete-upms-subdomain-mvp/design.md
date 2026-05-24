## Context

UPMS 模块 14 个子域均已具备完整的分层骨架（Entity、DomainService 接口/实现、Repository、AppService、Controller），但部分子域存在 AppService 绕过 DomainService 直接调 Repository 的问题。核心子域（Tenant、Role、Menu、Dept）的 DomainService 已有较完善的业务逻辑，但 Menu 的 AppService 完全绕过了已有校验逻辑。OauthClient 的 DomainService 是纯 CRUD 空壳且被 AppService 绕过。

## Goals / Non-Goals

**Goals:**
- 所有子域 AppService 的写操作通过 DomainService 调用 Repository，消除直接绕过
- OauthClient DomainService 增加基本校验（clientId 唯一性、clientSecret 非空、token 有效期范围、authorizedGrantTypes 合法性）
- User DomainService 增加用户名唯一性校验
- Area DomainService 补齐 CRUD 委托方法（当前仅有树查询方法）
- Dept AppService 写操作加 `@Transactional`

**Non-Goals:**
- 不做充血模型改造（Entity 行为留到前端对接后）
- 不修改 Entity 结构或新增值对象
- 不改动 Controller 和 Repository 层
- 不增加密码策略、用户锁定等复杂业务规则

## Decisions

### D1: OauthClient 校验放在 DomainService

**决策**：在 `SysOauthClientService` 中增加校验逻辑，不在 AppService 中做。

**Because**：DDD 规则要求业务规则在 domain 层。校验是领域规则的一部分。AppService 只负责编排和 DTO 转换。

**Because**：clientSecret 的加密/编码由 Auth 服务在认证时处理，UPMS 侧只做非空校验。不引入 PasswordEncoder 到 DomainService。

### D2: Area DomainService 补齐 CRUD 方法

**决策**：在 `ISysAreaService` 接口和 `SysAreaService` 实现中增加 `create`、`update`、`removeBatchByIds`、`queryPage`、`queryById` 方法。

**Because**：当前 Area DomainService 仅有 `selectAreaTree` 和 `selectChildren` 两个查询方法，AppService 的写操作直接调 Repository。补齐后保持与其他子域一致的调用链路。

**替代方案**：Area 作为参考数据（几乎不写），保持 AppService 直接调 Repository。放弃，因为违反 DDD 分层合规。

### D3: Menu AppService 改为调用已有 DomainService

**决策**：`SysMenuAppService` 将 CRUD 操作委托给 `SysMenuService`，仅保留 `queryMenuTree` 的树构建和 scope 过滤逻辑在 AppService。

**Because**：`SysMenuService` 已有 `validatePid()`（防止自引用和循环引用）和 `validateBeforeDelete()`（检查子菜单和角色引用），但 AppService 完全没有调用这些校验。这是安全隐患。

### D4: Tenant 删除操作路由到 DomainService

**决策**：在 `SysTenantService` 中增加 `removeBatchByIds` 方法，`SysTenantAppService.removeTenantBatchByIds` 改为调用 DomainService。

**Because**：保持一致性，未来可在删除前增加关联检查（如是否有用户属于该租户）。

### D5: User 唯一性校验利用已有 Repository 方法

**决策**：在 `SysUserService.create()` 和 `SysUserService.update()` 中调用 `ISysUserReadRepository.queryByUsername()` 检查用户名唯一性。

**Because**：`queryByUsername(tenantId, username)` 已存在，错误码 `USER_USERNAME_DUPLICATE` 已定义。无需新增基础设施代码。

## Risks / Trade-offs

- **[Area CRUD 几乎不会被调用]** → 地区数据通常由 SQL 脚本批量导入，但保留链路完整性是 DDD 合规的必要成本
- **[OauthClient clientSecret 明文存储]** → 当前 MVP 阶段可接受，后续改造时由 Auth 服务侧处理编码。UPMS 只存原始值
- **[批量操作校验性能]** → OauthClient 的 clientId 唯一性校验在批量删除场景不涉及，只影响创建和更新，性能可接受
