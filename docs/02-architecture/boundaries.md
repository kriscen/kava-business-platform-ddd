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
│  ├── model/                # 数据模型                          │
│  │   ├── request/          # 请求对象（入参）                  │
│  │   ├── response/         # 响应对象（出参）                  │
│  │   ├── query/            # 列表查询参数                      │
│  │   └── dto/              # 数据传输对象（无业务逻辑）        │
│  └── service/              # 服务接口定义（Dubbo Interface）  │
├─────────────────────────────────────────────────────────────┤
│  application/               # 应用层                          │
│  ├── model/                # 数据模型                          │
│  │   ├── command/          # 命令对象（CUD 操作）              │
│  │   └── dto/              # 应用层 DTO                       │
│  └── service/              # 应用服务实现                      │
├─────────────────────────────────────────────────────────────┤
│  domain/                    # 领域层（核心）                  │
│  ├── model/                # 领域模型                          │
│  │   ├── entity/           # 领域实体（非聚合根）              │
│  │   ├── aggregate/        # 聚合根（事务边界）               │
│  │   └── valobj/           # 值对象（不可变）                │
│  ├── service/              # 领域服务（无实体的操作）        │
│  ├── repository/           # 仓储接口（抽象持久化）          │
│  └── event/                # 领域事件定义（⚠️ 尚未实现）      │
├─────────────────────────────────────────────────────────────┤
│  infrastructure/            # 基础设施层                     │
│  ├── dao/                  # MyBatis Mapper/XML              │
│  │   └── po/               # 持久化对象（数据库行映射）      │
│  ├── adapter/repository/   # 仓储实现                        │
│  └── converter/            # PO ↔ Entity 转换器（MapStruct）│
└─────────────────────────────────────────────────────────────┘
```

### 2.2.2 各层命名约定

| 层 | 命名规则 | 示例 |
|---|---------|------|
| Entity（非聚合根） | `{Name}Entity` | `SysDeptEntity` |
| Aggregate（聚合根） | `{AggregateName}Entity` | `SysUserEntity` |
| Value Object | `{Name}Id` / `{Name}ListQuery` | `SysUserId`, `SysUserListQuery` |
| Repository（简单 CRUD） | `I{Entity}Repository` | `ISysDeptRepository` |
| Repository（CQRS 读写分离） | `I{Entity}ReadRepository`, `I{Entity}WriteRepository` | `ISysUserReadRepository` |
| Repository Impl | `{Entity}Repository`（在 infrastructure） | `SysUserReadRepository` |
| ApplicationService | `{Entity}AppService` | `SysUserAppService` |
| Command | `Sys{Entity}{Action}Command` | `SysUserCreateCommand`, `SysUserUpdateCommand` |
| Application DTO | `Sys{Entity}App{Purpose}DTO` | `SysUserAppDetailDTO`, `SysUserAppListDTO` |
| API Request | `Sys{Entity}Request` | `SysUserRequest` |
| API Response | `Sys{Entity}{Detail/List}Response` | `SysUserDetailResponse`, `SysUserListResponse` |
| API Query | `Sys{Entity}AdapterListQuery` | `SysUserAdapterListQuery` |
| API DTO | `Sys{Entity}DTO` | `SysUserDTO` |

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
// SysUserEntity - 用户聚合根
public class SysUserEntity implements AggregateRoot<SysUserId> {
    private SysUserId id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Integer lockFlag;
    private SysTenantId tenantId;
    private List<SysRoleId> roles;

    // 聚合根方法 - 保证内部一致性
    public void assignRole(SysRoleId roleId) {
        // 业务规则校验
        if (lockFlag == 1) {
            throw new DomainException("用户已锁定，不能分配角色");
        }
        roles.add(roleId);
    }
}
```

## 2.4.1 CQRS 策略

并非所有聚合都需要读写分离。策略如下：

| 场景 | 策略 | 示例 |
|------|------|------|
| **复杂领域** | 读写分离：`I{Entity}ReadRepository` + `I{Entity}WriteRepository` | `SysUser`、`SysRole` |
| **简单 CRUD** | 单一接口：`I{Entity}Repository` | `SysDept`、`SysMenu`、`SysTenant` 等 |

**判断标准**：聚合是否有复杂的业务规则、多表关联查询、或需要独立的查询优化？是则拆分，否则保持简单。

## 2.5 包结构规范（实际代码）

```
# 领域层（kbpd-upms-domain）
com.kava.kbpd.upms.domain
├── model/
│   ├── entity/              # 实体（非聚合根）
│   │   └── SysDeptEntity.java
│   ├── aggregate/           # 聚合根
│   │   └── SysUserEntity.java
│   │   └── SysRoleEntity.java
│   └── valobj/              # 值对象 / 查询参数
│       ├── SysUserId.java
│       ├── SysUserListQuery.java
│       └── SysDeptId.java
├── service/                 # 领域服务
│   ├── ISysUserService.java
│   └── impl/
│       └── SysUserService.java
└── repository/              # 仓储接口
    ├── ISysUserReadRepository.java
    ├── ISysUserWriteRepository.java
    └── ISysDeptRepository.java

# 应用层（kbpd-upms-application）
com.kava.kbpd.upms.application
├── model/
│   ├── command/             # 命令对象
│   │   └── SysUserCreateCommand.java
│   │   └── SysUserUpdateCommand.java
│   └── dto/                 # 应用层 DTO
│       ├── SysUserAppDetailDTO.java
│       └── SysUserAppListDTO.java
├── converter/               # App 层转换器
│   └── SysUserAppConverter.java
└── service/                 # 应用服务
    ├── ISysUserAppService.java
    └── impl/
        └── SysUserAppService.java

# 基础设施层（kbpd-upms-infrastructure）
com.kava.kbpd.upms.infrastructure
├── dao/                     # MyBatis Mapper
│   ├── SysUserMapper.java
│   └── po/                  # 持久化对象
│       └── SysUserPO.java
├── adapter/repository/      # 仓储实现
│   ├── SysUserReadRepository.java
│   ├── SysUserWriteRepository.java
│   └── SysDeptRepository.java
└── converter/               # MapStruct 转换器
    └── SysUserConverter.java

# API 层（kbpd-upms-api）
com.kava.kbpd.upms.api
├── model/
│   ├── request/             # 入参请求
│   │   └── SysUserRequest.java
│   ├── response/            # 出参响应
│   │   └── SysUserDetailResponse.java
│   ├── query/               # 列表查询
│   │   └── SysUserAdapterListQuery.java
│   └── dto/                 # RPC 传输对象
│       └── SysUserDTO.java
└── service/                 # Dubbo 接口
    └── IRemoteUserService.java

# 适配器层（kbpd-upms-adapter）
com.kava.kbpd.upms.adapter
├── controller/              # HTTP 控制器
│   └── SysUserController.java
└── rpc/                     # Dubbo 服务实现
    └── RemoteUserService.java
```

## 2.6 防腐层（Anticorruption Layer）

> **⚠️ 尚未落地，规划中**
>
> 当前项目处于初期调试阶段，跨服务调用直接通过 Dubbo 接口完成。防腐层将在链路稳定后逐步引入。

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
