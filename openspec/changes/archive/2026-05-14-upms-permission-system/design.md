## Context

UPMS 模块已建立 RBAC 数据模型（sys_user / sys_role / sys_menu / sys_user_role / sys_role_menu）和基本 CRUD 骨架，但存在以下问题：

- **写入链路断裂**：`SysRoleCreateCommand`/`UpdateCommand` 为空壳，角色-菜单和用户-角色的中间表无写入逻辑
- **权限校验缺失**：JWT 仅携带 `roles`（角色编码），服务端无权限解析和校验机制
- **数据隔离缺失**：MyBatis-Plus 仅有分页拦截器，无租户拦截器和数据权限拦截器
- **菜单作用域不完整**：`SysMenuScope` 仅有 system/tenant 两值，缺少"双方可见"选项
- **租户管理缺失**：无平台管理员管理租户、分配菜单的接口

现有基础设施较为完善：JWT 签发（kbpd-auth）、UserContext ThreadLocal 传播（含 Dubbo RPC 传播）、ResourceServer 自动配置、PO 层 tenantId 字段均已就绪。

## Goals / Non-Goals

**Goals:**

- 补全角色管理 CRUD，使角色-菜单绑定写入完整可用
- 补全用户-角色绑定，使用户创建/更新时角色关联正确持久化
- 建立服务端权限缓存 + 方法级鉴权运行时
- 实现 MyBatis-Plus 租户拦截器，自动注入 tenant_id 过滤
- 实现 @DataScope 注解驱动的数据权限行级过滤
- 完善菜单三值作用域模型（system / tenant / system_tenant）
- 新增平台管理员租户管理 CRUD（含菜单分配和必备角色初始化）
- 补充 UPMS 错误码体系

**Non-Goals:**

- 菜单包（Menu Package）机制——后期优化，MVP 不实现
- Gateway 层权限拦截——仅做透传，鉴权由各微服务内部处理
- C 端用户权限——本期聚焦 B 端管理权限
- 前端路由动态生成——属于前端工作，后端仅提供菜单树接口
- 权限变更实时推送——权限变更后需重新登录生效（Redis 缓存随 token 生命周期）

## Decisions

### D1: 权限校验采用服务端方法级注解

**选择**：各微服务内部使用 `@PreAuthorize` 注解做方法级权限校验。

**Because**：Gateway 仅做路由透传，不具备细粒度鉴权能力；按钮级权限（如 `sys_user_add`）只能在业务服务内部判定。JWT 已携带 roles，结合 Redis 缓存的 permissions 映射，可满足方法级校验需求。

**备选方案**：Gateway 统一拦截（仅能做 URL 粒度，无法覆盖按钮权限）。

### D2: 权限缓存策略——JWT 放 roles，服务端 Redis 缓存 permissions

**选择**：JWT 只携带 roles（角色编码集合），permissions（权限字符串集合）由服务端 Redis 缓存。首次访问时从数据库加载用户权限并缓存，权限变更需重新登录生效。

**Because**：
- JWT 体积可控，不随权限数量膨胀
- 角色数量少且稳定，适合放 JWT；权限字符串可能很多（数百个），放 JWT 不合理
- B 端管理后台用户量小，Redis 缓存命中率极高
- 权限变更频率低，"重新登录生效"可接受

**缓存 Key 设计**：`perm:user:{userId}` → Set\<String\>（permission 字符串集合），TTL 与 access token 一致。

### D3: 数据权限采用 MyBatis-Plus 拦截器 + 注解标记

**选择**：
- **租户隔离**：`TenantLineInnerInterceptor` 自动注入 `WHERE tenant_id = ?`，对业务代码透明
- **数据权限（行级过滤）**：自定义 `DataScopeInnerInterceptor`，通过 `@DataScope` 注解按需启用

**Because**：
- 租户隔离是强制的、全局的，适合自动拦截（安全默认值）
- 数据权限仅部分业务查询需要，按需启用避免全表 JOIN 的性能开销
- MyBatis-Plus 拦截器机制成熟，与项目现有的 `MybatisPlusConfig` 集成简单

**拦截器执行顺序**：`TenantLine` → `DataScope` → `Pagination`

### D4: 菜单作用域三值模型

**选择**：`SysMenuScope` 扩展为三个值：
- `SYSTEM`：仅平台管理员可见（租户管理、平台统计等）
- `TENANT`：仅租户内可见（租户自定义业务页面）
- `SYSTEM_TENANT`：双方可见（用户管理、角色管理等基础功能）

**Because**：二值模型无法表达"基础功能菜单双方都需要"的场景。新增 `SYSTEM_TENANT` 后，平台管理员创建租户时从 `SYSTEM_TENANT` 菜单中选择分配，租户管理员只能从已分配的 `SYSTEM_TENANT` + 自建 `TENANT` 菜单中构建角色。

### D5: 租户菜单分配使用 sys_tenant.menu_id 字段

**选择**：复用现有 `sys_tenant.menu_id`（text 类型），存储租户可用菜单 ID 列表。不引入菜单包表。

**Because**：MVP 阶段租户量小，直接分配菜单列表足够；菜单包是后期优化项。

### D6: 新租户自动初始化租户管理员角色

**选择**：创建租户时，系统自动创建一个"租户管理员"角色，关联所有已分配菜单的权限，并将租户创建者（或指定用户）绑定此角色。

**Because**：新租户创建后需要有管理员能立即登录并配置其他角色，否则系统不可用。

### D7: 平台管理员跳过租户隔离和数据权限

**选择**：通过 `UserContext.userType` 判断，平台管理员（B端且特定标识）执行查询时：
- 租户拦截器不注入 `tenant_id` 条件
- 数据权限拦截器不注入部门/人员过滤条件

**Because**：平台管理员需要跨租户查看数据，属于全局管理角色。

### D8: 权限解析时 Auth Server 写入 dataScope 到 JWT

**选择**：在 `kbpd-auth` 的 JWT token 自定义逻辑中，为 B 端用户写入 `dataScope` claim（来自用户主角色的 dsType）。

**Because**：`JwtClaimConstants.DATA_SCOPE` 已定义但未使用；数据权限拦截器需要知道当前用户的数据范围策略，从 JWT 获取避免额外查库。

## Risks / Trade-offs

- **[权限缓存一致性]** → 缓存 TTL 与 token 一致，权限变更需重新登录。MVP 阶段可接受，后期可引入缓存失效机制。
- **[拦截器性能影响]** → MyBatis-Plus 拦截器对每条 SQL 执行，可能影响查询性能。因为只在标记了 `@DataScope` 的查询上启用行级过滤，租户过滤条件简单（`tenant_id = ?`），性能影响可控。
- **[部门树递归查询]** → `DEPT_AND_CHILD` dsType 需要递归查询部门子树，可能导致慢查询。缓解方案：部门树缓存到 Redis，数据量小（每个租户通常几十个部门）。
- **[sys_tenant.menu_id 字段类型]** → 当前为 text 类型存储 ID 列表，大数据量时解析性能差。MVP 阶段菜单数量有限（数十到数百），可接受。后期可改为中间表。
- **[平台管理员识别]** → 目前 `UserType` 枚举只有 TO_B/TO_C/ERROR，无法区分平台管理员和租户管理员。需要在 JWT claims 或 UserContext 中增加标识（如角色编码包含 `ROLE_ADMIN`），或扩展 UserType。
