## Why

UPMS 模块中 7 个附属子域（SysI18nMessage、SysPublicParam、SysLog、SysFileGroup、SysFile、SysRouteConf、SysAuditLog）的 Domain Service 均为 stub 或缺失，App Service 绕过 Domain Service 直接调用 Repository，违反 DDD 分层原则（AppService → DomainService → Repository）。需要统一补齐，使调用链合规。

## What Changes

- 为 7 个附属子域补齐 Domain Service 实现，将 App Service 中的业务逻辑下沉到 Domain Service
- 修复 SysI18nMessage 缺失的 Domain Service 实现类
- 修复 SysAuditLog Infrastructure 层 queryPage 返回 null 的 bug
- App Service 改为通过 Domain Service 调用 Repository，不再直接依赖 Repository

涉及的 7 个子域：

| 子域 | 当前状态 | 变更内容 |
|------|---------|---------|
| SysI18nMessage | Domain Service 缺失 | 新建实现类，包含 code 重复校验逻辑 |
| SysPublicParam | Domain Service stub | 实现委托型 Domain Service |
| SysLog | Domain Service stub | 实现委托型 Domain Service |
| SysFileGroup | Domain Service stub | 实现委托型 Domain Service |
| SysFile | Domain Service stub | 实现委托型 Domain Service |
| SysRouteConf | Domain Service stub | 实现委托型 Domain Service |
| SysAuditLog | Domain Service stub + infra bug | 实现 Domain Service + 修复 queryPage |

## Capabilities

### New Capabilities

（无新增能力，本次为实现层面的架构修正）

### Modified Capabilities

- `i18n-kv-management`: Domain Service 层补齐，AppService 调用链修正
- `audit-fields-auto-fill`: Domain Service 层补齐，修复 queryPage 基础设施 bug

## Impact

- **kbpd-upms-domain**: 7 个 Domain Service 实现类（6 个 stub 改写 + 1 个新建）
- **kbpd-upms-application**: 7 个 App Service 改为通过 Domain Service 调用
- **kbpd-upms-infrastructure**: SysAuditLogRepository.queryPage 修复
- 不涉及 API 变更、数据库变更或跨模块依赖变更
