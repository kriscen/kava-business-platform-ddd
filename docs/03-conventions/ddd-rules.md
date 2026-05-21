# DDD 实现规范

本文档定义 Kava 项目中 DDD 各层的具体实现规则，是 [boundaries.md](../02-architecture/boundaries.md) 的补充。boundaries.md 定义"有哪些层、叫什么名字"，本文档定义"每层怎么写、边界在哪"。

---

## 1. 领域模型风格

### 1.1 充血方向，逐步完善

领域模型朝充血方向演进，但不要求一步到位：

- **已有业务规则的实体**：将行为收入实体/聚合根（如 `SysUserEntity.assignRole()`）
- **暂无复杂规则的实体**：保持数据壳状态，等业务规则出现时再补充
- **原则**：不为没有的业务复杂性建模型

### 1.2 Entity 的演进方向

```
当前（允许）                          目标（逐步演进）
═════════════                         ════════════════

@Data @Builder                        public class SysUserEntity {
public class SysUserEntity {              private SysUserId id;
    private SysUserId id;                 private String username;
    private String username;              private Integer lockFlag;
    private Integer lockFlag;             private List<SysRoleId> roleIds;
    private List<SysRoleId> roleIds;
}                                         // 行为方法：封装业务规则
                                          public void assignRole(SysRoleId roleId) {
                                              if (lockFlag == 1) {
                                                  throw new UpmsBizException(...);
                                              }
                                              roleIds.add(roleId);
                                          }
                                      }
```

---

## 2. Domain 层与框架的关系

### 2.1 Spring 依赖策略

- Domain 层 pom 依赖 `spring-context`（仅注解），不依赖 `spring-boot-starter`
- Domain 类使用 `@Service` 标注 + **构造器注入**
- **禁止** `@Resource`、`@Autowired` 字段注入
- Domain 类不调用任何 Spring API（如 `ApplicationContext`、`BeanFactory`）

```java
// ✓ 正确：构造器注入
@Service
public class SysUserServiceImpl implements ISysUserService {
    private final ISysUserWriteRepository writeRepository;
    private final ISysUserReadRepository readRepository;

    public SysUserServiceImpl(
            ISysUserWriteRepository writeRepository,
            ISysUserReadRepository readRepository) {
        this.writeRepository = writeRepository;
        this.readRepository = readRepository;
    }
}

// ✗ 错误：字段注入
@Service
public class SysUserServiceImpl implements ISysUserService {
    @Resource
    private ISysUserWriteRepository writeRepository;
}
```

### 2.2 Domain 层禁止项

| 禁止 | 原因 |
|------|------|
| 依赖 MyBatis/数据库驱动 | 持久化是基础设施关注点 |
| 依赖 Spring Web/Controller | 接入是适配器关注点 |
| 调用 `ApplicationContext` 等 Spring API | 运行时框架耦合 |
| 依赖其他业务模块的 domain | 限界上下文隔离 |

---

## 3. DomainService 规范

### 3.1 何时创建 DomainService

只有满足以下条件之一时才创建 DomainService：

| 条件 | 示例 |
|------|------|
| 跨 Repo 编排（写入多张表） | User 保存 + 角色关联保存 |
| 业务规则校验 | Role 的菜单作用域校验 |
| 跨聚合协调 | Tenant 创建时初始化 Role |
| 非平凡领域算法 | Area 的树形构建 |

### 3.2 透传 DomainService 的处理

- 所有子域保留 DomainService **接口**，作为扩展点
- **未被 AppService 调用的** DomainService 实现：不注入 Repository，方法体为空
- **被 AppService 调用的** DomainService 实现：正常注入 Repository，实现逻辑
- AppService 不注入未被使用的 DomainService

```
简单实体（暂无复杂逻辑）         复杂子域（有业务规则）
═══════════════════════         ══════════════════════

ISysDeptService（接口）          ISysRoleService（接口）
  └─ 实现类：空壳                 └─ 实现类：注入 Repo，含逻辑

AppService                      AppService
  └─ 直接调 Repository            └─ 调 DomainService
```

