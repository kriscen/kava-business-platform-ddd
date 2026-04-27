# 系统架构概览

## 1.1 整体架构

Kava Business Platform (KBPD) 是一个基于领域驱动设计（DDD）和六边形架构（Hexagonal Architecture）的企业级业务平台，采用微服务架构风格构建。

```
┌─────────────────────────────────────────────────────────────────┐
│                         接入层                                    │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────────────────┐  │
│  │  Web    │  │  Mobile │  │  API    │  │    Dubbo RPC        │  │
│  │  Client │  │  App    │  │  Client │  │    (Service Mesh)   │  │
│  └────┬────┘  └────┬────┘  └────┬────┘  └──────────┬──────────┘  │
└───────┼───────────┼───────────┼───────────────────┼─────────────┘
        │           │           │                   │
        └───────────┴───────────┴───────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         网关层                                    │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │                    kbpd-gateway (Spring Cloud Gateway)      ││
│  │                    - 路由转发 / 鉴权 / 限流 / 日志            ││
│  └─────────────────────────────────────────────────────────────┘│
└──────────────────────────────┬──────────────────────────────────┘
                               │
        ┌──────────────────────┬┼──────────────────────┐
        │                      │                      │
        ▼                      ▼                      ▼
┌───────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  kbpd-auth    │    │   kbpd-upms     │    │  kbpd-member    │
│  OAuth2 Server│    │  (User & Perm)  │    │  (Member Mgmt)  │
│               │    │                 │    │                 │
│ - OAuth2      │    │ - 用户管理      │    │ - 会员管理      │
│ - SSO         │    │ - 角色权限      │    │ - 会员等级      │
│ - Token       │    │ - 租户管理      │    │ - 会员权益      │
│   管理        │    │ - 菜单管理      │    │                 │
└───────────────┘    └─────────────────┘    └─────────────────┘
        │                      │                      │
        └──────────────────────┴──────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                         基础设施层                                │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────────┐   │
│  │  Nacos   │  │  MySQL   │  │   Redis  │  │    MQ        │   │
│  │ (注册/配置)│  │ (存储)   │  │ (缓存)   │  │ (消息队列)   │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

## 1.2 架构风格

### 六边形架构（端口与适配器）

```
                    ┌─────────────────────┐
                    │     外部世界         │
                    │  (HTTP Client/Dubbo) │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │      适配器层         │
                    │   (Adapter Layer)    │
                    │  - Controller        │
                    │  - RPC Adapter       │
                    │  - Event Listener    │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │      端口层           │
                    │   (Port Layer)       │
                    │  - IService          │
                    │  - IRepository       │
                    │  - IEventPublisher   │
                    └──────────┬──────────┘
                               │
┌──────────────────────────────┼──────────────────────────────┐
│                    ┌─────────▼─────────┐                    │
│                    │    应用层           │                    │
│                    │ (Application)     │                    │
│                    │  - ApplicationSvc  │                    │
│                    │  - Command/Query   │                    │
│                    └─────────┬───────────┘                    │
│                    ┌─────────▼─────────┐                      │
│                    │     领域层          │                      │
│                    │    (Domain)        │                      │
│                    │  - Aggregate       │                      │
│                    │  - Entity          │                      │
│                    │  - Value Object    │                      │
│                    │  - Domain Service  │                      │
│                    └────────────────────┘                      │
└───────────────────────────────────────────────────────────────┘
                               │
                    ┌──────────▼──────────┐
                    │   基础设施层          │
                    │ (Infrastructure)    │
                    │  - MyBatis DAO       │
                    │  - Redis Cache       │
                    │  - Repository Impl  │
                    └──────────────────────┘
