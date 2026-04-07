# 代码风格规范

## 3.1 命名规范

### 3.1.1 Java 命名

| 类型 | 规范 | 示例 |
|------|------|------|
| 类名 | UpperCamelCase | `SysUserAggregate`, `UserServiceImpl` |
| 方法名 | lowerCamelCase | `findByUsername`, `assignRole` |
| 变量名 | lowerCamelCase | `userId`, `roleList` |
| 常量 | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT`, `DEFAULT_PASSWORD` |
| 枚举值 | UPPER_SNAKE_CASE | `USER_STATUS_LOCKED`, `ROLE_TYPE_ADMIN` |
| 包名 | lowercase | `cn.kava.upms.domain.aggregate` |

### 3.1.2 特定类型命名

```
# 值对象 - 以 VO 结尾或使用领域名称
✓ SysUserId, Password, Email, PhoneNumber
✓ UserName, RoleCode

# DTO - 包含用途后缀
✓ SysUserDTO, CreateUserDTO, UpdateUserDTO
✓ UserLoginRequest, UserLoginResponse

# Query 对象 - 以 Query 结尾
✓ UserPageQuery, UserDetailQuery, UserSearchQuery

# Command 对象 - 以 Cmd 结尾
✓ CreateUserCmd, DeleteUserCmd, AssignRoleCmd

# 枚举类 - 使用 EntityType 格式
✓ UserStatusEnum, MenuTypeEnum, DataScopeTypeEnum
```

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
public class SysUserAggregate {

    // 1. 静态成员（常量 > 静态块 > 静态方法）
    private static final Logger log = LoggerFactory.getLogger(...);
    public static final int MAX_NAME_LENGTH = 64;

    // 2. 成员变量（final > mutable）

    // 3. 构造方法（使用 Lombok @RequiredArgsConstructor 后可不写）

    // 4. 业务方法（公开方法 > 私有方法）

    // 5. 内部类（枚举 > 静态内部类 > 内部类）
}
```

### 3.2.2 方法长度

- **推荐**：每个方法不超过 30 行
- **上限**：每个方法不超过 50 行
- **例外**：构造函数、复杂的 Builder 模式除外

### 3.2.3 参数数量

- **推荐**：方法参数不超过 3 个
- **上限**：方法参数不超过 5 个
- **超过时的重构方式**：使用参数对象封装

```java
// ✗ 不推荐：参数过多
public void createUser(String name, String email, String phone,
    String password, Long deptId, Long tenantId, ...) { }

// ✓ 推荐：使用 Command 对象
public void createUser(CreateUserCmd cmd) { }
```

## 3.3 领域模型规范

### 3.3.1 实体（Entity）

```java
/**
 * 用户实体 - 具有全局唯一标识的实体
 */
public class SysUserEntity {
    // 必须有 ID
    private SysUserId id;

    // 业务相关属性
    private String username;
    private String nickname;

    // 审计字段
    private String creator;
    private LocalDateTime gmtCreate;

    // 唯一身份标识（由聚合根管理）
    public SysUserId getId() { return id; }

    // 业务方法
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
```

### 3.3.2 聚合根（Aggregate Root）

```java
/**
 * 用户聚合根 - 事务边界
 */
public class SysUserAggregate implements AggregateRoot<SysUserId> {

    private SysUserId id;
    private UserName username;
    private Password password;
    private UserStatus status;
    private TenantId tenantId;

    // 聚合内强一致性业务规则
    public void lock() {
        if (status == UserStatus.DELETED) {
            throw new DomainException("已删除用户无法锁定");
        }
        this.status = UserStatus.LOCKED;
        // 发布领域事件
        registerEvent(new UserLockedEvent(this.id));
    }

    @Override
    public SysUserId getId() { return id; }
}
```

### 3.3.3 值对象（Value Object）

```java
/**
 * 用户ID - 值对象，不可变
 */
public class SysUserId implements ValueObject {
    private final Long value;

    private SysUserId(Long value) {
        this.value = value;
    }

    public static SysUserId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return new SysUserId(value);
    }

    public Long getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysUserId sysUserId = (SysUserId) o;
        return Objects.equals(value, sysUserId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
```

## 3.4 异常处理规范

### 3.4.1 异常分类

| 异常类型 | 用途 | 包位置 |
|---------|------|--------|
| `DomainException` | 领域层业务规则违反 | `domain.service.exception` |
| `ApplicationException` | 应用层业务错误 | `application.service.exception` |
| `InfrastructureException` | 基础设施层错误 | `infrastructure.exception` |
| `IllegalArgumentException` | 参数校验错误 | JDK |

### 3.4.2 异常处理原则

