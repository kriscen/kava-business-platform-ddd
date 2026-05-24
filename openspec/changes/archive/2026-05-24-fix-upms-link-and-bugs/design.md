## Context

UPMS 模块 14 个子域的 CRUD 骨架已全部搭建完成，但存在以下链路断裂问题：

1. **MetaObjectHandler 缺失**：`kbpd-common-database` 中未注册 `MybatisMetaObjectHandler`，导致 PO 层 `@TableField(fill=...)` 注解无对应处理器，creator/gmtCreate 等审计字段全部为 null
2. **loginByPwd 桩实现**：`RemoteUserService.loginByPwd()` 返回硬编码 `id=1`，auth 模块的密码认证链路无法走通
3. **User/Tenant 缺失 ID 值对象**：`SysUserEntity` 和 `SysTenantEntity` 直接使用 `Long` 作为 ID 类型，与其他子域（Role/Menu/Dept/Area 等）使用独立值对象的模式不一致
4. **FileGroup 路径错误**：`SysFileGroupController` 的 `@RequestMapping` 使用 `FileGroup-group` 而非 kebab-case 的 `file-group`

## Goals / Non-Goals

**Goals:**
- 实现 MetaObjectHandler，使 PO 审计字段自动填充
- 实现 loginByPwd 的真实用户查询和密码校验
- 补齐 SysUserId、SysTenantId 值对象，统一 ID 类型模式
- 修正 FileGroup 路径命名

**Non-Goals:**
- 不涉及用户锁定策略（P2，后续 change）
- 不涉及密码过期检测（P2，后续 change）
- 不涉及数据权限子系统（P1，独立 change）
- 不涉及操作日志 AOP 和审计日志字段追踪（P3，后续 change）
- 不做业务充血（按探索结论单独规划）

## Decisions

### Decision 1: MetaObjectHandler 放在 kbpd-common-database

**选择**：在 `kbpd-common-database` 模块中实现 `MybatisMetaObjectHandler`，实现 `MetaObjectHandler` 接口。

**Because**：MetaObjectHandler 是 MyBatis-Plus 的基础设施层组件，所有业务模块共享。放在 `kbpd-common-database` 符合"数据库相关配置统一放置"的现有模式（该模块已包含 Druid 配置、MyBatis-Plus 配置等）。

**备选方案**：放在各业务模块的 infrastructure 层 → 重复代码，不可接受。

**实现要点**：
- 通过 `SecurityUtils.getUserContext()` 获取当前用户 ID 作为 creator/modifier
- 无认证上下文时回退为 `"system"`
- 使用 `@Component` 注解，由 Spring 自动注册

### Decision 2: loginByPwd 通过 Repository 查询而非新增 Domain 方法

**选择**：在 `RemoteUserService` 中直接调用 `ISysUserReadRepository` 的已有查询方法，校验密码后返回用户 ID。

**Because**：loginByPwd 的职责是"根据用户名查找用户 + 校验密码 + 返回用户标识"，这是一个适配层的认证编排逻辑，不属于 User 聚合根的核心业务规则。Domain 层已有 `ISysUserReadRepository.findByUsername()` 方法可复用。

**实现要点**：
- 调用 `ISysUserReadRepository.findByUsername(username)` 查询用户
- 使用 `BCryptPasswordEncoder.matches()` 校验密码
- 校验失败抛出 `AuthenticationServiceException`
- 查询不到用户返回 null（由 auth 层处理 `UsernameNotFoundException`）

### Decision 3: 值对象 ID 使用 Long 基础类型

**选择**：`SysUserId` 和 `SysTenantId` 值对象内部持有 `Long value`，与其他子域（`SysRoleId`、`SysMenuId` 等）保持一致。

**Because**：项目已有成熟的值对象 ID 模式（参考 `kbpd-common-core` 中的 `RoleId`、`MenuId` 等），统一使用 Long 基础类型可保持一致性，且与 MyBatis-Plus 的 Long 主键策略兼容。

### Decision 4: 值对象补齐范围限定在 domain 层及直接引用层

**选择**：本次只修改 domain 层的 entity/aggregate/service/repository 引用，以及 infrastructure 的 converter 和 PO 适配。不修改 application 层的 command/DTO（它们使用 Long 即可）。

**Because**：值对象是 domain 层的概念，application 层的 Command/DTO 是对外契约，使用基础类型 Long 更简洁。MapStruct converter 负责 Long ↔ 值对象的转换。

## Risks / Trade-offs

- **[Risk] loginByPwd 实现依赖 BCryptPasswordEncoder** → 该 Bean 已在 `kbpd-common-security` 中配置，可直接注入
- **[Risk] 值对象补齐涉及多文件修改** → 通过 MapStruct converter 隔离，domain 外的改动最小化
- **[Risk] MetaObjectHandler 依赖 UserContext** → 定时任务等无认证场景需回退为 "system"，已在设计中考虑