```

### 依赖原则

**核心原则：依赖方向必须向内**

```
adapter/infrastructure  →  application  →  domain
```

- **domain 层**：核心业务逻辑，绝对不依赖任何外部框架（Spring、MyBatis 等）
- **application 层**：编排领域对象，协调用例实现
- **adapter 层**：协议适配器（HTTP→Domain, Dubbo→Domain）
- **infrastructure 层**：持久化实现、外部服务调用

## 1.3 技术栈

| 层级 | 技术 | 版本 | 用途 |
|------|------|------|------|
| 运行时 | Java | 21 | 主要开发语言 |
| 框架 | Spring Boot | 3.3.13 | 基础框架 |
| 微服务 | Spring Cloud | 2023.0.6 | 服务治理 |
| 云生态 | Spring Cloud Alibaba | 2023.0.3.2 | Nacos、Sentinel |
| RPC | Apache Dubbo | 3.3.x | 服务间通信 |
| 数据库 | MySQL + MyBatis-Plus | 8.x / 3.5.x | 持久化 |
| 缓存 | Redis | - | 缓存、分布式锁 |
| 注册/配置 | Nacos | - | 服务发现、配置中心 |
| 安全 | Spring Security + OAuth2 | - | 认证授权 |

## 1.4 核心模块

### 1.4.1 kbpd-upms（用户权限管理服务）

完整的 DDD 结构，提供用户、角色、权限、租户管理能力。

```
kbpd-upms/
├── api/                    # 端口定义
│   ├── model/
│   │   ├── request/       # 请求对象（入参）
│   │   ├── response/      # 响应对象（出参）
│   │   ├── query/         # 列表查询参数
│   │   └── dto/           # 数据传输对象（Dubbo 用）
│   └── service/            # 服务接口（Dubbo 对外暴露）
├── application/            # 应用服务
│   ├── model/
│   │   ├── command/       # 命令对象（CUD）
│   │   └── dto/           # 应用层 DTO
│   └── service/            # 应用服务实现
├── domain/                 # 领域层（核心）
│   ├── model/
│   │   ├── entity/        # 领域实体
│   │   ├── aggregate/     # 聚合根
│   │   └── valobj/        # 值对象
│   ├── service/           # 领域服务
│   └── repository/         # 仓储接口
├── infrastructure/         # 基础设施
│   ├── dao/               # MyBatis Mapper
│   │   └── po/            # 持久化对象
│   ├── adapter/repository/ # 仓储实现
│   └── converter/          # PO ↔ Entity 转换
├── adapter/                # 适配器
│   ├── controller/         # HTTP Controller
│   └── rpc/               # Dubbo RPC 实现
└── bootstrap/              # 启动入口
```

### 1.4.2 kbpd-auth（认证授权服务）

独立的 OAuth2 授权服务器，负责身份认证和令牌发放。

- 支持 OAuth2 授权码模式、客户端模式
- 提供 Token 管理和刷新机制
- 集成 SSO 单点登录

### 1.4.3 kbpd-gateway（API 网关）

Spring Cloud Gateway 实现，作为所有外部请求的统一入口。

- 路由转发（根据路径/Header 转发到后端服务）
- 鉴权（JWT Token 验证）
- 限流熔断（Sentinel）
- 全局日志记录

### 1.4.4 kbpd-member（会员服务）

> **状态：建设中**，domain 层尚未实现。

会员管理领域服务，规划提供会员等级、权益等能力。当前模块结构已搭建，核心业务逻辑待补充。

### 1.4.5 kbpd-demo（示例模块）

> **状态：模板参考模块**，无业务逻辑，当前已从根 `pom.xml` 中禁用。

作为新增业务模块的目录结构和依赖关系参考，展示了完整的 DDD 分层模块（api / application / domain / infrastructure / adapter / bootstrap / types）。

### 1.4.6 kbpd-common（公共模块）

```
kbpd-common/
├── kbpd-common-core/      # 核心工具、值对象基类
├── kbpd-common-database/   # 数据库通用组件
├── kbpd-common-security/   # 安全相关（加密、签名）
├── kbpd-common-cache/     # 缓存抽象
├── kbpd-common-web/       # Web 通用（异常处理、响应包装）
└── kbpd-common-bom/       # 依赖版本管理
```

## 1.5 服务通信

### 同步通信（Dubbo RPC）

```yaml
# Dubbo 配置示例
dubbo:
  protocol:
    name: dubbo
    port: 20881
  registry:
    address: nacos://localhost:8848
  cluster: failfast
```

### 异步通信（事件驱动）

> 🟡 规划中 — `BaseEvent` 基类已预留，MQ 选型与具体事件待后续落地

## 1.6 数据一致性

> 🟡 规划中 — 计划采用 Saga 模式，通过领域事件 + MQ 实现最终一致性

## 1.7 安全性

```
┌─────────┐     ┌────────────┐     ┌────────────┐     ┌──────────┐
│ Client  │────▶│  Gateway   │────▶│ Auth Server │────▶│ Service  │
└─────────┘     │ (JWT Verify)│     │ (Token Issue)│     └──────────┘
                      │              │
                      │         ┌────▼────┐
                      │         │  User   │
                      │         │  Store  │
                      │         └─────────┘
                      │
                ┌─────▼─────┐
                │  Tenant   │
                │  Resolver │
                └───────────┘
```

1. 客户端请求先到 Gateway，透传至下游服务
2. 下游服务（Resource Server）验证 JWT Token 并填充安全上下文
3. 业务服务通过 SecurityUtils 获取当前用户信息
4. 多租户通过 OAuth2 Client 绑定的 tenantId 在登录阶段确定租户

> 详细认证流程、JWT 结构、RBAC 模型见 [security-architecture.md](security-architecture.md)

## 1.8 规划状态汇总

| 能力 | 状态 | 说明 |
|------|------|------|
| Dubbo RPC 同步调用 | ✅ 已实现 | Auth ↔ UPMS/Member |
| OAuth2 授权码模式 | ✅ 已实现 | Auth Server 完整流程 |
| JWT 签发与验证 | ✅ 已实现 | RSA2048 |
| RBAC 权限模型 | ✅ 已实现 | 用户→角色→菜单/按钮 |
| 多租户登录隔离 | ✅ 已实现 | Client 绑定 tenantId |
| Gateway HTTP 路由 | ✅ 已实现 | 静态配置，硬编码 localhost |
| Gateway JWT 鉴权 | 🟡 规划中 | 当前透传，下游服务自行校验 |
| Gateway 限流熔断 | 🟡 规划中 | Sentinel 依赖已引入 |
| 领域事件 | 🟡 规划中 | BaseEvent 已预留，无 MQ |
| Saga 分布式事务 | 🟡 规划中 | 等事件驱动架构落地 |
| MyBatis-Plus 租户拦截器 | 🟡 规划中 | 当前查询手动过滤 tenant_id |
| 防腐层 | 🟡 规划中 | 跨服务直接依赖 Dubbo DTO |
| Member 服务 Resource Server | 🟡 规划中 | 未启用 JWT 校验 |

> 🟡 表示已规划但尚未实现。详细通信约定见 [integration-patterns.md](integration-patterns.md)，安全细节见 [security-architecture.md](security-architecture.md)