```java
// ✗ 不推荐：捕获所有异常并记录
try {
    userRepository.save(user);
} catch (Exception e) {
    log.error("保存用户失败", e);
}

// ✓ 推荐：明确异常类型，向上传播
try {
    userRepository.save(user);
} catch (DataAccessException e) {
    throw new InfrastructureException("用户数据保存失败", e);
}
```

### 3.4.3 全局异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public Result<Void> handleDomainException(DomainException e) {
        return Result.fail(e.getCode(), e.getMessage());
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

## 3.5 日志规范

### 3.5.1 日志级别使用

| 级别 | 使用场景 |
|------|----------|
| ERROR | 程序异常，需要人工介入处理 |
| WARN | 潜在问题，但不影响业务继续 |
| INFO | 重要业务节点（登录、创建订单等） |
| DEBUG | 开发调试信息，生产环境不输出 |
| TRACE | 详细跟踪信息 |

### 3.5.2 日志内容规范

```java
// ✗ 不推荐：日志信息不足
log.error("保存失败");

// ✓ 推荐：包含上下文信息
log.error("保存用户失败: userId={}, username={}, tenantId={}",
    user.getId().getValue(), user.getUsername(), user.getTenantId(), e);

// ✗ 不推荐：日志过于冗长
log.info("开始执行创建用户操作，当前时间为：{}，用户名称为：{}",
    LocalDateTime.now(), user.getName());

// ✓ 推荐：关键信息，简明扼要
log.info("创建用户: userId={}, username={}", user.getId(), user.getUsername());
```

### 3.5.3 敏感信息处理

```java
// ✗ 禁止：日志中记录敏感信息
log.info("用户登录: username={}, password={}", username, password);

// ✓ 推荐：脱敏处理
log.info("用户登录: username={}, phone={}",
    username, phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
```

## 3.6 代码格式化

### 3.6.1 格式化工具

使用 Maven Spotless 插件统一代码格式：

```bash
# 格式化代码
mvn spotless:apply

# 检查格式
mvn spotless:check
```

### 3.6.2 IDEA/Eclipse 配置

导入项目根目录的 `config/idea-java-conventions.xml` 或 `eclipse-formatting.xml`。

### 3.6.3 Import 顺序

```xml
<!-- spotless maven plugin 配置 -->
<java>
    <importOrder>
        <order>java,javax,org,com,kava,cn,$\blank,$\</order>
    </importOrder>
</java>
```

## 3.7 注解使用规范

### 3.7.1 Lombok 注解

| 注解 | 使用场景 | 注意事项 |
|------|----------|---------|
| `@RequiredArgsConstructor` | 领域实体、聚合根（需要注入依赖） | 不要用于 PO 类 |
| `@Getter/@Setter` | DTO、VO | domain 实体按需使用 |
| `@Builder` | DTO、Command、Query | 聚合根慎用 |
| `@EqualsAndHashCode` | VO 必须，Entity/聚合根仅含 ID | callSuper = false 通常更合理 |

### 3.7.2 Spring 注解

```java
// ✗ 不推荐：滥用 @Service/@Repository
@Service
public class DomainService {  // 领域服务不应是 Spring Bean
        }

// ✓ 推荐：领域服务是普通类，通过构造器注入
public class UserDomainService {
    private final IUserReadRepository readRepository;

    public UserDomainService(IUserReadRepository readRepository) {
        this.readRepository = readRepository;
    }
}

// ✗ 不推荐：在领域层使用 Spring 注解
@DomainService  // 不存在这样的注解
public class UserService {

}

// ✓ 推荐：领域逻辑在聚合根或领域服务中
public class SysUserAggregate {
    public void changePassword(Password newPassword) {
        // 密码变更逻辑在聚合根内
    }
}
```

## 3.8 测试规范

### 3.8.1 测试类命名

| 类型 | 命名规范 | 示例 |
|------|---------|------|
| 单元测试 | `{被测类}Test` | `SysUserAggregateTest` |
| 集成测试 | `{被测类}IntegrationTest` | `UserRepositoryIntegrationTest` |
| 应用层测试 | `{被测类}ApplicationTest` | `CreateUserApplicationTest` |

### 3.8.2 测试方法命名

```java
@Test
void should_throw_exception_when_password_is_empty() { }

@Test
void should_assign_role_successfully() { }

@Test
void should_not_allow_duplicate_username() { }
```

### 3.8.3 测试覆盖率要求

- **领域层**：关键业务逻辑覆盖率 > 80%
- **应用层**：用例测试覆盖率 > 70%
- **涉及资金、权限、数据完整性**：必须 100% 测试
