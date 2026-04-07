# ADR 历史记录

> Architecture Decision Records - 架构决策记录

本目录记录了 Kava Business Platform (KBPD) 项目中的重要架构决策。

## 决策索引

| ID | 标题 | 状态 | 日期 |
|----|------|------|------|
| [ADR-0001](adr-0001-ddd-hexagonal-architecture.md) | 采用 DDD + 六边形架构 | 已采纳 | 2024-01-15 |
| [ADR-0002](adr-0002-java-microservices-stack.md) | 技术栈决策：Java 21 + Spring Cloud Alibaba | 已采纳 | 2024-01-20 |
| [ADR-0003](adr-0003-cqrs-read-write-separation.md) | 采用 CQRS 模式：Repository 读写分离 | 已采纳 | 2024-02-05 |
| [ADR-0004](adr-0004-value-object-ids.md) | 值对象 ID：强类型标识符 | 已采纳 | 2024-02-10 |
| [ADR-0005](adr-0005-saga-eventual-consistency.md) | 跨服务一致性：Saga 模式 | 已采纳 | 2024-02-15 |
| [ADR-0006](adr-0006-oauth2-jwt-security.md) | 安全架构：OAuth2 + JWT | 已采纳 | 2024-02-20 |
| [ADR-0007](adr-0007-anticorruption-layer.md) | 限界上下文映射：防腐层（ACL） | 已采纳 | 2024-02-25 |

## 状态说明

- **提议中（Proposed）**：正在评审中，尚未采纳
- **已采纳（Accepted）**：已评审通过，正在实施
- **已废弃（Deprecated）**：已不再推荐
- **替代（Superseded）**：被新的 ADR 替代

## 新增 ADR

新增架构决策时，请：

1. 复制 `adr-template.md` 作为模板
2. 命名为 `adr-XXXX-title.md`
3. 在本索引添加对应条目
4. 确保决策有完整的背景、理由和后果分析

## 决策原则

1. **可逆性**：架构决策应尽量可逆，避免不可挽回的锁定
2. **渐进式**：优先选择可以渐进实施的方案
3. **上下文**：记录决策的背景和约束，不做孤立决策
4. **可追溯**：重大决策必须记录理由和替代方案对比
