# 代码风格规范

> 本文档基于项目实际代码总结，反映真实使用的命名与结构约定。

## 3.1 命名规范

### 3.1.1 Java 命名

| 类型 | 规范 | 示例 |
|------|------|------|
| 类名 | UpperCamelCase | `SysUserEntity`, `SysUserAppService` |
| 方法名 | lowerCamelCase | `queryById`, `createTenant` |
| 变量名 | lowerCamelCase | `userId`, `roleList` |
| 常量 | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT`, `DEFAULT_PASSWORD` |
| 枚举值 | UPPER_SNAKE_CASE | `COUNTRY`, `PROVINCE`, `ALL`, `CUSTOM` |
| 包名 | lowercase | `com.kava.kbpd.upms.domain.model.aggregate` |

### 3.1.2 各层命名约定

本节列出各层主要类型的命名模式。`{Entity}` 代表业务实体名称（如 `User`、`Tenant`、`Role`）。

**领域层（domain）**

| 类型 | 命名规则 | 示例 |
|------|---------|------|
| 聚合根 | `Sys{Entity}Entity`（放在 `model.aggregate` 包） | `SysUserEntity`, `SysRoleEntity` |
| 实体（非聚合根） | `Sys{Entity}Entity`（放在 `model.entity` 包） | `SysDeptEntity`, `SysMenuEntity`, `SysTenantEntity` |
| ID 值对象 | `Sys{Entity}Id` | `SysUserId`, `SysTenantId`, `SysRoleId` |
| 列表查询值对象 | `Sys{Entity}ListQuery` | `SysUserListQuery`, `SysTenantListQuery` |
| 仓储接口（CQRS） | `ISys{Entity}ReadRepository` / `ISys{Entity}WriteRepository` | `ISysUserReadRepository` |
| 仓储接口（简单） | `ISys{Entity}Repository` | `ISysDeptRepository`, `ISysTenantRepository` |
| 领域服务接口 | `ISys{Entity}Service` | `ISysUserService` |
| 领域服务实现 | `Sys{Entity}Service` | `SysUserService` |

> **聚合根 vs 实体**的区分靠 **包位置**（`aggregate/` vs `entity/`）和 **标记接口**（`AggregateRoot<T>` vs `Entity<T>`），而非类名后缀。

**应用层（application）**

| 类型 | 命名规则 | 示例 |
|------|---------|------|
| 应用服务接口 | `ISys{Entity}AppService` | `ISysUserAppService` |
| 应用服务实现 | `Sys{Entity}AppService` | `SysUserAppService` |
| 命令对象 | `Sys{Entity}CreateCommand` / `Sys{Entity}UpdateCommand` | `SysUserCreateCommand` |
| 应用 DTO | `Sys{Entity}AppDetailDTO` / `Sys{Entity}AppListDTO` | `SysUserAppDetailDTO` |
| 应用层转换器 | `Sys{Entity}AppConverter` | `SysUserAppConverter` |

**API 层（api）**

| 类型 | 命名规则 | 示例 |
|------|---------|------|
| 请求对象 | `Sys{Entity}Request` | `SysUserRequest` |
| 详情响应 | `Sys{Entity}DetailResponse` | `SysUserDetailResponse` |
| 列表响应 | `Sys{Entity}ListResponse` | `SysUserListResponse` |
| 适配器查询 | `Sys{Entity}AdapterListQuery` | `SysUserAdapterListQuery` |
| RPC 传输对象 | `Sys{Entity}DTO` | `SysUserDTO` |
| 远程服务接口 | `IRemote{Entity}Service` | `IRemoteUserService` |

**基础设施层（infrastructure）**

| 类型 | 命名规则 | 示例 |
|------|---------|------|
| 持久化对象 | `Sys{Entity}PO` | `SysUserPO`, `SysTenantPO` |
| Mapper 接口 | `Sys{Entity}Mapper` | `SysUserMapper` |
| 仓储实现 | 去掉接口 `I` 前缀 | `SysUserReadRepository`, `SysTenantRepository` |
| 基础设施转换器 | `Sys{Entity}Converter` | `SysUserConverter` |

**适配器层（adapter）**

| 类型 | 命名规则 | 示例 |
|------|---------|------|
| HTTP 控制器 | `Sys{Entity}Controller`（在 `adapter.http` 包） | `SysUserController` |
| RPC 服务实现 | `Remote{Entity}Service`（在 `adapter.rpc` 包） | `RemoteUserService` |
| 适配器转换器 | `Sys{Entity}AdapterConverter` | `SysUserAdapterConverter` |

**类型层（types）**

| 类型 | 命名规则 | 示例 |
|------|---------|------|
| 枚举 | `Sys{Entity}{Aspect}` | `SysMenuType`, `SysRoleDataScope` |
| 业务异常 | `{Module}BizException` | `UpmsBizException` |
| 错误码枚举 | `{Module}BizErrorCodeEnum` | `UpmsBizErrorCodeEnum` |

### 3.1.3 数据库命名

| 类型 | 规范 | 示例 |
|------|------|------|
| 表名 | snake_case，前缀 `sys_` | `sys_user`, `sys_role_menu` |
| 字段名 | snake_case | `user_id`, `gmt_create`, `del_flag` |
| 索引名 | `idx_{表名}_{字段}` | `idx_sys_user_username` |
| 唯一索引 | `uk_{表名}_{字段}` | `uk_sys_user_username` |

## 3.2 代码结构规范

### 3.2.1 类组织顺序

```java
public class SysUserEntity implements AggregateRoot<SysUserId> {

