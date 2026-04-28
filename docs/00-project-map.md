# 项目文档地图

本文档是 `docs/` 目录的导航索引，帮助你快速找到所需文档。

## 目录结构

```
docs/
├── 00-project-map.md        ← 你在这里
├── 01-sql/                  # 数据库脚本
├── 02-architecture/         # 架构设计
├── 03-conventions/          # 开发规范
├── 04-reference/            # 参考手册
├── 05-history/              # 决策历史
├── 06-modules/              # 模块文档
│   ├── kbpd-auth/           # 认证服务
│   ├── kbpd-common/         # 公共模块
│   ├── kbpd-gateway/        # API 网关
│   ├── kbpd-member/         # 会员服务
│   └── kbpd-upms/           # 用户权限管理服务
└── 07-product/              # 产品文档
```

---

## 01-sql/ — 数据库脚本

| 文件 | 说明 |
|------|------|
| [kbpd-upms.sql](01-sql/kbpd-upms.sql) | UPMS（用户权限管理）数据库建表脚本，基于 MySQL 8.4，包含系统表、租户、审计日志等表结构定义 |

---

## 02-architecture/ — 架构设计

| 文件 | 说明 |
|------|------|
| [README.md](02-architecture/README.md) | 架构目录导航，文档清单与推荐阅读顺序 |
| [overview.md](02-architecture/overview.md) | 系统架构总览，描述 DDD + 六边形架构分层、微服务划分及基础设施选型（Gateway → Auth/UPMS/Member → Nacos/MySQL/Redis/MQ） |
| [boundaries.md](02-architecture/boundaries.md) | 服务边界与模块划分，定义 DDD 分层职责、命名约定、CQRS 策略、聚合边界及跨服务调用规则 |
| [security-architecture.md](02-architecture/security-architecture.md) | 安全架构，描述认证流程、JWT Token 结构、RBAC 权限模型、多租户隔离策略及 Resource Server 配置 |
| [integration-patterns.md](02-architecture/integration-patterns.md) | 服务间通信与集成模式，描述 Dubbo RPC 规范、接口版本管理、跨服务错误处理及事件驱动架构规划 |

---

## 03-conventions/ — 开发规范

| 文件 | 说明 |
|------|------|
| [code-style.md](03-conventions/code-style.md) | 代码风格与命名规范，涵盖 Java 类名、方法名、常量的命名规则，以及 VO/DTO/Query/Cmd 等后缀约定 |
| [git.md](03-conventions/git.md) | Git 分支与协作规范，定义 feature/bugfix/hotfix/release/refactor 分支命名格式及 main 分支保护策略 |

---

## 04-reference/ — 参考手册

| 文件 | 说明 |
|------|------|
| [error-codes.md](04-reference/error-codes.md) | 错误码规范（待填充） |

---

## 05-history/ — 决策历史

| 文件 | 说明 |
|------|------|
| [adr-template.md](05-history/adr-template.md) | ADR（架构决策记录）模板，包含状态、背景、决策、理由、备选方案、后果等标准章节，用于记录重大架构决策 |

---

## 06-modules/ — 模块文档

### README

| 文件 | 说明 |
|------|------|
| [README.md](06-modules/README.md) | 模块文档产出规范，定义业务模块与公共模块的文档要求及目录对应关系 |

### kbpd-auth/ — 认证服务

| 文件 | 说明 |
|------|------|
| [overview.md](06-modules/kbpd-auth/overview.md) | Auth 模块概览，描述 OAuth2 认证授权中心定位、双 Security 过滤链、多租户认证流程、Token 存储策略与 JWT Claims |
| [api.md](06-modules/kbpd-auth/api.md) | Auth 模块接口文档，包含页面接口（登录/授权确认）和 OAuth2 协议端点说明 |
| [business-rules.md](06-modules/kbpd-auth/business-rules.md) | Auth 模块业务规则，定义多租户认证路由、Token 生命周期管理、客户端校验等规则 |

### kbpd-common/ — 公共模块

| 文件 | 说明 |
|------|------|
| [overview.md](06-modules/kbpd-common/overview.md) | Common 模块概览，描述 BOM 版本管理、DDD 类型体系、Redis/数据库/安全/Web 基础设施封装 |

### kbpd-gateway/ — API 网关

| 文件 | 说明 |
|------|------|
| [overview.md](06-modules/kbpd-gateway/overview.md) | Gateway 模块概览（待填充） |

### kbpd-member/ — 会员服务

| 文件 | 说明 |
|------|------|
| [overview.md](06-modules/kbpd-member/overview.md) | Member 模块概览（待填充） |
| [api.md](06-modules/kbpd-member/api.md) | Member 模块接口文档（待填充） |
| [business-rules.md](06-modules/kbpd-member/business-rules.md) | Member 模块业务规则（待填充） |

### kbpd-upms/ — 用户权限管理服务

| 文件 | 说明 |
|------|------|
| [overview.md](06-modules/kbpd-upms/overview.md) | UPMS 模块概览（待填充） |
| [api.md](06-modules/kbpd-upms/api.md) | UPMS 模块接口文档（待填充） |
| [business-rules.md](06-modules/kbpd-upms/business-rules.md) | UPMS 模块业务规则（待填充） |

---

## 07-product/ — 产品文档

| 文件 | 说明 |
|------|------|
| [README.md](07-product/README.md) | 产品文档导航 |
| [vision.md](07-product/vision.md) | 产品愿景与定位 — Kava 是什么、解决什么问题、核心价值、平台边界、发展阶段 |
| [user-roles.md](07-product/user-roles.md) | 用户角色定义 — B端（平台管理员、租户管理员）/ C端（App 终端用户）的角色、权限、使用场景 |
| [platform-architecture.md](07-product/platform-architecture.md) | 平台架构概念 — 租户模型、数据隔离策略、OAuth2 接入流程、模块职责与产品关系 |

---

## 快速导航

**新人上手**：`overview.md` → `boundaries.md` → `code-style.md` → `git.md`

**了解产品**：`07-product/vision.md` → `user-roles.md` → `platform-architecture.md`

**开发接口**：`error-codes.md`

**建库建表**：`01-sql/kbpd-upms.sql`

**架构变更**：`adr-template.md`（填写后提交到此目录）

**模块开发**：`06-modules/kbpd-{模块}/overview.md` → `api.md` → `business-rules.md`
