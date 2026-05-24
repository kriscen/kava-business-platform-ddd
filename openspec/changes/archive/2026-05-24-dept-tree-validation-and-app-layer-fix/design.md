## Context

SysDept 是 UPMS 模块中的部门子域，`sys_dept` 表通过 `pid` 字段构建父子层级树。当前实现只有基础 CRUD 骨架：DomainService 是纯委托无业务逻辑，Application 层的 Command/DTO 为空类导致数据传递断裂，AppService 绕过 DomainService 直连 Repository。

Menu 子域已完成树形验证（自引用、循环引用、删除前检查子节点和角色引用），Dept 与 Menu 同为树形结构，采用相同方案。

## Goals / Non-Goals

**Goals:**
- 补齐 Dept 树形验证规则（自引用、循环引用、删除前检查子节点和用户引用）
- 填充 Application 层空壳 Command/DTO，使 CRUD 数据流通
- 修正 AppService 调用链，改为走 DomainService
- 确保 `queryAll()` 受租户隔离过滤

**Non-Goals:**
- 不新增 RPC 接口（Dept 暂无跨服务调用需求）
- 不做部门层级深度限制
- 不做同级部门名称唯一性校验
- 不重构 Entity 为 Aggregate（Dept 当前无内部值对象需求）

## Decisions

### 1. 树形验证算法：全表加载 + 内存遍历
**Because** 部门数量在租户维度通常为几十到几百条，全表加载到内存构建 childToParent 映射后向上遍历检测循环引用，性能完全可接受。与 Menu 子域保持一致，避免引入 SQL 递归 CTE 的跨库兼容问题。

### 2. 用户引用检查：在 DeptRepository 中注入 SysUserMapper
**Because** `sys_user.dept_id` 引用了 `sys_dept`，删除部门前需要检查是否有用户关联。`sys_user` 有自己的聚合根但 `sys_user` 的 Dept 关联只是一列外键，不需要跨聚合写操作——只在 Dept Repository 实现中注入 SysUserMapper 做 selectCount 即可。这与 MenuRepository 注入 SysRoleMenuMapper 检查角色引用的模式一致。

### 3. AppService 改为注入 ISysDeptService
**Because** DomainService 已有（且即将加入）业务逻辑，AppService 直连 Repository 会绕过这些规则。改为注入 ISysDeptService 使得所有操作经过 DomainService，符合 DDD 分层原则。

### 4. queryAll() 租户隔离由 Infrastructure 层保障
**Because** 当前项目使用 MyBatis-Plus 的多租户插件（`TenantLineInnerInterceptor`），SQL 层面自动追加 `tenant_id` 过滤条件。Repository 实现无需手动过滤，但需要在 `SysDeptRepository.queryAll()` 中使用 MyBatis-Plus 的 `list()` 方法而非自定义 SQL，以确保租户拦截器生效。

### 5. Command/DTO 字段选取
**Because** Command 是 Application 层的输入模型，只包含业务操作所需字段（create 不含 id，update 含 id）；DTO 是 Application 层的输出模型，包含查询所需展示字段但不包含审计字段（creator/gmtCreate 等由基础设施层自动填充，不应暴露给前端通过 Command 传入）。

## Risks / Trade-offs

- **[全表加载性能]** → 部门数量远小于菜单，风险更低。若未来部门数达到万级，可改用 SQL 递归 CTE，但当前不需要过早优化。
- **[SysUserMapper 跨聚合引用]** → 仅在 Infrastructure 层的 Repository 实现中注入，Domain 层不感知。符合 DDD 中"跨聚合读"允许的约定。