    // 1. 静态成员（常量 > 静态块 > 静态方法）
    private static final Logger log = LoggerFactory.getLogger(...);

    // 2. 成员变量（ID > 业务属性 > 关联对象）
    private SysUserId id;
    private String username;
    private String password;
    private List<SysRoleId> roles;

    // 3. 构造方法（使用 Lombok @Builder @NoArgsConstructor 后可不写）

    // 4. 业务方法（公开方法 > 私有方法）
    public void assignRole(SysRoleId roleId) { ... }

    // 5. 标记接口实现
    @Override
    public SysUserId identifier() { return id; }
}
```

### 3.2.2 方法长度

- **推荐**：每个方法不超过 30 行
- **上限**：每个方法不超过 50 行
- **例外**：构造函数、复杂的 Builder 模式除外

### 3.2.3 参数数量

- **推荐**：方法参数不超过 3 个
- **上限**：方法参数不超过 5 个
- **超过时的重构方式**：使用 Command 对象封装

```java
// ✗ 不推荐：参数过多
public void createUser(String name, String email, String phone,
    String password, Long deptId, Long tenantId, ...) { }

// ✓ 推荐：使用 Command 对象
public void createUser(SysUserCreateCommand command) { }
```

## 3.3 领域模型规范

### 3.3.1 实体（Entity）

聚合根与非聚合根实体均使用 `Sys{Entity}Entity` 命名，通过包位置和标记接口区分：

```java
/**
 * 部门实体 - 非聚合根，放在 domain.model.entity 包
 * 实现 Entity<T> 标记接口
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysDeptEntity implements Entity<SysDeptId> {
    private SysDeptId id;
    private String name;
    private SysDeptId parentId;
    private Integer sort;

    @Override
    public SysDeptId identifier() { return id; }
}
```

### 3.3.2 聚合根（Aggregate Root）

```java
/**
 * 用户聚合根 - 放在 domain.model.aggregate 包
 * 实现 AggregateRoot<T> 标记接口（继承自 Entity<T>）
 * 使用 @Getter 而非 @Data，避免暴露 setter
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserEntity implements AggregateRoot<SysUserId> {
    private SysUserId id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Integer lockFlag;
    private SysTenantId tenantId;
    private List<SysRoleId> roles;

    // 聚合内强一致性业务规则
    public void assignRole(SysRoleId roleId) {
        if (lockFlag == 1) {
            throw new UpmsBizException("用户已锁定，不能分配角色");
        }
        roles.add(roleId);
    }

    @Override
    public SysUserId identifier() { return id; }
}
```

### 3.3.3 值对象（Value Object）

值对象使用 `@Value @Builder` 保证不可变性，提供静态工厂方法：

```java
/**
 * 用户ID - 值对象，不可变
 * 使用 @Value 生成 final 字段、getter、equals、hashCode
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class SysUserId implements Identifier {
    Long id;

    public static SysUserId of(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return SysUserId.builder().id(id).build();
    }
}
```

列表查询值对象同样使用 `@Value @Builder`：

```java
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class SysUserListQuery implements ValueObject {
    QueryParamValObj queryParam;
}
```

### 3.3.4 标记接口体系

所有标记接口定义在 `com.kava.kbpd.common.core.label`：

| 接口 | 继承关系 | 用途 |
|------|---------|------|
| `ValueObject` | extends `Serializable` | 值对象标记 |
| `Identifier` | extends `ValueObject` | ID 值对象标记 |
| `Entity<T extends Identifier>` | extends `Serializable` | 实体标记，定义 `T identifier()` |
| `AggregateRoot<T extends Identifier>` | extends `Entity<T>` | 聚合根标记（空接口） |

## 3.4 Lombok 注解使用规范

### 3.4.1 各层注解对照

| 层 / 类型 | Lombok 注解 | 说明 |
|-----------|------------|------|
| 聚合根 | `@Getter @Builder @AllArgsConstructor @NoArgsConstructor` | 不用 `@Data`，避免暴露 setter |
| 非聚合根实体 | `@Data @Builder @AllArgsConstructor @NoArgsConstructor` | 简单 CRUD 实体可使用 `@Data` |
| ID 值对象 | `@Value @Builder @AllArgsConstructor(access = PRIVATE)` | `@Value` 保证不可变 |
| 列表查询 VO | `@Value @Builder @AllArgsConstructor(access = PRIVATE)` | 同上 |
| PO | `@Data @EqualsAndHashCode(callSuper = true)` | 继承 BasePO 层级 |
| Command | `@Data @Builder @NoArgsConstructor` | 可变命令对象 |
| DTO（各层） | `@Data` | 简单数据载体 |
| API Request | `@Data` | |
| API Response | `@Data` | |
| API Query | `@Data @EqualsAndHashCode(callSuper = true)` | 继承 `AdapterBaseListQuery` |
| 枚举 | `@Getter @AllArgsConstructor` | 只需 getter 和全参构造 |
| Controller | `@Slf4j` | 日志 |
| App Service impl | `@Slf4j @Service` | |
| RPC impl | `@Slf4j @DubboService` | |
| 仓储 impl | `@Repository` | |

### 3.4.2 依赖注入

```java
// ✓ 使用 @Resource 注入（项目统一规范）
@Repository
public class SysTenantRepository implements ISysTenantRepository {
    @Resource
    private SysTenantMapper sysTenantMapper;
    @Resource
    private SysTenantConverter sysTenantConverter;
}

// ✗ 不使用 @Autowired
```

## 3.5 PO 继承体系

PO 类使用继承链处理审计字段，定义在 `com.kava.kbpd.common.database.po`：

```
BasePO                           # 空基类（@Data @ToString）
├── SysCreatedPO                 # + id, creator, gmtCreate
│   └── SysUpdatedPO             # + modifier, gmtModified
│       └── SysDeletablePO       # + delFlag（@TableLogic 逻辑删除）
├── TenantCreatedPO              # + id, creator, gmtCreate, tenantId
│   └── TenantUpdatedPO          # + modifier, gmtModified
│       └── TenantDeletablePO    # + delFlag（@TableLogic 逻辑删除）
```

选择依据：实体是否需要租户字段和逻辑删除。

```java
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUserPO extends TenantDeletablePO {
    private String username;
    private String password;
    // ...
}
```

## 3.6 MapStruct 转换器

项目在三个层使用 MapStruct 转换器，均配置 `componentModel = "spring"`：

| 层 | 命名 | 职责 | 典型方法 |
|----|------|------|----------|
| infrastructure | `Sys{Entity}Converter` | PO ↔ Entity | `convertPO2Entity`, `convertEntity2PO` |
| application | `Sys{Entity}AppConverter` | Command → Entity, Entity → AppDTO | `convertCreateCommand2Entity` |
| adapter | `Sys{Entity}AdapterConverter` | Request → Command, Entity → Response | `convertRequest2CreateCommand`, `convertEntity2Detail` |

```java
@Mapper(componentModel = "spring")
public interface SysUserConverter {
    SysUserEntity convertPO2Entity(SysUserPO po);
    SysUserPO convertEntity2PO(SysUserEntity entity);
}
```

## 3.7 异常处理规范

### 3.7.1 异常体系

```
RuntimeException
└── BaseBizException          # com.kava.kbpd.common.core.exception
    └── UpmsBizException      # com.kava.kbpd.upms.types.exception（各模块自定义）
```

- `BaseBizException`：包含 `errorCode` 和 `errorMsg` 字段，支持多种构造方式
- 各模块继承创建自己的异常类（如 `UpmsBizException`）

错误码通过枚举管理：

```java
@Getter
@AllArgsConstructor
public enum UpmsBizErrorCodeEnum implements BaseErrorCodeEnum {
    // 待定义具体错误码
    ;

    private final String errorCode;
    private final String errorMsg;
}
```

### 3.7.2 异常使用原则

```java
// ✓ 推荐：使用模块异常类 + 错误码枚举
throw new UpmsBizException(UpmsBizErrorCodeEnum.USER_LOCKED);

// ✓ 也可以：直接传错误信息
throw new UpmsBizException("用户已锁定，不能分配角色");

// ✗ 不推荐：在领域层抛出基础设施异常
throw new DataAccessException("用户数据保存失败");
```

### 3.7.3 全局异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseBizException.class)
    public Result<Void> handleBizException(BaseBizException e) {
        return Result.fail(e.getErrorCode(), e.getErrorMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError()
            .map(FieldError::getDefaultMessage)
            .orElse("参数校验失败");
        return Result.fail("VALIDATION_ERROR", message);
    }
}
```

## 3.8 日志规范

项目统一使用 **Lombok `@Slf4j`** 注解，不手动引入 `org.slf4j.Logger`。

### 3.8.1 日志级别使用

| 级别 | 使用场景 |
|------|----------|
| ERROR | 程序异常，需要人工介入处理 |
| WARN | 潜在问题，但不影响业务继续 |
| INFO | 重要业务节点（登录、创建订单等） |
| DEBUG | 开发调试信息，生产环境不输出 |

### 3.8.2 日志内容规范

```java
// ✗ 不推荐：日志信息不足
log.error("保存失败");

// ✓ 推荐：包含上下文信息
log.error("保存用户失败: userId={}, username={}, tenantId={}",
    user.identifier().getId(), user.getUsername(), user.getTenantId(), e);

// ✗ 不推荐：日志过于冗长
log.info("开始执行创建用户操作，当前时间为：{}，用户名称为：{}",
    LocalDateTime.now(), user.getName());

// ✓ 推荐：关键信息，简明扼要
log.info("创建用户: userId={}, username={}", user.identifier(), user.getUsername());
```

### 3.8.3 敏感信息处理

```java
// ✗ 禁止：日志中记录敏感信息
log.info("用户登录: username={}, password={}", username, password);

// ✓ 推荐：脱敏处理
log.info("用户登录: username={}, phone={}",
    username, phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
```

## 3.9 测试规范

### 3.9.1 测试类命名

| 类型 | 命名规范 | 示例 |
|------|---------|------|
| 启动测试 | `{Module}ApplicationTest` | `UpmsApplicationTest` |
| 单元测试 | `{被测类}Test` | `SysUserEntityTest` |
| 集成测试 | `{被测类}IntegrationTest` | `SysUserReadRepositoryIntegrationTest` |

### 3.9.2 测试注解

```java
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UpmsApplicationTest {
    // ...
}
```

### 3.9.3 测试覆盖率要求

- **领域层**：关键业务逻辑覆盖率 > 80%
- **应用层**：用例测试覆盖率 > 70%
- **涉及资金、权限、数据完整性**：必须 100% 测试

## 3.10 枚举规范

枚举统一使用 `code` + `desc` 字段模式：

```java
@Getter
@AllArgsConstructor
public enum SysMenuType {
    CATALOG("0", "目录"),
    MENU("1", "菜单"),
    BUTTON("2", "按钮");

    private final String code;
    private final String desc;
}
```

| 规则 | 说明 |
|------|------|
| 类名 | `Sys{Entity}{Aspect}` |
| 值命名 | UPPER_SNAKE_CASE |
| 字段 | `code`（String/Integer）+ `desc`（String） |
| 注解 | `@Getter @AllArgsConstructor` |
| 位置 | `types.enums` 包 |

## 3.11 代码格式化

### 3.11.1 格式化工具

使用 Maven Spotless 插件统一代码格式：

```bash
# 格式化代码
mvn spotless:apply

# 检查格式
mvn spotless:check
```

### 3.11.2 IDEA/Eclipse 配置

导入项目根目录的 `config/idea-java-conventions.xml` 或 `eclipse-formatting.xml`。

### 3.11.3 Import 顺序

```xml
<!-- spotless maven plugin 配置 -->
<java>
    <importOrder>
        <order>java,javax,org,com,kava,cn,$\blank,$\</order>
    </importOrder>
</java>
```
