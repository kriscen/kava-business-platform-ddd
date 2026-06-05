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
├── 05-frontend/             # 前端对接文档
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
| [kbpd-upms.sql](01-sql/kbpd-upms.sql) | UPMS 数据库建表脚本，包含用户、角色、菜单、分组、租户、应用等表结构定义 |

---

## 02-architecture/ — 架构设计

| 文件 | 说明 |
|------|------|
| [README.md](02-architecture/README.md) | 架构目录导航，文档清单与推荐阅读顺序 |
| [overview.md](02-architecture/overview.md) | 系统架构总览，DDD + 六边形架构分层、微服务划分及基础设施选型 |
| [boundaries.md](02-architecture/boundaries.md) | 服务边界与模块划分，DDD 分层职责、CQRS 策略、聚合边界及跨服务调用规则 |
| [security-architecture.md](02-architecture/security-architecture.md) | 安全架构，JWT Token 结构、RBAC 权限模型、多租户隔离策略 |
| [integration-patterns.md](02-architecture/integration-patterns.md) | 服务间通信与集成模式，Dubbo RPC 规范、接口版本管理、跨服务错误处理 |

---

## 03-conventions/ — 开发规范

| 文件 | 说明 |
|------|------|
| [code-style.md](03-conventions/code-style.md) | 代码风格与命名规范，Java 类名、方法名、常量规则及 VO/DTO/Query/Cmd 后缀约定 |
| [ddd-rules.md](03-conventions/ddd-rules.md) | DDD 实现规范，领域模型风格、DomainService 规则、跨聚合访问策略、层间引用边界 |
| [git.md](03-conventions/git.md) | Git 分支与协作规范，feature/bugfix/hotfix 分支命名格式及 main 分支保护策略 |

---

## 04-reference/ — 参考手册

| 文件 | 说明 |
|------|------|
| [error-codes.md](04-reference/error-codes.md) | 错误码规范，8 位数字格式（MM-SS-NNNN）的模块号分配与通用/业务错误码 |

---

## 05-frontend/ — 前端对接文档

| 文件 | 说明 |
|------|------|
| [auth-api.md](05-frontend/auth-api.md) | Auth 前端对接文档，登录流程、OAuth2 协议端点和 JWT Token 结构 |
| [upms-api.md](05-frontend/upms-api.md) | UPMS 前端对接文档，HTTP REST 接口、请求/响应字段定义和查询参数 |

---

## 06-modules/ — 模块文档

### README

| 文件 | 说明 |
|------|------|
| [README.md](06-modules/README.md) | 模块文档产出规范，定义业务模块与公共模块的文档要求及目录对应关系 |

### kbpd-auth/ — 认证服务

| 文件 | 说明 |
|------|------|
| [overview.md](06-modules/kbpd-auth/overview.md) | Auth 模块概览，OAuth2 认证授权中心定位、双 Security 过滤链、多租户认证流程、Token 存储策略 |
| [business-rules.md](06-modules/kbpd-auth/business-rules.md) | Auth 模块业务规则，多租户认证路由、Token 生命周期管理、客户端校验规则 |

### kbpd-common/ — 公共模块

| 文件 | 说明 |
|------|------|
| [overview.md](06-modules/kbpd-common/overview.md) | Common 模块概览，BOM 版本管理、DDD 类型体系、Redis/数据库/安全/Web 基础设施封装 |

### kbpd-gateway/ — API 网关

| 文件 | 说明 |
|------|------|
| [overview.md](06-modules/kbpd-gateway/overview.md) | Gateway 模块概览，Spring Cloud Gateway 路由、Nacos 服务发现与配置管理 |

### kbpd-member/ — 会员服务

| 文件 | 说明 |
|------|------|
| [overview.md](06-modules/kbpd-member/overview.md) | Member 模块概览，会员管理微服务定位、DDD 分层结构与 Dubbo RPC 骨架 |
| [business-rules.md](06-modules/kbpd-member/business-rules.md) | Member 模块业务规则（规划），多租户隔离、CQRS、DDD 分层约束 |

### kbpd-upms/ — 用户权限管理服务

| 文件 | 说明 |
|------|------|
| [overview.md](06-modules/kbpd-upms/overview.md) | UPMS 模块概览，用户权限管理核心服务、14 个资源 CRUD 体系、RBAC 权限模型与数据隔离策略 |
| [business-rules.md](06-modules/kbpd-upms/business-rules.md) | UPMS 模块业务规则，RBAC 关联持久化、权限运行时、租户管理及数据隔离规则 |

---

## 07-product/ — 产品文档

| 文件 | 说明 |
|------|------|
| [README.md](07-product/README.md) | 产品文档导航 |
| [vision.md](07-product/vision.md) | 产品愿景与定位 — Kava 家庭 SaaS 平台核心价值、App 接入模型、发展阶段 |
| [user-roles.md](07-product/user-roles.md) | 用户角色定义 — B端（平台管理员、租户管理员）/ C端（App 终端用户）角色与场景 |
| [auth-chain.md](07-product/auth-chain.md) | 认证链路 — JWT Token 结构、验证流程、租户上下文传播机制 |
| [menu-app-model.md](07-product/menu-app-model.md) | 菜单与应用模型 — 菜单 level 体系、App 菜单组合包、白标 SaaS 模式 |
| [platform-architecture.md](07-product/platform-architecture.md) | 平台架构概念 — Gateway 纯路由、租户模型、App 与 OAuth2 Client 关系、数据隔离策略 |

---

## 快速导航

**新人上手**：`02-architecture/overview.md` → `02-architecture/boundaries.md` → `03-conventions/code-style.md` → `03-conventions/git.md`

**了解产品**：`07-product/vision.md` → `07-product/user-roles.md` → `07-product/platform-architecture.md` → `07-product/auth-chain.md`

**前端对接**：`05-frontend/auth-api.md`（认证流程） → `05-frontend/upms-api.md`（业务接口）

**开发接口**：`04-reference/error-codes.md`

**建库建表**：`01-sql/kbpd-upms.sql`

**模块开发**：`06-modules/kbpd-{模块}/overview.md` → `api.md` → `business-rules.md`
