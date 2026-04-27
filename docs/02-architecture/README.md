# 02-architecture — 架构设计

本目录存放 KBPD 系统的**跨模块架构决策文档**。每份文档聚焦一个架构维度，定位清晰、不重叠。

## 文档清单

| 文件 | 定位 | 核心内容 |
|------|------|---------|
| [overview.md](overview.md) | **系统全景** | 架构风格、技术栈、模块概览、规划状态汇总 |
| [boundaries.md](boundaries.md) | **边界划分** | DDD 分层职责、命名约定、聚合边界、限界上下文 |
| [security-architecture.md](security-architecture.md) | **安全架构** | 认证流程、JWT 结构、RBAC、多租户隔离 |
| [integration-patterns.md](integration-patterns.md) | **通信模式** | Dubbo 规范、接口版本管理、跨服务错误处理 |

## 推荐阅读顺序

1. **overview.md** — 先理解系统全貌和各模块定位
2. **boundaries.md** — 再看内部如何分层、怎么切分边界
3. **security-architecture.md** — 认证授权影响所有服务接口，早期必读
4. **integration-patterns.md** — 涉及跨服务开发时查阅

> 各模块自身的详细文档（领域模型、业务规则、接口）在 [06-modules/](../06-modules/) 中，不在此目录重复。

## 维护提醒

新增或删除架构文档后，执行 `/update-docs-map` 同步 `docs/00-project-map.md`。
