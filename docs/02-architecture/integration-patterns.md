# 数据交互与通信模式

本文档定义系统内所有数据交互方式，涵盖内部层间调用、服务内 HTTP、跨服务 RPC、以及未来异步通信协议。

## 4.1 交互架构总览

```
外部请求流（从左到右）：
                                                       ┌─────────────────────────────────┐
                                                       │         kbpd-gateway            │
                                                       │        (HTTP 反向代理)            │
                                                       └───────────────┬─────────────────┘
                                                       │ HTTP 路由
                                        ┌──────────────┼──────────────┐
                                        v              v              v
                               ┌────────────┐  ┌───────────┐  ┌────────────┐
                               │ kbpd-auth   │  │ kbpd-upms │  │ kbpd-member│
                               │ (Consumer)  │  │ (Provider)│  │ (Provider) │
                               └──────┬──────┘  └─────▲─────┘  └─────▲──────┘
                                      │ Dubbo RPC       │              │
                                      ├─────────────────┘              │
                                      └────────────────────────────────┘

服务内部流（从上到下）：

  HTTP ──→ adapter ──→ application ──→ domain ──→ infrastructure
                     ↑               ↑            ↑
                  编排协调         业务规则       技术实现
```

**通信类型分类**：

| 类型 | 范围 | 当前方式 | 文档章节 |
|------|------|---------|---------|
| 内部层间调用 | 单服务内各层之间 | 方法调用 | [4.2](#42-内部层间交互) |
| HTTP | 外部客户端 → 服务 | REST API | [4.3](#43-http-通信规范) |
| Dubbo RPC | 服务 ↔ 服务 | 同步调用 | [4.4](#44-dubbo-rpc-规范) |
| Gateway 路由 | 客户端 → 服务 | HTTP 反向代理 | [4.5](#45-gateway-路由配置) |
| 异步/事件 | 服务 → 服务（解耦） | 规划中（MQ） | [4.7](#47-异步通信与事件驱动) |
| IoT/MQTT | 设备 → 服务 | 规划中 | [4.8](#48-通信协议扩展框架) |

## 4.2 内部层间交互

### 4.2.1 两种调用模式

根据模块的业务复杂度，应用层有两种调用模式。**关键区分标准是：该模块是否需要承载业务规则和跨聚合协调逻辑。**

```
模式 A（简单 CRUD）：Application 直接调用 Repository

┌──────────────┐         ┌──────────────────┐         ┌──────────────┐
│  Controller   │────────→│  AppService       │────────→│  Repository   │
│  (adapter)    │         │  (application)    │         │  (infra impl) │
└──────────────┘         └──────────────────┘         └──────────────┘
                               │
                          转换 + 简单编排
                          不涉及复杂业务规则

典型模块：Area、Dept、OauthClient


模式 B（复杂业务）：Application → DomainService → Repository

┌──────────────┐   ┌──────────────┐   ┌────────────────┐   ┌──────────────┐
│  Controller   │──→│  AppService   │──→│  DomainService  │──→│  Repository   │
│  (adapter)    │   │  (application)│   │  (domain)       │   │  (infra impl) │
└──────────────┘   └──────────────┘   └────────────────┘   └──────────────┘
                          │                    │
                     编排协调              业务规则校验
                     事务边界              跨聚合协调

典型模块：User、Role（当前为初期阶段，逐步演进中）
```

**选择标准**：

| 场景 | 模式 | 说明 |
|------|------|------|
| 纯数据维护，无复杂业务规则 | A：AppService → Repository | 如地区管理、字典维护 |
| 需要特定领域逻辑（非 CRUD） | A + 局部调 DomainService | 如 Area 的树构建，CRUD 走 Repo，树走 Service |
| 跨聚合根协调、复杂业务流程 | B：AppService → DomainService → Repository | 如用户注册需校验角色、分配权限等 |

> **注意**：模式 A 不意味着没有 domain 层。即使简单模块，domain 层仍然定义 Entity、Repository 接口和值对象。只是 application 层无需经过 DomainService 做透传。

### 4.2.2 模式 A 示例：简单 CRUD（SysAreaAppService）

简单模块直接调 Repository，业务逻辑透明：

```java
// application 层 — 直接调用仓储，无中间领域服务
@Slf4j
@Service
public class SysAreaAppService implements ISysAreaAppService {
    @Resource
    private ISysAreaRepository sysAreaRepository;
    @Resource
    private ISysAreaService sysAreaService;          // 仅在需要特定领域逻辑时使用
    @Resource
    private SysAreaAppConverter sysAreaAppConverter;

    // CRUD 操作 — 直接调 Repository
    public SysAreaId createArea(SysAreaCreateCommand command) {
        SysAreaEntity entity = sysAreaAppConverter.convertCreateCommand2Entity(command);
        return sysAreaRepository.create(entity);     // 直接 → Repository
    }

    public PagingInfo<SysAreaAppListDTO> queryAreaPage(SysAreaListQuery query) {
        PagingInfo<SysAreaEntity> pagingInfo = sysAreaRepository.queryPage(query);
        // ... convert and return
    }

    // 非常规操作 — 走 DomainService（含树构建等领域逻辑）
    public List<Tree<Long>> selectAreaTree(SysAreaListQuery query) {
        return sysAreaService.selectAreaTree(query);  // → DomainService → Repository
    }
}
```

**对应 DomainService 仅承载特定的领域逻辑**：

```java
// domain 层 — 只定义非常规 CRUD 的业务方法
@Service
public class SysAreaService implements ISysAreaService {
    @Resource
    private ISysAreaRepository repository;

    public List<Tree<Long>> selectAreaTree(SysAreaListQuery query) {
        List<SysAreaEntity> entityList = repository.selectTreeList(query);
        // 构建树形结构 — 这是领域逻辑，不属于简单 CRUD
        return TreeUtil.build(nodeList, defaultPid);
    }
}
```

**模式 A 的要点**：
- CRUD 操作不经过 DomainService，避免空壳透传
- DomainService 只在有真正业务逻辑时才定义方法
- 当前使用：Area、Dept、Tenant、OauthClient、Menu

### 4.2.3 模式 B 示例：复杂业务（SysUserAppService，演进中）

复杂模块**目标**是 AppService → DomainService → Repository。当前因项目初期，DomainService 为空壳，AppService 暂时直接调 Repository，后续逐步将业务逻辑下沉到 DomainService。

**当前状态（初期）**：

```java
// application 层 — 暂时直接调 Repository（ISysUserService 已预留但为空）
@Slf4j
@Service
public class SysUserAppService implements ISysUserAppService {
    @Resource
    private ISysUserReadRepository readRepository;
    @Resource
    private ISysUserWriteRepository writeRepository;
    @Resource
    private ISysUserService sysUserService;          // 已注入，当前为空壳，待后续演进
    @Resource
    private SysUserAppConverter sysUserAppConverter;

    public SysUserId createUser(SysUserCreateCommand command) {
        // TODO: 后续改为 sysUserService.createUser(entity)
        return writeRepository.create(sysUserAppConverter.convertCreateCommand2Entity(command));
    }
}
```

```java
// domain 层 — 空壳，待业务逻辑丰富后填充
@Service
public class SysUserService implements ISysUserService {
    @Resource
    private ISysUserWriteRepository writeRepository;
    @Resource
    private ISysUserReadRepository readRepository;

    // TODO: 后续在此添加业务方法，如：
    // - registerUserWithDefaultRole()
    // - lockUser()
    // - assignRole()
}
```

**演进后目标**：

```java
// application 层 — 不再直接调 Repository，通过 DomainService 编排
@Slf4j
@Service
public class SysUserAppService implements ISysUserAppService {
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private SysUserAppConverter sysUserAppConverter;

    public SysUserId createUser(SysUserCreateCommand command) {
        SysUserEntity entity = sysUserAppConverter.convertCreateCommand2Entity(command);
        return sysUserService.createUser(entity);     // → DomainService → Repository
    }
}

// domain 层 — 承载业务规则
@Service
public class SysUserService implements ISysUserService {
    @Resource
    private ISysUserWriteRepository writeRepository;
    @Resource
    private ISysRoleReadRepository roleReadRepository;

    public SysUserId createUser(SysUserEntity user) {
        // 业务规则：用户名唯一性校验、默认角色分配等
        user.assignRole(defaultRoleId);
        return writeRepository.create(user);
    }

    public void lockUser(SysUserId userId) {
        SysUserEntity user = readRepository.queryById(userId);
        user.lock();                                   // 聚合根内业务规则
        writeRepository.update(user);
    }
}
```

**演进原则**：

| 原则 | 说明 |
|------|------|
| 逐步迁移 | 不需要一次性改完，新增业务逻辑时直接写在 DomainService |
| 新方法优先走 Service | 新增的业务方法直接在 DomainService 中实现，不再直接调 Repository |
| 查询可豁免 | 简单的按 ID 查询、分页查询可保留在 AppService 直接调 Repository |
| DomainService 不透传 | 如果 DomainService 方法只是 `return repository.xxx()`，说明还不需要这个方法 |

### 4.2.4 各层职责边界

| 层 | 允许做的事 | 禁止做的事 |
|----|-----------|-----------|
| **adapter** | 参数校验（@Valid）、格式转换、调用 application | 包含业务逻辑、直接调用 repository |
| **application** | 编排流程、事务控制（@Transactional）、调用 domain service 或 repository（模式 A） | 实现业务规则、直接操作数据库 |
| **domain** | 业务规则校验、跨聚合协调 | 依赖基础设施细节 |
| **infrastructure** | 数据库操作、外部服务调用 | 包含业务逻辑 |

### 4.2.5 仓储接口策略

项目支持两种仓储接口，按实体复杂度选择：

```
CQRS 分离型（复杂实体，如 User、Role）：
  ISysUserReadRepository   → queryPage, queryById
  ISysUserWriteRepository  → create, update, removeBatchByIds

简单合并型（简单实体，如 Area、Dept、Tenant、OauthClient）：
  ISysAreaRepository extends IBaseSimpleRepository<...>
    → create, update, removeById, queryPage, queryById
```

| 策略 | 基类 | 适用场景 | 当前使用 |
|------|------|---------|---------|
| CQRS | `IBaseReadRepository` + `IBaseWriteRepository` | 读写模型差异大、读写量差异大 | User, Role |
| 简单 | `IBaseSimpleRepository` | 简单 CRUD，读写模型一致 | Area, Dept, Tenant, OauthClient, Menu |

## 4.3 HTTP 通信规范

### 4.3.1 请求链路

```
客户端 → Gateway → Controller → AdapterConverter → AppService → ...
```

完整请求示例（GET `/api/v1/sys/user/page`）：

```
SysUserController.queryUserPage()
  │
  ├→ SysUserAdapterConverter.convertQueryDTO2QueryVal()     // Request → 领域 Query
  ├→ ISysUserAppService.queryUserPage()                     // 编排
  │    ├→ ISysUserReadRepository.queryPage()                // 查询
  │    │    ├→ SysUserMapper.selectPage()                   // MyBatis-Plus
  │    │    └→ SysUserConverter.convertPO2Entity()          // PO → Entity
  │    └→ SysUserAppConverter.convertEntity2DTO()           // Entity → AppDTO
  │
  ├→ SysUserAdapterConverter.convertDTO2List()              // AppDTO → Response
  └→ JsonResult.buildSuccess(response)                      // 统一包装
```

### 4.3.2 Controller 规范

所有 Controller 遵循统一 5 端点 CRUD 模式：

```java
@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/{entity}/")
public class SysUserController {

    @Resource
    private ISysUserAppService sysUserAppService;
    @Resource
    private SysUserAdapterConverter sysUserAdapterConverter;

    @GetMapping("/page")
    public JsonResult<PagingInfo<SysUserListResponse>> queryPage(
            SysUserAdapterListQuery query) { ... }

    @GetMapping("/{id}")
    public JsonResult<SysUserDetailResponse> queryById(@PathVariable Long id) { ... }

    @PostMapping("/")
    public JsonResult<Boolean> create(@RequestBody SysUserRequest request) { ... }

    @PutMapping("/")
    public JsonResult<Boolean> update(@RequestBody SysUserRequest request) { ... }

    @DeleteMapping("/")
    public JsonResult<Boolean> removeBatch(@RequestBody List<Long> ids) { ... }
}
```

| 端点 | HTTP 方法 | 路径 | 用途 |
|------|----------|------|------|
| 分页查询 | GET | `/page` | 带分页的列表查询 |
| 详情查询 | GET | `/{id}` | 按 ID 查详情 |
| 创建 | POST | `/` | 新建资源 |
| 更新 | PUT | `/` | 更新资源 |
| 批量删除 | DELETE | `/` | 按 ID 列表批量删除 |

### 4.3.3 统一响应格式

所有 HTTP 响应使用 `JsonResult<T>` 包装：

```java
// 成功
JsonResult.buildSuccess(data)
→ { "success": true, "data": {...}, "errorCode": null, "errorMessage": null }

// 失败
JsonResult.buildFail("ERROR_CODE", "错误描述")
→ { "success": false, "data": null, "errorCode": "ERROR_CODE", "errorMessage": "错误描述" }
```

分页响应使用 `PagingInfo<T>`：

```java
// 字段：pageNo, pageSize, total, list
PagingInfo<SysUserEntity> pagingInfo = readRepository.queryPage(query);
```

### 4.3.4 数据转换链路

每次请求经过三层转换器，各层职责分明：

```
Request ──[AdapterConverter]──→ Command/Query ──[AppConverter]──→ Entity ──[Converter]──→ PO
                                                    ↑                         ↑
                                              application 层              infrastructure 层
                                              Command → Entity           PO ↔ Entity
                                              Entity → AppDTO

Response ←──[AdapterConverter]── AppDTO ←──[AppConverter]── Entity ←──[Converter]── PO ←── DB
```

| 转换器 | 所在层 | 职责 | 命名 |
|--------|-------|------|------|
| `SysUserAdapterConverter` | adapter | Request ↔ Command/Query, AppDTO ↔ Response | `{Entity}AdapterConverter` |
| `SysUserAppConverter` | application | Command → Entity, Entity → AppDTO | `{Entity}AppConverter` |
| `SysUserConverter` | infrastructure | PO ↔ Entity | `{Entity}Converter` |

### 4.3.5 API 版本管理

各服务通过配置属性统一管理版本前缀：

```yaml
app:
  config:
    api-version: v1
```

Controller 中引用：

```java
@RequestMapping("/api/${app.config.api-version}/sys/user/")
```

## 4.4 Dubbo RPC 规范

### 4.4.1 端口分配

| 服务 | Dubbo 协议端口 | QoS 端口 | 应用名 |
|------|--------------|---------|--------|
| kbpd-auth | 20600 | 22600 | kbpd-auth |
| kbpd-upms | 20610 | 22610 | kbpd-upms |
| kbpd-member | 20620 | 22620 | kbpd-member |

### 4.4.2 服务版本管理

所有 Dubbo 接口使用 `version = "1.0"`，在 `@DubboService` 和 `@DubboReference` 上显式声明。

```java
// 提供方
@DubboService(version = "1.0")
public class RemoteUserService implements IRemoteUserService { ... }

// 消费方
@DubboReference(version = "1.0")
private IRemoteUserService remoteUserService;
```

**版本演进规则**：

| 变更类型 | 处理方式 |
|---------|---------|
| 新增方法 | 在当前版本接口中直接添加，提供方先上线 |
| 修改方法签名 | 新增 `version = "2.0"` 接口，逐步迁移调用方 |
| 删除方法 | 新版本中移除，旧版本保留至所有调用方迁移完毕 |
| 新增接口 | 新建 Interface 类，独立版本号 |

### 4.4.3 接口定义规范

Dubbo 接口定义在各服务的 `api` 模块中：

```
kbpd-{module}-api/
└── src/main/java/com/kava/kbpd/{module}/api/
    ├── service/              # Dubbo 接口
    │   └── IRemote{Entity}Service.java
    └── model/
        └── dto/              # RPC 传输对象
            └── {Entity}DTO.java
```

**命名约定**：

| 类型 | 命名规则 | 示例 |
|------|---------|------|
| 接口 | `IRemote{Entity}Service` | `IRemoteUserService` |
| 实现 | `Remote{Entity}Service` | `RemoteUserService` |
| DTO | `{Entity}DTO` | `SysUserDTO` |

**接口设计原则**：

1. 接口方法参数和返回值使用 DTO，不暴露领域实体
2. DTO 放在 `api` 模块，供消费方直接依赖
3. 接口粒度按业务场景设计，避免 CRUD 式的通用接口

### 4.4.4 RPC 调用链路

```
消费方                                       提供方
─────────────────────                      ─────────────────────
@DubboReference                                @DubboService
IRemoteUserService                            RemoteUserService
       │                                              │
       │  Dubbo 协议 (TCP, 端口 20610)                │
       │──────────────────────────────────────────────→│
       │                                              ├→ ISysUserAppService (application 层)
       │                                              │    └→ ISysUserReadRepository → DB
       │                                              │
       │  SysUserDTO (序列化返回)                      ├→ SysOauthClientAdapterConverter
       │←──────────────────────────────────────────────┤    (Entity → DTO)
```

**关键约束**：RPC 实现类（`adapter.rpc` 包）只做两件事：
1. 调用 application 层服务
2. 用 adapter converter 将结果转换为 API 层 DTO

不包含业务逻辑。

### 4.4.5 当前已注册接口

| 接口 | 模块 | 方法 | 消费方 |
|------|------|------|--------|
| `IRemoteOauthClientService` | kbpd-upms-api | `queryByClientId(String)` → `SysOauthClientDTO` | kbpd-auth |
| `IRemoteUserService` | kbpd-upms-api | `findByUsername(String, String)` → `SysUserDTO`<br>`loginByPwd(String, String)` → `SysUserDTO` | kbpd-auth |
| `IRemoteMemberService` | kbpd-member-api | `findMemberByMobile(String, String)` → `MemberInfoDTO` | kbpd-auth |

### 4.4.6 注册中心

所有服务使用 Nacos 作为注册中心：

```yaml
dubbo:
  registry:
    address: nacos://localhost:8848
  consumer:
    check: false    # 启动时不强制检查服务可用性
```

### 4.4.7 超时与容错

**当前状态**：未显式配置超时和容错策略，使用 Dubbo 默认值。

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| timeout | 1000ms | 消费方超时时间 |
| retries | 2 | 消费方重试次数（不含首次调用） |
| cluster | failover | 集群容错策略 |

**建议配置**（后续补充到各服务 yml）：

```yaml
dubbo:
  consumer:
    timeout: 3000        # 建议设为 3 秒
    retries: 0           # 非幂等操作不要重试
    cluster: failfast    # 快速失败，适合查询类
```

| 场景 | cluster 策略 | retries | 说明 |
|------|-------------|---------|------|
| 查询类（幂等） | failover | 1 | 可自动重试其他节点 |
| 写入类（非幂等） | failfast | 0 | 快速失败，不重试 |
| 关键写入 | failfast | 0 | 调用方自行补偿 |

## 4.5 Gateway 路由配置

### 4.5.1 当前路由规则

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: kbpd-auth
          uri: http://localhost:8600
          predicates:
            - Path=/auth/**
        - id: kbpd-upms
          uri: http://localhost:8610
          predicates:
            - Path=/upms/**
        - id: kbpd-member
          uri: http://localhost:8620
          predicates:
            - Path=/member/**
```

**注意**：当前使用硬编码 `localhost` 地址，未使用 Nacos 服务发现路由（`lb://service-name`）。

## 4.6 跨服务错误处理

### 4.6.1 错误传播规则

```
提供方抛出异常 → Dubbo 序列化异常 → 消费方捕获
```

| 异常层级 | 类 | 位置 | 用途 |
|---------|-----|------|------|
| 基础业务异常 | `BaseBizException` | kbpd-common-core | 含 errorCode + errorMsg |
| 领域业务异常 | `UpmsBizException` | kbpd-upms-types | UPMS 特定异常 |

### 4.6.2 错误处理建议模式

```java
// RPC 消费方建议模式
@DubboReference(version = "1.0")
private IRemoteUserService remoteUserService;

public SysUserDTO getUser(String tenantId, String username) {
    try {
        return remoteUserService.findByUsername(tenantId, username);
    } catch (RpcException e) {
        // Dubbo 框架级异常（超时、网络）
        log.error("RPC 调用失败: service=IRemoteUserService, method=findByUsername", e);
        throw new BaseBizException("用户服务不可用，请稍后重试");
    } catch (BaseBizException e) {
        // 业务异常，转换为本域错误码
        throw new BaseBizException(e.getErrorCode(), e.getErrorMsg());
    }
}
```

## 4.7 异步通信与事件驱动

### 4.7.1 当前状态

> **规划中，基础设施已预留**

- `BaseEvent<T>` 基类已定义（`kbpd-common-core`），支持 `topic()` 和 `EventMessage<T>`
- 无 MQ 依赖（无 RocketMQ/Kafka/RabbitMQ）
- 无 `ApplicationEventPublisher` 使用
- 无 `@EventListener` 实现
- 聚合根未集成领域事件收集能力

### 4.7.2 事件格式约定

```java
// 事件基类结构（已定义）
public abstract class BaseEvent<T> {
    public abstract EventMessage<T> buildEventMessage(T data);
    public abstract String topic();

    @Data @Builder
    public static class EventMessage<T> {
        private String id;         // 事件唯一 ID
        private Date timestamp;    // 事件时间戳
        private T data;            // 事件负载
    }
}
```

### 4.7.3 引入 MQ 后的约定

| 约定 | 说明 |
|------|------|
| 消费幂等 | 所有消费方必须支持重复消费，通过事件 ID 去重 |
| 事件不可变 | 事件发布后不可修改，格式变更通过新 topic 实现 |
| 本地消息表 | 建议使用本地消息表 + 定时补偿，确保消息不丢失 |
| 消费失败处理 | 重试 3 次后进入死信队列，人工介入 |

### 4.7.4 领域事件流（规划）

```
聚合根操作 → 注册领域事件 → AppService 发布事件 → MQ → 消费方处理

示例：
SysUserEntity.changePassword()
  → registerEvent(new PasswordChangedEvent(userId))
  → AppService.save() 后发布
  → MQ Topic: "user.password.changed"
  → 消费方：通知服务（发送邮件/SMS）
```

## 4.8 通信协议扩展框架

本节为未来通信协议预留扩展规范。每个新协议接入时应遵循以下统一约束。

### 4.8.1 统一接入约束

无论使用何种通信协议，所有接入方式必须遵守：

| 约束 | 说明 |
|------|------|
| **不暴露领域层** | 外部通信只能在 adapter 层处理，转换后方可进入 application 层 |
| **统一异常体系** | 所有协议的异常最终转为 `BaseBizException`，由全局异常处理器统一响应 |
| **统一数据转换** | 每种协议有独立的 converter，负责外部格式 ↔ application Command/DTO 互转 |
| **统一响应格式** | 所有协议的响应体使用 `JsonResult<T>` 包装（如适用） |

### 4.8.2 MQTT（规划中）

适用于 IoT 设备接入、实时消息推送等场景。

**目录结构**（接入时创建）：

```
kbpd-{module}-adapter/
└── src/main/java/com/kava/kbpd/{module}/adapter/
    └── mqtt/                              # MQTT 适配器
        ├── handler/                       # 消息处理器
        │   └── {Entity}MqttHandler.java
        └── converter/                     # MQTT 消息转换器
            └── {Entity}MqttConverter.java
```

**接入约定**：

```java
// 消息处理器 — 在 adapter 层，不包含业务逻辑
@Component
@Slf4j
public class DeviceMqttHandler {

    @Resource
    private IDeviceAppService deviceAppService;
    @Resource
    private DeviceMqttConverter deviceMqttConverter;

    @MQTTSubscribe(topic = "device/${deviceId}/status")
    public void handleStatusUpdate(MqttMessage message) {
        DeviceStatusCommand command = deviceMqttConverter.toCommand(message);
        deviceAppService.updateStatus(command);
    }
}
```

| 约定 | 说明 |
|------|------|
| Topic 命名 | `{模块}/{实体}/{动作}`，如 `device/sensor/report` |
| QoS 级别 | 默认 QoS 1（至少一次），关键控制指令 QoS 2 |
| 消息格式 | JSON，含 `messageId`、`timestamp`、`type`、`data` 字段 |
| 幂等处理 | 消费方通过 `messageId` 去重 |

### 4.8.3 WebSocket（规划中）

适用于实时双向通信场景（如在线状态、实时通知）。

**目录结构**：

```
kbpd-{module}-adapter/
└── src/main/java/com/kava/kbpd/{module}/adapter/
    └── websocket/                         # WebSocket 适配器
        ├── handler/                       # 连接处理器
        │   └── {Entity}WsHandler.java
        └── converter/
            └── {Entity}WsConverter.java
```

| 约定 | 说明 |
|------|------|
| 消息格式 | JSON，含 `type`（事件类型）+ `data`（负载） |
| 认证 | 连接时通过 token 参数认证 |
| 心跳 | 客户端每 30s 发 ping，服务端 pong，超时 60s 断开 |
| 重连 | 客户端指数退避重连 |

### 4.8.4 新协议接入 Checklist

引入新通信协议时，确保完成以下步骤：

- [ ] 在 `adapter` 层创建对应子包（如 `adapter/mqtt/`、`adapter/websocket/`）
- [ ] 实现专用的 converter，负责外部格式 ↔ application 层对象互转
- [ ] handler 只调用 application 层服务，不直接操作 domain/infrastructure
- [ ] 异常统一转为 `BaseBizException`
- [ ] 在本文档对应章节补充协议规范
- [ ] 更新 [4.1 交互架构总览](#41-交互架构总览) 中的通信类型分类表

## 4.9 规划状态

| 能力 | 状态 | 说明 |
|------|------|------|
| 内部层间调用规范 | ✅ 已实现 | AppService → Repository（简单）/ DomainService（复杂） |
| HTTP REST API | ✅ 已实现 | 统一 5 端点 CRUD 模式 + JsonResult 包装 |
| Dubbo RPC 同步调用 | ✅ 已实现 | Auth 消费 UPMS/Member 接口 |
| 接口版本管理 | ✅ 已实现 | Dubbo version + API path prefix |
| Nacos 服务注册 | ✅ 已实现 | 所有服务已注册 |
| 超时/重试/容错配置 | 🟡 待补充 | 当前使用 Dubbo 默认值 |
| RPC 异常处理 | 🟡 待补充 | 消费方无显式异常捕获 |
| Gateway 服务发现路由 | 🟡 待补充 | 当前硬编码 localhost |
| Gateway 鉴权/限流/熔断 | 🟡 规划中 | 依赖未启用 |
| MQ 异步通信 | 🟡 规划中 | BaseEvent 已预留，MQ 未引入 |
| MQTT IoT 接入 | 🟡 规划中 | 框架已定义，待引入依赖 |
| WebSocket 实时通信 | 🟡 规划中 | 框架已定义 |
| 分布式事务（Saga） | 🟡 规划中 | 等事件驱动架构落地 |