---

## 4. 跨聚合访问规范

### 4.1 核心规则

| 操作类型 | DomainService | AppService |
|----------|--------------|------------|
| 跨聚合**读** | ✓ 允许 | ✓ 允许 |
| 跨聚合**写** | ✗ 禁止 | ✓ 允许（在 @Transactional 内） |

### 4.2 当前处理方式（应用层同步编排）

```
SysTenantAppService.createTenant()
  ├── sysTenantService.create(entity)         ← 聚合 A 写入
  └── sysRoleService.initTenantAdminRole(...)  ← 聚合 B 写入（App 层编排）
  // 同一 @Transactional，强一致
```

跨聚合写逻辑归属原则：
- **业务规则**（如角色初始化逻辑）放在对应聚合的 DomainService 中
- **编排**（先做 A 再做 B）放在 AppService 中

### 4.3 引入领域事件后的演进路径

```
阶段 1（当前）：AppService 同步编排
  AppService.createTenant()
    ├─ sysTenantService.create()
    └─ sysRoleService.initAdminRole()

阶段 2（同步事件）：聚合注册事件，Spring @EventListener 同步分发
  TenantService.create()
    └─ entity.registerEvent(TenantCreatedEvent)
  AppService 发布事件
  TenantCreatedEventHandler.handle()
    └─ sysRoleService.initAdminRole()

阶段 3（异步事件）：按需用 @TransactionalEventListener 或 MQ
  根据业务需求决定是否需要最终一致性
```

---

## 5. 层间引用规范

### 5.1 类型归属

| 类型 | 归属模块 | 说明 |
|------|----------|------|
| 标记接口（Identifier, Entity, AggregateRoot） | common-core | 所有层可见 |
| 跨模块共享 ID（SysUserId, SysTenantId） | common-core | 多模块都需要 |
| 模块内部 ID（SysRoleId, SysMenuId 等） | domain/model/valobj | 按需提升到 common-core |
| 公共枚举 | types | 所有层可见 |
| ListQuery 值对象 | domain/model/valobj | Repository 参数，必须在 domain |
| Entity / AggregateRoot | domain/model | 领域核心 |
| DomainService | domain/service | 领域服务 |
| Repository 接口 | domain/repository | 仓储抽象 |
| Command / AppDTO | application/model | 应用层类型 |
| AppService 接口 | application/service | 应用层端口 |
| Request / Response | api/model | 外部契约 |

### 5.2 ID 类型的提升规则

ID 默认放在所属模块的 `domain/model/valobj` 中。当且仅当满足以下条件时提升到 `common-core`：

- 其他模块需要引用该 ID 类型（如 auth 模块需要 SysUserId）
- 跨限界上下文共享的身份标识

**原则：按需提升，不预设共享。**

### 5.3 AppService 接口签名

AppService 接口使用 domain 类型（ID、ListQuery）和 application 类型（Command、AppDTO）：

```java
public interface ISysUserAppService {
    SysUserId createUser(SysUserCreateCommand command);
    void removeUserBatchByIds(List<SysUserId> ids);
    SysUserAppDetailDTO queryUserById(SysUserId id);
    PagingInfo<SysUserAppListDTO> queryUserPage(SysUserListQuery query);
}
```

### 5.4 Adapter 引用边界

```
Adapter 可以引用：
  ✓ domain.model.valobj.*       ← ID 类型、ListQuery（类型安全包装器，不含业务逻辑）
  ✓ types.*                      ← 枚举、常量
  ✓ api.*                        ← Request / Response / DTO
  ✓ application.service          ← AppService 接口
  ✓ application.converter        ← App Converter
  ✓ application.model            ← Command、AppDTO

Adapter 禁止引用：
  ✗ domain.model.entity.*        ← 实体
  ✗ domain.model.aggregate.*     ← 聚合根
  ✗ domain.service.*             ← 领域服务
  ✗ domain.repository.*          ← 仓储接口
```

