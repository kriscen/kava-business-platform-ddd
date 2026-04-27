# 服务边界与模块划分

## 2.1 服务边界

### 2.1.1 服务职责矩阵

| 服务 | 职责范围 | 不归其负责 |
|------|----------|-----------|
| **kbpd-gateway** | 统一入口、路由、鉴权、限流、日志 | 业务逻辑、持久化 |
| **kbpd-auth** | OAuth2 认证、Token 发放、SSO | 业务数据、权限判断 |
| **kbpd-upms** | 用户、角色、权限、租户、菜单、部门 | 业务领域逻辑（如订单、会员） |
| **kbpd-member** | 会员、会员等级、会员权益 | 用户管理、认证 |
| **kbpd-xxx** | 对应业务领域 | 用户权限、认证 |

### 2.1.2 服务间依赖关系

```
kbpd-gateway
    │
    ├──► kbpd-auth (验证 Token)
    │
    └──► kbpd-upms (获取用户/权限信息)
              │
              └──► kbpd-member (可选：获取会员信息)
```

**依赖规则**：
- 上游服务可调用下游服务，反之不行
- `kbpd-gateway` 是入口，依赖所有业务服务
- 业务服务之间通过 Dubbo RPC 调用，通过接口耦合
- 禁止业务服务直接访问其他服务的数据库表

### 2.1.3 跨服务调用约定

```java
// 调用方：kbpd-member 服务
@DubboReference
private SysUserService sysUserService;  // 依赖 kbpd-upms 暴露的接口

// 提供方：kbpd-upms 服务
@DubboService
public class SysUserServiceImpl implements SysUserService {
    // 实现
}
```

## 2.2 模块内部边界

### 2.2.1 DDD 分层职责

```
┌─────────────────────────────────────────────────────────────┐
│  adapter/                   # 适配器层                        │
│  ├── controller/           # HTTP 控制器（入站适配器）        │
│  └── rpc/                  # Dubbo RPC 实现（出站适配器）     │
├─────────────────────────────────────────────────────────────┤
│  api/                       # 端口层                          │
│  ├── dto/                  # 数据传输对象（无业务逻辑）        │
│  └── service/              # 服务接口定义（Dubbo Interface）  │
├─────────────────────────────────────────────────────────────┤
│  application/               # 应用层                          │
│  ├── command/              # 命令处理器（CUD 操作）           │
│  └── query/                # 查询处理器（读操作）             │
├─────────────────────────────────────────────────────────────┤
│  domain/                    # 领域层（核心）                  │
│  ├── entity/               # 领域实体（具有身份标识）         │
│  ├── aggregate/           # 聚合根（事务边界）               │
│  ├── vo/                   # 值对象（不可变）                │
│  ├── service/              # 领域服务（无实体的操作）        │
│  ├── repository/           # 仓储接口（抽象持久化）          │
│  └── event/                # 领域事件定义                    │
├─────────────────────────────────────────────────────────────┤
│  infrastructure/            # 基础设施层                     │
│  ├── dao/                  # MyBatis Mapper/XML              │
│  ├── po/                   # 持久化对象（数据库行映射）      │
│  ├── repository/           # 仓储实现                        │
│  ├── converter/            # PO ↔ Entity 转换器（MapStruct）│
│  └── external/             # 外部服务调用实现                │
└─────────────────────────────────────────────────────────────┘
```

### 2.2.2 各层命名约定

| 层 | 命名规则 | 示例 |
|---|---------|------|
| Entity | `{AggregateName}Entity` | `SysUserEntity` |
| Aggregate | `{AggregateName}Aggregate` | `SysUserAggregate` |
| Value Object | `{Name}VO` 或 `{Name}Id` | `SysUserId`, `UserName` |
| Repository Interface | `I{Entity}ReadRepository`, `I{Entity}WriteRepository` | `ISysUserReadRepository` |
| Repository Impl | `{Entity}RepositoryImpl`（在 infrastructure） | `SysUserRepositoryImpl` |
| ApplicationService | `{UsecaseName}AppService` | `UserManagementAppService` |
| Command | `{Action}{Entity}Cmd` | `CreateUserCmd`, `UpdateUserCmd` |
| Query | `{Entity}{Condition}Query` | `UserPageQuery` |
| DTO | `{Entity}{Purpose}DTO` | `SysUserDTO`, `SysUserCreateDTO` |

## 2.3 限界上下文（Bounded Context）

