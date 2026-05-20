# kbpd-upms -- 业务规则

> 模块已完成 14 个资源的 CRUD、RBAC 关联持久化、权限运行时、租户管理和数据隔离。部分领域业务逻辑和集成测试尚在推进中。

---

## 已确认的设计约束

| 约束 | 说明 | 来源 |
|---|---|---|
| 多租户隔离 | MyBatis-Plus `KavaTenantLineInnerInterceptor` 自动注入 `WHERE tenant_id = ?`，PO 继承 `TenantDeletablePO` | upms-permission-system |
| CQRS 读写分离 | User、Role 聚合使用独立的 Read/Write Repository | 领域层设计 |
| DDD 分层依赖 | domain 不依赖 Spring 等外部框架，依赖方向内向 | 项目规范 |
| 聚合边界明确 | 仅 User 和 Role 为聚合根，User 持有 roleIds，Role 持有 menuIds | 领域模型 |
| 软删除 | PO 继承 `SysDeletablePO`，通过 `delFlag` 实现逻辑删除 | 基础设施层 |
| 三级 PO 继承 | `BasePO` → `SysDeletablePO`（+delFlag）→ `TenantDeletablePO`（+tenantId） | 基础设施层 |
| 树形数据构建 | 地区实体使用 Hutool `TreeUtil`，按 `areaStatus=YES` 过滤、`areaSort DESC` 排序 | SysAreaService |
| 统一异常体系 | `UpmsBizException` 继承 `BaseBizException`，错误码枚举 `UpmsBizErrorCodeEnum` 覆盖角色/用户/菜单/租户 | types 层 |
| 拦截器执行顺序 | TenantLine → DataScope → Pagination（在 `MybatisPlusConfig` 中注册） | kbpd-common-database |
| 平台管理员跳过 | `ROLE_ADMIN` 角色跳过租户隔离和数据权限过滤 | upms-permission-system |
| 平台管理员无角色 | 平台管理员不经过 RBAC 角色体系，直接可见所有平台级菜单（SYSTEM + SYSTEM_TENANT），角色体系仅服务于租户 | 产品设计 |

---

## 领域模型关系

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│   SysUser   │──N:N──│   SysRole   │──N:N──│   SysMenu   │
│  (聚合根)    │       │  (聚合根)    │       │   (实体)     │
│  roleIds    │       │  menuIds    │       │  scope      │
└──────┬──────┘       └──────┬──────┘       └─────────────┘
       │                     │
       │ N:1                 │ N:1
       ▼                     ▼
┌─────────────┐       ┌─────────────┐
│   SysDept   │       │  SysTenant  │
│   (实体)     │       │   (实体)     │
└─────────────┘       │  menuId     │
                      └─────────────┘

关联表（基础设施层）：
  sys_user_role  → userId, roleId
  sys_role_menu  → roleId, menuId
