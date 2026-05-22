## Context

UPMS 模块 14 个子域中，SysUser、SysRole、SysTenant、SysArea 4 个子域的 Domain Service 已正确实现。剩余 10 个子域中，本次处理的 7 个附属子域的 Domain Service 均为 stub（抛 UnsupportedOperationException）或缺失，App Service 绕过 Domain Service 直接注入 Repository。

当前调用链（违规）：
```
AppService ──直接注入──▶ Repository
```

目标调用链（合规）：
```
AppService ──注入──▶ DomainService ──注入──▶ Repository
```

参考实现：`SysTenantService` — 构造器注入 `ISysTenantRepository`，5 个方法全部委托给 repository。

## Goals / Non-Goals

**Goals:**
- 7 个附属子域的 Domain Service 实现为委托型（delegate to repository）
- App Service 改为注入 Domain Service 而非 Repository
- 修复 SysAuditLogRepository.queryPage 返回 null 的 bug
- SysI18nMessage 补齐缺失的 Domain Service 实现类

**Non-Goals:**
- 不为这 7 个子域引入 Read/Write Repository 分离（它们是简单 Entity，不需要 CQRS）
- 不将它们提升为 Aggregate Root
- 不新增业务规则（这些子域当前没有需要收入 Domain 的业务逻辑）
- 不变更 API 层（Controller、DTO、RPC 接口不变）

## Decisions

### Decision 1: 采用委托型 Domain Service 模式

7 个子域的 Domain Service 全部采用与 `SysTenantService` 相同的委托模式：注入 Repository，5 个方法直接转发。

**Because**: 这些子域当前没有需要收入 Domain 的业务规则。委托模式保证 DDD 分层合规，同时为未来业务规则增长预留了入口（在 Domain Service 中添加逻辑即可，无需改 AppService）。

**替代方案**: 跳过 Domain Service，让 AppService 继续直接调 Repository。被否决——违反项目 DDD 约束。

### Decision 2: SysI18nMessage 打破现有 spec 约束，创建 Domain Service 实现

现有 spec (`i18n-kv-management`) 规定 "MUST NOT 创建实现类"。但该 spec 制定时的假设是这些子域不需要 Domain Service。现在统一补齐的背景下，创建实现类更合理——保持所有附属子域一致。

**Because**: 一致性优先。如果 I18n 是唯一没有 Domain Service 实现的子域，会造成困惑。且当前 AppService 中有 code 重复校验逻辑，未来可能需要下沉到 Domain Service。

**影响**: 需要更新 `i18n-kv-management` spec，将 "MUST NOT 创建实现类" 改为允许创建。

### Decision 3: SysAuditLogRepository.queryPage 修复方式

取消注释现有实现代码，参照其他 Repository 的 queryPage 实现模式（使用 MyBatis-Plus Page + LambdaQueryWrapper）。

**Because**: 被注释的代码已经给出了基本框架，只需补全查询条件构建逻辑。参照同模块其他 Repository（如 SysLogRepository）的实现模式即可。

## Risks / Trade-offs

- **[低风险] 委托型 Domain Service 增加了一层间接调用** → 对性能无实质影响，且为未来业务规则增长预留了空间
- **[低风险] SysI18nMessage spec 变更** → 仅移除 "MUST NOT 创建实现类" 约束，不影响已有功能
- **[低风险] SysAuditLogRepository.queryPage 修复** → 参照现有模式，风险可控