### 5.5 完整依赖方向图

```
  ┌──────────┐      引用 domain.valobj, types, api, application
  │ Adapter  │─────────────────────────────────────────────┐
  └────┬─────┘                                             │
       │                                                   │
       │ 引用 application                                  │
       ▼                                                   ▼
  ┌──────────┐      引用 domain                     ┌──────────┐
  │Application│─────────────────────────────────────▶│  Domain  │
  └──────────┘                                      └────┬─────┘
       │                                                 │
       │ 引用（Command, AppDTO, AppService 接口）         │ 依赖
       ▼                                                 ▼
  ┌───────────────────┐                          ┌──────────────┐
  │ Infrastructure    │                          │ common-core  │
  └───────────────────┘                          │ types        │
                                                 └──────────────┘
```

---

## 6. Converter 规范

### 6.1 三层转换

```
Adapter Converter:  API Request → Application Command
                   API Query   → Domain ListQuery（通过 valobj 引用）
                   AppDTO      → API Response

App Converter:      Command → Domain Entity
                   Domain Entity → AppDTO

Infra Converter:    Domain Entity → PO
                   PO → Domain Entity
```

### 6.2 ID 转换模式

```java
// Adapter → Application: Long → SysUserId（在 Controller 中）
SysUserId id = SysUserId.of(idParam);

// App Converter: Command(Long fields) → Entity(ID fields)
@Mapping(source = "deptId", target = "deptId.id")
SysUserEntity convertCreateCommand2Entity(SysUserCreateCommand command);

// Infra Converter: Entity(ID) → PO(Long)
@Mapping(source = "id.id", target = "id")
SysUserPO convertEntity2PO(SysUserEntity entity);
```

---

## 7. Repository 选型规范

### 7.1 两种模式

| 模式 | 接口 | 适用场景 |
|------|------|----------|
| **CQRS Split** | `IBaseReadRepository` + `IBaseWriteRepository` | 有中间关联表、读写差异显著的聚合 |
| **SimpleRepository** | `IBaseSimpleRepository` | 扁平实体、简单 CRUD 的聚合 |

### 7.2 选型标准

满足以下**任一**条件时使用 CQRS Split，否则使用 SimpleRepository：

- 聚合有**中间关联表**需要级联写入（如 `sys_user_role`、`sys_role_menu`）
- Read 端需要**大量专用查询方法**（如多表 JOIN 取权限）
- Write 端需要**关联管理方法**（如 `saveUserRoles`、`removeUserRoles`）
- 存在只需要读取能力的消费方（如 `SysMenuAppService` 注入 `ISysUserReadRepository`）

### 7.3 接口契约对齐

两种模式共享统一的读操作能力：

```java
// CQRS Read 端（三个泛型参数）
public interface IBaseReadRepository<I extends Identifier, E extends Entity<I>, Q extends ValueObject> {
    E queryById(I id);
    PagingInfo<E> queryPage(Q query);
}

// SimpleRepository（同样三个泛型参数，读写合一）
public interface IBaseSimpleRepository<I extends Identifier, E extends Entity<I>, Q extends ValueObject> {
    I create(E entity);
    Boolean update(E entity);
    Boolean removeBatchByIds(List<I> ids);
    PagingInfo<E> queryPage(Q query);
    E queryById(I id);
}
```

### 7.4 当前使用情况

| 模式 | 聚合 |
|------|------|
| CQRS Split | SysUser（关联 sys_user_role）、SysRole（关联 sys_role_menu） |
| SimpleRepository | SysTenant、SysMenu、SysDept、SysFile、SysLog、SysOauthClient、SysArea、SysAuditLog、SysRouteConf、SysI18nMessage、SysFileGroup、SysPublicParam |
