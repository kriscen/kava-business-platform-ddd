## Context

UPMS 模块 14 个子域的 CRUD 骨架已完整，所有 Controller 都有 page/detail/create/update/delete 端点。但经审查发现三个层面的问题：

1. **P0 断裂**：Menu 的 Create/Update Command 为空壳（`SysMenuCreateCommand`/`SysMenuUpdateCommand` 无字段），Menu tree 的 `SysMenuListResponse` 缺少 `children` 字段导致树结构被丢弃
2. **P1 缺口**：前端表单需要的下拉选择器（Role/Dept/Tenant 非分页列表）和 Dept tree 端点不存在
3. **P2 体验**：响应只返回关联 ID 不返回名称，前端无法直接展示

## Goals / Non-Goals

**Goals:**
- 修复 Menu Create/Update 空壳，使前端能正常创建和编辑菜单
- 修复 Menu tree 响应结构，使前端能渲染菜单树
- 补齐前端表单必需的下拉选择器和树端点
- 在响应中补充关联名称，减少前端 N+1 查询

**Non-Goals:**
- 不新增业务逻辑（充血化、权限引擎等属于后续 change）
- 不涉及 Security/登录认证（属于另一个 change）
- 不涉及文件上传（multipart）改造
- 不重构现有端点的 URL 风格或返回格式

## Decisions

### D1: 关联名称在 Infrastructure 层批量查询，不跨 Repository 调用

**决策**：在 Infrastructure 层的 Repository 实现中，通过 Mapper 的 JOIN 查询或批量 IN 查询获取关联名称。

**Because...** DDD 规则禁止 Domain Service 跨聚合写，但 Repository 实现可以跨表读。关联名称是展示需求而非业务规则，在 Infrastructure 层用 SQL JOIN / 批量查询最高效，避免 N+1。

**替代方案**：在 Application 层多次调用不同 Repository 拼装 → 会产生 N+1 查询性能问题。

### D2: 下拉端点返回精简 DTO，不复用分页 Response

**决策**：新建 `SysRoleDropdownResponse`（id、roleName、roleCode）和 `SysTenantDropdownResponse`（id、name、code、status）等精简 DTO。

**Because...** 下拉选择器只需要 id 和显示名，不需要分页 Response 的全部字段。复用分页 Response 会导致返回冗余数据且语义不清。

### D3: Dept tree 复用 Area tree 的 TreeBuilder 模式

**决策**：Dept tree 实现参考 `SysAreaService.selectAreaTree()` 的模式，复用 `TreeBuilder` 工具类。

**Because...** 项目已有成熟的树构建工具，不需要引入新依赖或重写逻辑。Dept 和 Area 的树结构本质相同（pid + id）。

### D4: Menu tree Response 添加 children 字段，不新建 Response 类

**决策**：在 `SysMenuListResponse` 中添加 `List<SysMenuListResponse> children` 字段。

**Because...** 最小改动，前端已有的类型定义只需扩展一个字段。新建 Response 类会增加无谓的转换代码。

## Risks / Trade-offs

- **[性能] 关联名称查询**：用户列表页批量查询 dept/tenant/role 名称可能有性能影响 → 通过批量 IN 查询而非逐条查询缓解，后续数据量大时可加缓存
- **[范围] P2 关联名称可能触发连锁改动**：每新增一个关联 ID 字段就要对应补充名称 → 仅补充当前已有明确需求的关联名称，不预设未来字段
- **[兼容] Response 新增字段是向后兼容的**：新增字段不破坏已有前端代码 → 无迁移风险