```

---

## 已实现的业务规则

### 用户管理

| 规则 | 描述 |
|---|---|
| 用户-角色关联持久化 | 创建/更新用户时全量替换 `sys_user_role`（先删后插），删除时级联清理，查询时返回 roleIds |
| 用户名唯一性 | 创建用户时校验同一租户下用户名不可重复（`UpmsBizErrorCodeEnum.USERNAME_EXISTS`） |

### 角色与权限

| 规则 | 描述 |
|---|---|
| **平台管理员无角色** | 平台管理员（`ROLE_ADMIN`）不经过角色体系，直接可见所有 `scope=SYSTEM` 和 `scope=SYSTEM_TENANT` 的菜单，无需角色-菜单绑定 |
| 角色体系仅服务于租户 | `SysRole` 聚合根仅用于租户内权限管理，由租户管理员创建和管理 |
| 角色-菜单关联持久化 | 创建/更新角色时全量替换 `sys_role_menu`（先删后插），删除时级联清理 sys_role_menu + sys_user_role，查询时返回 menuIds |
| 角色编码唯一性 | 创建和更新角色时校验同一租户下 roleCode 不可重复（`UpmsBizErrorCodeEnum.ROLE_CODE_DUPLICATE`），不同租户允许相同 roleCode |
| 菜单作用域三值模型 | `SysMenuScope`: SYSTEM（平台专属）、TENANT（租户专属）、SYSTEM_TENANT（双方可见） |
| 角色绑定校验 | 角色绑定菜单时校验 scope 可见性：租户角色不可绑定 SYSTEM 菜单，平台角色不可绑定 TENANT 菜单 |
| 数据权限 dsType | 角色持有 dsType（0=ALL/1=CUSTOM/2=DEPT_AND_CHILD/3=DEPT_ONLY/4=SELF）和 dsScope 字段 |

### 权限运行时

| 规则 | 描述 |
|---|---|
| 权限 Redis 缓存 | `PermissionCacheService` 缓存 key `{env}:upms:perm:user:{userId}`，TTL 12h，缓存未命中从 DB 加载 |
| 方法级鉴权 | `@PreAuthorize("@permissionChecker.hasPermission('xxx')")` 校验，ROLE_ADMIN 跳过所有权限检查 |
| 菜单树接口 | `GET /menu/tree` 按用户角色和 scope 过滤：平台管理员看 SYSTEM + SYSTEM_TENANT，租户用户看已分配范围 |
| JWT dataScope | Auth Server 签发 JWT 时写入 `dataScope` claim，`UserContext.fromJwtClaims` 解析 |
| Dubbo 权限传播 | Consumer/Provider Filter 传播 permissions、dataScope、roles 等 UserContext 字段 |

### 租户管理

| 规则 | 描述 |
|---|---|
| 租户数据隔离 | `KavaTenantLineInnerInterceptor` 自动注入 `WHERE tenant_id = ?`，平台管理员跳过，支持忽略表配置 |
| 租户创建自动初始化 | 创建租户时自动创建 `tenant_admin` 角色，关联所有已分配菜单，dsType=ALL |
| 租户菜单分配 | 通过 `sys_tenant.menu_id` 字段存储分配的菜单 ID 列表 |

### 错误码

| 分类 | 错误码 | 说明 |
|---|---|---|
| 角色 | A00101 | 角色不存在 |
| 角色 | A00102 | 角色编码已存在 |
| 角色 | A00103 | 角色必须关联至少一个菜单 |
| 用户 | A00201 | 用户不存在 |
| 用户 | A00202 | 用户名已存在 |
| 菜单 | A00301 | 菜单不存在 |
| 菜单 | A00302 | 菜单作用域无效 |
| 租户 | A00401 | 租户不存在 |
| 租户 | A00402 | 租户编码已存在 |

---

## 待实现 / 后续迭代

| 规则 | 描述 | 优先级 | 状态 |
|---|---|---|---|
| 密码加密存储 | 创建/修改用户时密码加密，明文不得持久化 | P0 | 未实现 |
| 用户锁定策略 | 连续登录失败 N 次自动锁定 | P1 | 未实现 |
| 密码过期检测 | `passwordExpireFlag` + `passwordModifyTime` 判断 | P1 | 未实现 |
| DataScope SQL 注入 | `DataScopeInnerInterceptor.beforeQuery()` 未调用 `buildDataScopeCondition()` | P0 | DEFERRED |
| @DataScope 注解使用 | 注解已定义但无方法标注，拦截器未检查注解 | P1 | DEFERRED |
| CUSTOM dsType 实现 | dsType "1" 未使用 dsScope 字段，与 DEPT_ONLY 逻辑相同 | P1 | DEFERRED |
| 多角色数据权限合并 | 当前取第一个角色 dsType，需按优先级取最大范围 | P1 | DEFERRED |
| DEPT_AND_CHILD 递归 | 未实现部门子树递归查询 | P1 | DEFERRED |
| 租户创建管理员用户 | 仅创建 tenant_admin 角色，未创建初始管理员用户 | P1 | DEFERRED |
| MetaObjectHandler | PO 层 FieldFill 注解无对应 Handler | P1 | 未实现 |
| loginByPwd 完善 | Dubbo RPC `loginByPwd` 仍为桩实现 | P0 | 未实现 |
| 操作日志自动记录 | AOP 拦截关键操作写入 sys_log | P2 | 未实现 |
| 审计日志字段追踪 | 敏感字段变更记录 beforeVal / afterVal | P2 | 未实现 |
| SysLogPO 表名修正 | `@TableName("sys_file_group")` 疑似应为 `sys_log` | P0 | 待确认 |
| 文件分组路径修正 | `FileGroup-group` 应改为 `file-group` | P1 | 未实现 |
| 集成测试 | 角色菜单绑定、用户角色绑定、权限缓存、租户隔离、数据权限、菜单作用域、租户管理 | P1 | DEFERRED |
