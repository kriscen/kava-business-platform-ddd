## Context

SysRole 是 UPMS 模块中角色管理的核心聚合，当前已实现 CRUD + 菜单绑定 + 用户角色关联。但存在多项缺陷影响功能正确性：列表 DTO 为空、API 审计字段混入、菜单校验与 Spec 矛盾、缺少租户隔离和唯一性校验、依赖注入不规范。

## Goals / Non-Goals

**Goals:**
- 修复六项已知缺陷，使角色管理功能可用且符合 DDD 规范
- 确保角色查询具备租户隔离，符合产品"强数据隔离"要求
- API 层字段清理，审计字段由系统自动填充，不暴露给前端

**Non-Goals:**
- 不实现数据权限（dsType/dsScope）的运行时过滤逻辑，留作后续独立 change
- 不增加角色状态/启禁用等新字段
- 不涉及认证链路中角色权限的加载

## Decisions

### D1: 租户隔离从 Security Context 获取 tenantId

**决策**：角色分页查询通过 Security Context 获取当前用户的 tenantId，传入 domain 层进行过滤。

**Because**: 产品要求强数据隔离，tenantId 不应由前端传入（可伪造）。从服务端认证上下文获取更安全。平台管理员（无 tenantId）可查看所有角色。

### D2: roleCode 唯一性在 Domain Service 中校验

**决策**：在 `SysRoleService.create()` 和 `SysRoleService.update()` 中增加 roleCode 唯一性校验，通过 ReadRepository 查询同租户下是否存在相同 roleCode。

**Because**: 唯一性是业务规则，属于 domain 层职责。数据库层面也应加唯一索引作为兜底。

### D3: 菜单绑定空值以 Spec 为准

**决策**：移除 `validateMenuBinding()` 中的空值检查，允许 menuIds 为空。仅保留菜单 scope 校验。

**Because**: Spec 明确定义了"menuIds 为空 → 创建角色不写中间表"的场景，代码实现应与 Spec 一致。

### D4: 依赖注入统一改为构造器注入

**决策**：Controller 层和 Infrastructure 层的 `@Resource` 字段注入全部改为 `@RequiredArgsConstructor` + `final` 字段。

**Because**: DDD 规范明确要求构造器注入，字段注入不利于测试和显式依赖声明。

## Risks / Trade-offs

- **roleCode 校验并发风险** → 创建和更新时的 roleCode 查询与写入之间存在极小的竞态窗口。**缓解**：数据库层面加 `(tenant_id, role_code)` 唯一索引作为最终兜底。
- **租户隔离改造范围** → 仅涉及 queryPage，queryById 暂不加租户过滤（当前场景是管理员操作自己租户的角色）。**后续可按需加强**。
