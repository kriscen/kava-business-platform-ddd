# kbpd-member -- 会员服务

## 模块定位

kbpd-member 是基于 DDD 分层架构的会员管理微服务，负责平台会员的注册、信息管理、角色权限等核心业务。

当前为**早期脚手架阶段**，仅搭建了完整的 DDD 分层目录结构和基础的 Dubbo RPC 通信骨架，尚未实现具体的业务逻辑。领域层、应用层、基础设施层均为空目录占位。

**核心职责（规划）：**

- 会员注册与管理（手机号、基本信息）
- 会员角色与权限管理
- 会员信息查询（供其他微服务通过 RPC 调用）

**不属于本模块的职责：**

- 用户认证与授权（由 `kbpd-auth` 负责）
- 后台用户权限管理（由 `kbpd-upms` 负责）
- API 路由转发（由 `kbpd-gateway` 负责）

---

## 技术选型

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.3.13 | 应用框架 |
| Spring Cloud Alibaba Nacos | 2023.0.3.2 | 服务注册发现 & 配置管理 |
| Apache Dubbo | 3.3.x | 微服务间 RPC 通信 |
| MyBatis-Plus | 3.5.x | 数据访问层（已引入依赖，未使用） |
| MySQL | — | 数据库（`kava-member`） |
| Redis | — | 缓存（已配置连接，未使用） |
| MapStruct | — | 对象映射（已引入依赖，未使用） |
| Java | 21 | 运行时 |

---

## 目录结构

```
kbpd-member/
├── pom.xml                           # 父 POM，定义 7 个子模块
├── kbpd-member-api/                  # RPC 契约：DTO、服务接口
│   └── src/main/java/
│       └── com/kava/kbpd/member/api/
│           ├── model/dto/
│           │   └── MemberInfoDTO.java    # 会员信息 DTO（id, roles, permissions）
│           └── service/
│               └── IRemoteMemberService.java  # Dubbo RPC 服务接口
│
├── kbpd-member-types/                # 枚举、常量（空）
├── kbpd-member-domain/               # 领域层：实体、聚合根、仓储接口（空）
├── kbpd-member-application/          # 应用层：应用服务（仅占位 App.java）
├── kbpd-member-infrastructure/       # 基础设施层：持久化、外部服务（空）
│
├── kbpd-member-adapter/              # 适配器层：RPC Provider、HTTP Controller
│   └── src/main/java/
│       └── com/kava/kbpd/member/trigger/
│           ├── rpc/
│           │   └── RemoteMemberService.java   # Dubbo RPC 实现（桩方法）
│           └── http/
│               ├── admin/              # 后台管理端 Controller（空）
│               └── app/                # 移动端/App Controller（空）
│
└── kbpd-member-bootstrap/            # Spring Boot 启动模块
    └── src/main/
        ├── java/com/kava/kbpd/member/
        │   ├── MemberApplication.java       # 启动类（@EnableDubbo）
        │   └── config/
        │       └── MemberSecurityConfig.java  # 安全配置（空）
        └── resources/
            ├── application.yml              # 主配置：端口、Nacos
            ├── application-dev.yml          # 开发环境：数据源、Dubbo、Redis
            └── logback-spring.xml           # 日志配置
```

---

## 架构设计

### DDD 分层与依赖方向

```
                    依赖方向
                    ────────→

 adapter          application          domain          infrastructure
 (触发器层)         (应用层)            (领域层)          (基础设施层)
 ┌──────────┐     ┌──────────┐     ┌──────────┐     ┌──────────┐
 │ RPC      │     │          │     │          │     │          │
 │ Provider │────→│ App Svc  │────→│ Entity   │←────│ Mapper   │
 │ HTTP Ctrl│     │          │     │ Aggregate│     │ PO       │
 └──────────┘     └──────────┘     │ Repo IF  │     │ Converter│
       ↑                             └──────────┘     └──────────┘
       │                                  ↑                  │
  kbpd-member-api                        │                  │
  (DTO, 服务接口)                         └──────────────────┘

  kbpd-member-types ← 被所有层引用（枚举、常量）
  kbpd-member-bootstrap → 组装所有层，提供启动入口
```

