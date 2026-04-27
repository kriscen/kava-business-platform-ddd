# kbpd-gateway -- API 网关

## 模块定位

kbpd-gateway 是基于 Spring Cloud Gateway 的反应式 API 网关，作为整个平台的统一流量入口。它负责请求路由转发，并通过 Nacos 实现服务发现与集中配置管理。

当前为早期脚手架阶段，仅包含基础路由规则，尚未接入安全过滤、限流熔断等横切关注点。

**核心职责：**

- 统一流量入口，按路径前缀将请求路由至下游微服务
- 通过 Nacos Config 实现集中化配置管理
- 通过 Nacos Discovery 支持服务发现（生产模式下使用 `lb://` 负载均衡）

**不属于本模块的职责：**

- 用户认证与授权（由 `kbpd-auth` 负责）
- 业务逻辑处理（由各业务微服务负责）
- 细粒度权限校验（由各服务的 `kbpd-common-security` 负责）

---

## 技术选型

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Cloud Gateway | 2023.0.6 (Spring Cloud BOM) | 反应式 API 网关，基于 Netty |
| Spring Cloud Alibaba Nacos Discovery | 2023.0.3.2 | 服务注册与发现 |
| Spring Cloud Alibaba Nacos Config | 2023.0.3.2 | 集中化配置管理 |
| Java | 21 | 运行时 |

---

## 目录结构

```
kbpd-gateway/
├── pom.xml                        # Maven 构建配置，含 3 个核心依赖
└── src/main/
    ├── java/com/kava/kbpd/gateway/
    │   └── GatewayApplication.java  # Spring Boot 启动类
    └── resources/
        ├── application.yml         # 主配置：端口、Nacos 连接、配置导入
        ├── application-dev.yml     # 开发环境：路由规则、日志级别
        └── logback-spring.xml      # 日志配置：控制台 + 滚动文件
```

---

## 架构设计

### 请求路由流程

```
Client Request
     │
     ▼
┌──────────────┐
│   Netty      │  port: 8500
│   Server     │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│  Path Route  │  /auth/**  ──→  http://localhost:8600  (kbpd-auth)
│  Predicate   │  /upms/**  ──→  http://localhost:8610  (kbpd-upms)
│  Matching    │  /member/**──→  http://localhost:8620  (kbpd-member)
└──────┬───────┘
       │
       ▼
┌──────────────┐
│  Forward     │  直接转发（dev 模式无负载均衡）
│  Proxy       │
└──────────────┘
```

### 路由规则

当前路由定义于 `application-dev.yml`，仅使用 `Path` 断言：

| 路由 ID | 路径断言 | 目标地址 | 下游端口 | 说明 |
|---------|---------|---------|---------|------|
| `kbpd-auth` | `/auth/**` | `http://localhost:8600` | 8600 | 认证授权服务 |
| `kbpd-upms` | `/upms/**` | `http://localhost:8610` | 8610 | 用户权限管理服务 |
| `kbpd-member` | `/member/**` | `http://localhost:8620` | 8620 | 会员服务 |

> **注意：** dev 模式下路由使用硬编码的 HTTP 地址（`http://localhost:PORT`），未使用 Nacos 服务发现。
> 生产模式应使用 `lb://kbpd-auth` 格式以启用负载均衡。

### 配置加载策略

网关配置分为本地文件和 Nacos 远程配置两部分：

```
application.yml                     ← 本地：端口、Nacos 连接信息
    │
    ├── optional:nacos:application-dev.yml       ← Nacos 共享配置
    │
    └── optional:nacos:kbpd-gateway-dev.yml      ← Nacos 网关专属配置
```

- `optional:` 前缀确保 Nacos 不可用时不阻塞启动
- Nacos 配置可能包含生产路由规则、全局过滤器等（不在源码仓库中）

### 日志配置

`logback-spring.xml` 定义了三个 Appender：

| Appender | 输出目标 | 文件路径 | 滚动策略 |
|----------|---------|---------|---------|
| `console` | 标准输出 | — | — |
| `debug` | 滚动文件 | `logs/kbpd-gateway/debug.log` | 50MB/文件，30 天保留，gzip 压缩 |
| `error` | 滚动文件 | `logs/kbpd-gateway/error.log` | 50MB/文件，30 天保留，gzip 压缩 |

当前 Root Logger 仅挂载 `console`，`debug` 和 `error` 文件 Appender 已定义但未引用。

---

## 配置项

主要配置在 `application.yml` 和 `application-dev.yml` 中：

### 服务基础配置（`application.yml`）

| 属性 | 值 | 说明 |
|------|------|------|
| `server.port` | `8500` | 网关监听端口 |
| `spring.application.name` | `@artifactId@`（Maven 过滤为 `kbpd-gateway`） | 服务名 |
| `spring.cloud.nacos.discovery.server-addr` | `localhost:8848` | Nacos 地址 |
| `spring.cloud.nacos.discovery.namespace` | `92c33821-...` | Nacos 命名空间 ID |

### 网关路由配置（`application-dev.yml`）

路由通过标准 Spring Cloud Gateway YAML DSL 定义，格式如下：

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: <路由ID>
          uri: <目标地址>
          predicates:
            - Path=<路径匹配模式>
```

---

## 运行

```bash
# 确保依赖已构建
mvn clean install -DskipTests

# 启动网关（需要 Nacos 先运行）
cd kbpd-gateway && mvn spring-boot:run
```

- **端口：** `8500`
- **前置条件：** Nacos 服务（`localhost:8848`）和下游微服务需已启动

---

## 当前开发状态（TODO）

| 功能 | 状态 | 说明 |
|------|------|------|
| 基础路由转发 | ✅ 已实现 | 3 条静态路由覆盖 auth/upms/member |
| Nacos 配置集成 | ✅ 已实现 | 本地 + 远程双层配置 |
| 服务发现负载均衡 | ❌ 未启用 | dev 模式使用硬编码地址，需改为 `lb://` |
| 认证过滤（OAuth2 Resource Server） | ❌ 未启用 | 依赖已在 pom.xml 中注释 |
| 全局日志/追踪 Filter | ❌ 未实现 | 无自定义 GlobalFilter |
| 限流熔断（Sentinel/Resilience4j） | ❌ 未实现 | — |
| CORS 配置 | ❌ 未实现 | — |
| 全局异常处理 | ❌ 未实现 | — |
| 健康检查 / Actuator | ❌ 未实现 | — |
