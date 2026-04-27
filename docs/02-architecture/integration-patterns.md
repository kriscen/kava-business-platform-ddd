# 服务间通信与集成模式

## 4.1 通信架构总览

```
                    ┌──────────────────┐
                    │   kbpd-gateway   │
                    │  (HTTP 反向代理)  │
                    └────────┬─────────┘
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
```

**当前状态**：仅使用 Dubbo RPC 同步调用，无 MQ 异步通信。Auth 是唯一的 RPC 消费方，UPMS 和 Member 是纯提供方。

## 4.2 Dubbo RPC 规范

### 4.2.1 端口分配

| 服务 | Dubbo 协议端口 | QoS 端口 | 应用名 |
|------|--------------|---------|--------|
| kbpd-auth | 20600 | 22600 | kbpd-auth |
| kbpd-upms | 20610 | 22610 | kbpd-upms |
| kbpd-member | 20620 | 22620 | kbpd-member |

### 4.2.2 服务版本管理

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

### 4.2.3 接口定义规范

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

### 4.2.4 当前已注册接口

| 接口 | 模块 | 方法 | 消费方 |
|------|------|------|--------|
| `IRemoteOauthClientService` | kbpd-upms-api | `queryByClientId(String)` → `SysOauthClientDTO` | kbpd-auth |
| `IRemoteUserService` | kbpd-upms-api | `findByUsername(String, String)` → `SysUserDTO`<br>`loginByPwd(String, String)` → `SysUserDTO` | kbpd-auth |
| `IRemoteMemberService` | kbpd-member-api | `findMemberByMobile(String, String)` → `MemberInfoDTO` | kbpd-auth |

### 4.2.5 注册中心

所有服务使用 Nacos 作为注册中心：

```yaml
dubbo:
  registry:
    address: nacos://localhost:8848
  consumer:
    check: false    # 启动时不强制检查服务可用性
```

`consumer.check: false` 允许服务在依赖服务未启动时仍能正常启动。

### 4.2.6 超时与容错

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

## 4.3 Gateway 路由配置

### 4.3.1 当前路由规则

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

### 4.3.2 API 版本管理

各服务通过配置属性统一管理 API 路径版本前缀：

```yaml
app:
  config:
    api-version: v1
```

Controller 中引用：

```java
@RequestMapping("/api/${app.config.api-version}/sys/user/")
```

变更加版本时修改配置即可，无需逐个修改 Controller。

## 4.4 跨服务错误处理

### 4.4.1 错误传播规则

```
提供方抛出异常 → Dubbo 序列化异常 → 消费方捕获
```

| 异常层级 | 类 | 位置 | 用途 |
|---------|-----|------|------|
| 基础业务异常 | `BaseBizException` | kbpd-common-core | 含 errorCode + errorMsg |
| 领域业务异常 | `UpmsBizException` | kbpd-upms-types | UPMS 特定异常 |

**当前状态**：RPC 调用方未做显式异常捕获，异常直接上抛。后续需在消费方添加异常转换。

### 4.4.2 错误处理建议模式

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
        throw new ApplicationException("用户服务不可用，请稍后重试");
    } catch (BaseBizException e) {
        // 业务异常，转换为本域错误码
        throw new ApplicationException(e.getErrorCode(), e.getErrorMsg());
    }
}
```

## 4.5 事件驱动架构

### 4.5.1 当前状态

> **🟡 规划中，基础设施已预留**

- `BaseEvent<T>` 基类已定义（`kbpd-common-core`），支持 `topic()` 和 `EventMessage<T>`
- 无 MQ 依赖（无 RocketMQ/Kafka/RabbitMQ）
- 无 `ApplicationEventPublisher` 使用
- 无 `@EventListener` 实现
- 聚合根未集成领域事件收集能力

### 4.5.2 事件格式约定

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

### 4.5.3 引入 MQ 后的约定

| 约定 | 说明 |
|------|------|
| 消费幂等 | 所有消费方必须支持重复消费，通过事件 ID 去重 |
| 事件不可变 | 事件发布后不可修改，格式变更通过新 topic 实现 |
| 本地消息表 | 建议使用本地消息表 + 定时补偿，确保消息不丢失 |
| 消费失败处理 | 重试 3 次后进入死信队列，人工介入 |

## 4.6 规划状态

| 能力 | 状态 | 说明 |
|------|------|------|
| Dubbo RPC 同步调用 | ✅ 已实现 | Auth 消费 UPMS/Member 接口 |
| 接口版本管理 | ✅ 已实现 | Dubbo version + API path prefix |
| Nacos 服务注册 | ✅ 已实现 | 所有服务已注册 |
| 超时/重试/容错配置 | 🟡 待补充 | 当前使用 Dubbo 默认值 |
| RPC 异常处理 | 🟡 待补充 | 消费方无显式异常捕获 |
| Gateway 服务发现路由 | 🟡 待补充 | 当前硬编码 localhost |
| Gateway 鉴权/限流/熔断 | 🟡 规划中 | 依赖未启用 |
| MQ 异步通信 | 🟡 规划中 | BaseEvent 已预留，MQ 未引入 |
| 分布式事务（Saga） | 🟡 规划中 | 等事件驱动架构落地 |