### 当前已实现的通信路径

```
其他微服务（如 kbpd-auth）
       │
       │ Dubbo RPC (dubbo://localhost:20620)
       ▼
┌──────────────────────┐
│ RemoteMemberService  │  adapter 层
│ (version: "1.0")     │
│                      │
│ findMemberByMobile() │  ← 返回桩数据（硬编码 id=1）
└──────────────────────┘
```

> **注意：** 当前 RPC 实现为桩方法，直接返回硬编码数据，尚未调用领域层或持久层。

### 子模块依赖关系

```
kbpd-member-types
    ↓
kbpd-member-api  ←── (types, jakarta.validation-api)
    ↓
kbpd-member-domain ←── (types, commons-lang3, guava)
    ↓
kbpd-member-application ←── (domain)
    ↓
kbpd-member-infrastructure ←── (domain, mybatis-plus)
    ↓
kbpd-member-adapter ←── (types, application, api, common-security, dubbo)
    ↓
kbpd-member-bootstrap ←── (adapter, infrastructure, mysql, nacos, redis)
```

---

## 配置项

### 服务基础配置（`application.yml`）

| 属性 | 值 | 说明 |
|------|------|------|
| `server.port` | `8620` | HTTP 服务端口 |
| `server.servlet.context-path` | `/member` | 上下文路径 |
| `spring.application.name` | `@artifactId@`（解析为 `kbpd-member`） | 服务名 |
| `spring.cloud.nacos.discovery.server-addr` | `127.0.0.1:8848` | Nacos 地址 |
| `spring.cloud.nacos.discovery.namespace` | `92c33821-...` | Nacos 命名空间 |

### 开发环境配置（`application-dev.yml`）

| 属性 | 值 | 说明 |
|------|------|------|
| `spring.datasource.url` | `jdbc:mysql://127.0.0.1:3306/kava-member` | MySQL 连接 |
| `spring.datasource.username` | `root` | 数据库用户名 |
| `spring.datasource.password` | `root` | 数据库密码 |
| `dubbo.protocol.port` | `20620` | Dubbo 协议端口 |
| `dubbo.protocol.qos-port` | `22620` | Dubbo QoS 端口 |
| `dubbo.registry.address` | `nacos://127.0.0.1:8848` | 注册中心地址 |
| `spring.data.redis.host` | `127.0.0.1` | Redis 地址 |
| `spring.data.redis.port` | `6379` | Redis 端口 |

---

## 运行

```bash
# 确保依赖已构建
mvn clean install -DskipTests

# 启动会员服务（需要 Nacos、MySQL 先运行）
cd kbpd-member/kbpd-member-bootstrap && mvn spring-boot:run
```

- **HTTP 端口：** `8620`（context-path: `/member`）
- **Dubbo 端口：** `20620`
- **前置条件：** Nacos（`localhost:8848`）、MySQL（`kava-member` 库）

---

## 当前开发状态（TODO）

| 功能 | 状态 | 说明 |
|------|------|------|
| DDD 分层目录结构 | ✅ 已搭建 | 7 个子模块，依赖方向正确 |
| Dubbo RPC 骨架 | ✅ 已搭建 | `IRemoteMemberService` + 桩实现 |
| Nacos 配置集成 | ✅ 已搭建 | 本地 + 远程双层配置 |
| 数据源配置 | ✅ 已搭建 | MySQL + Druid 已配置 |
| 领域模型（实体、聚合根） | ❌ 未实现 | domain 层为空 |
| 仓储接口与实现（CQRS） | ❌ 未实现 | 无 Repository、Mapper、PO |
| 应用服务 | ❌ 未实现 | application 层为空 |
| HTTP Controller | ❌ 未实现 | admin/app 目录为空 |
| 枚举与常量 | ❌ 未实现 | types 层为空 |
| 对象映射（MapStruct） | ❌ 未实现 | 依赖已引入 |
| 缓存策略 | ❌ 未实现 | Redis 依赖已配置 |
| 安全配置 | ❌ 未实现 | MemberSecurityConfig 为空 |
| 数据库脚本 | ❌ 未创建 | 无 SQL 文件 |
