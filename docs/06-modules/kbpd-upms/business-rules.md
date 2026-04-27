# kbpd-upms -- 业务规则

> 当前模块已完成 14 个资源的 CRUD 脚手架搭建，部分领域业务逻辑尚未实现（如密码加密、关联关系持久化等）。以下文档包含已确认的设计约束和规划中的业务规则。

---

## 已确认的设计约束

| 约束 | 说明 | 来源 |
|---|---|---|
| 多租户隔离 | 大多数实体携带 `tenantId`，PO 继承 `TenantDeletablePO`，租户间数据隔离 | 代码结构 |
| CQRS 读写分离 | User、Role 聚合使用独立的 Read/Write Repository | 领域层设计 |
| DDD 分层依赖 | domain 不依赖 Spring 等外部框架，依赖方向内向 | 项目规范 |
| 聚合边界明确 | 仅 User 和 Role 为聚合根，User 持有 roleIds，Role 持有 menuIds | 领域模型 |
| 软删除 | PO 继承 `SysDeletablePO`，通过 `delFlag` 实现逻辑删除 | 基础设施层 |
| 三级 PO 继承 | `BasePO` → `SysDeletablePO`（+delFlag）→ `TenantDeletablePO`（+tenantId） | 基础设施层 |
| 树形数据构建 | 地区实体使用 Hutool `TreeUtil`，按 `areaStatus=YES` 过滤、`areaSort DESC` 排序 | SysAreaService |
| 统一异常体系 | `UpmsBizException` 继承 `BaseBizException`，错误码枚举 `UpmsBizErrorCodeEnum` 已定义但为空 | types 层 |

---

## 领域模型关系

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│   SysUser   │──N:N──│   SysRole   │──N:N──│   SysMenu   │
│  (聚合根)    │       │  (聚合根)    │       │   (实体)     │
│  roleIds    │       │  menuIds    │       │             │
└──────┬──────┘       └──────┬──────┘       └─────────────┘
       │                     │
       │ N:1                 │ N:1
       ▼                     ▼
┌─────────────┐       ┌─────────────┐
│   SysDept   │       │  SysTenant  │
│   (实体)     │       │   (实体)     │
└─────────────┘       └─────────────┘

关联表（基础设施层）：
  sys_user_role  → userId, roleId
  sys_role_menu  → roleId, menuId
```

---

## 预期业务规则（规划）

### 用户管理

| 规则 | 描述 | 优先级 |
|---|---|---|
| 密码加密存储 | 创建/修改用户时，密码必须加密后存储，明文不得持久化 | P0 |
| 用户名唯一性 | 同一租户下用户名不可重复 | P0 |
| 用户锁定策略 | 连续登录失败 N 次后自动锁定，需管理员手动解锁（`lockFlag` 字段） | P1 |
| 密码过期检测 | 根据 `passwordExpireFlag` 和 `passwordModifyTime` 判断是否需要强制修改密码 | P1 |
| 用户-角色关联持久化 | 创建/更新用户时同步维护 `sys_user_role` 关联表 | P0 |

### 角色与权限

| 规则 | 描述 | 优先级 |
|---|---|---|
| 角色编码唯一性 | 同一租户下角色编码（`roleCode`）不可重复 | P0 |
| 角色-菜单关联持久化 | 创建/更新角色时同步维护 `sys_role_menu` 关联表 | P0 |
| 数据权限过滤 | 根据 `dsType`（ALL/CUSTOM/DEPT_AND_CHILD/DEPT_ONLY/SELF）在查询时过滤数据 | P1 |
| 菜单作用域隔离 | `scope=SYSTEM` 的菜单仅超级管理员可见，`scope=TENANT` 的菜单对租户管理员可见 | P2 |

### 租户管理

| 规则 | 描述 | 优先级 |
|---|---|---|
| 租户编码唯一性 | `code` 和 `tenantDomain` 全局唯一 | P0 |
| 租户有效期 | `startTime` / `endTime` 控制租户可用时段，过期自动禁用 | P2 |
| 租户初始菜单 | 创建租户时根据 `menuId` 模板初始化菜单树 | P2 |

### 安全与审计

| 规则 | 描述 | 优先级 |
|---|---|---|
| Dubbo RPC 实现完善 | `findByUsername` 和 `loginByPwd` 需对接真实仓储，供 auth 服务调用 | P0 |
| 操作日志自动记录 | 关键操作（用户创建、角色变更、权限修改）自动写入 sys_log | P1 |
| 审计日志字段级追踪 | 敏感字段变更时记录 beforeVal / afterVal 到 sys_audit_log | P2 |
| 错误码体系建立 | 在 `UpmsBizErrorCodeEnum` 中定义用户名冲突、角色编码冲突、租户过期等错误码 | P1 |

### 文件与通用

| 规则 | 描述 | 优先级 |
|---|---|---|
| SysLogPO 表名修正 | `@TableName("sys_file_group")` 疑似复制粘贴错误，应为 `sys_log` | P0 |
| 文件分组路径修正 | `FileGroup-group` 应改为 `file-group` | P1 |
| 公共参数校验 | `validateCode` 字段用于参数值校验表达式（如正则），创建/更新时应验证合法性 | P2 |

---

## 开发优先级建议

1. **补全聚合关联持久化** — 用户-角色、角色-菜单关联表的读写逻辑，使 CRUD 真正可用
2. **完善 Dubbo RPC 接口** — 实现 `findByUsername` 和 `loginByPwd`，打通 auth 服务认证链路
3. **用户密码安全** — 密码加密存储 + 用户名唯一性校验
4. **错误码与异常体系** — 填充 `UpmsBizErrorCodeEnum`，统一业务异常处理
5. **数据权限过滤** — 基于角色 `dsType` 实现数据范围查询过滤
6. **操作日志自动化** — AOP 拦截关键操作自动记录日志
7. **代码缺陷修复** — SysLogPO 表名、FileGroup 路径等已知问题