```
┌─────────────────────────────────────────────────────────────────┐
│                        UPMS Context                              │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐           │
│  │  用户    │  │  角色    │  │  权限    │  │  租户    │           │
│  │ Context │  │ Context │  │ Context │  │ Context │           │
│  └────┬────┘  └────┬────┘  └────┬────┘  └────┬────┘           │
│       └─────────────┴─────────────┴─────────────┘             │
│                          │                                      │
│                    共享内核：SysUserId                            │
└─────────────────────────────────────────────────────────────────┘
                           │ 跨上下文引用
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Member Context                              │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐                        │
│  │  会员    │  │  等级    │  │  权益    │                        │
│  │ Context │  │ Context │  │ Context │                        │
│  └─────────┘  └─────────┘  └─────────┘                        │
│       │                                                        │
│       └────────────────────┐                                   │
│                            │                                    │
│                    引用 UPMS 的 SysUserId                       │
└─────────────────────────────────────────────────────────────────┘
```

### 上下文映射

| 源上下文 | 目标上下文 | 映射类型 | 说明 |
|---------|-----------|---------|------|
| Member | UPMS | **Conformist**（追随） | 会员引用用户，但不需要理解用户创建流程 |
| UPMS | Member | **Anticorruption**（防腐层） | UPMS 中需要隔离会员相关概念 |

## 2.4 聚合边界设计原则

1. **聚合内强一致性**：聚合内的对象保持强一致性
2. **聚合间最终一致性**：跨聚合通过领域事件实现最终一致性
3. **聚合是事务边界**：一个聚合对应一次数据库事务
4. **聚合尽量小**：优先设计小聚合，减少并发冲突

### 聚合设计示例

```java
// SysUserAggregate - 用户聚合
public class SysUserAggregate {
    private SysUserId id;
    private UserName name;
    private Password password;           // 值对象
    private PhoneNumber phone;
    private Email email;
    private SysUserStatus status;
    private TenantId tenantId;
    private List<SysUserRole> roles;     // 聚合内对象

    // 聚合根方法 - 保证内部一致性
    public void assignRole(RoleId roleId) {
        // 业务规则校验
        if (status.isLocked()) {
            throw new DomainException("用户已锁定，不能分配角色");
        }
        roles.add(new SysUserRole(roleId));
    }
}
```

## 2.5 包结构规范

```
# 领域层（kbpd-upms-domain）
cn.kava.upms.domain
├── entity/              # 实体（非聚合根）
├── aggregate/           # 聚合根
│   └── sysuser/
│       ├── SysUserAggregate.java
│       ├── SysUserId.java
│       └── SysUserStatus.java
├── vo/                  # 值对象
│   ├── Password.java
│   ├── PhoneNumber.java
│   └── Email.java
├── service/             # 领域服务
├── repository/          # 仓储接口（读写分离）
│   ├── ISysUserReadRepository.java
│   └── ISysUserWriteRepository.java
└── event/               # 领域事件
    ├── UserCreatedEvent.java
    └── UserDeletedEvent.java

# 应用层（kbpd-upms-application）
cn.kava.upms.application
├── command/            # 命令
│   └── sysuser/
│       ├── CreateUserCmd.java
│       ├── UpdateUserCmd.java
│       └── DeleteUserCmd.java
├── query/              # 查询
│   └── sysuser/
│       ├── UserPageQuery.java
│       └── UserDetailQuery.java
└── service/            # 应用服务
    └── sysuser/
        └── SysUserApplicationService.java

# 基础设施层
cn.kava.upms.infrastructure
├── dao/                # MyBatis Mapper
├── po/                 # 持久化对象
├── repository/         # 仓储实现
│   └── sysuser/
│       ├── SysUserReadRepositoryImpl.java
│       └── SysUserWriteRepositoryImpl.java
└── converter/         # MapStruct 转换器
    └── sysuser/
        └── SysUserConverter.java
```

## 2.6 防腐层（Anticorruption Layer）

跨上下文引用时，必须通过防腐层隔离外部模型：

```java
// kbpd-upms 暴露给外部的接口（保持自身模型）
public interface SysUserFacadeService {
    SysUserDTO getUserById(Long userId);
}

// kbpd-member 中的防腐层
@Service
public class MemberUserAdapter {
    @DubboReference
    private SysUserFacadeService sysUserService;

    // 转换为自身领域模型
    public MemberUser toMemberUser(Long userId) {
        SysUserDTO dto = sysUserService.getUserById(userId);
        return new MemberUser(
            MemberUserId.of(dto.getUserId()),
            MemberUserName.of(dto.getNickname()),
            // ...
        );
    }
}
```
