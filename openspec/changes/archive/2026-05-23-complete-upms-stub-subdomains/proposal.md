## Why

UPMS 模块中 Menu、Dept、OauthClient 三个子域的 DomainService 当前所有方法均抛出 `UnsupportedOperationException("暂未实现")`，连 Repository 都未注入。这三个子域的基础设施层（Repository 实现、PO、Converter）已经完整就绪，但 Domain 层始终未接入，导致完整的 CRUD 链路断裂。同时 Menu 和 OauthClient 的 Repository 接口定义了额外查询方法（`queryAll()`、`queryByIds()`、`queryByClientId()`），但这些方法未通过 DomainService 接口暴露，导致 AppService 层无法合规调用。

## What Changes

- **补齐三个 DomainService 实现**：SysMenuService、SysDeptService、SysOauthClientService 注入对应 Repository，实现标准 CRUD 委托转发
- **扩展 DomainService 接口**：ISysMenuService 增加 `queryAll()` 和 `queryByIds()` 方法；ISysOauthClientService 增加 `queryByClientId()` 方法
- **对齐 DDD 分层规范**：确保 AppService 通过 DomainService 调用 Repository，而非跨层直接注入 Repository

## Capabilities

### New Capabilities

- `menu-domain-service`: Menu 子域 DomainService 完整实现（CRUD + queryAll + queryByIds）
- `dept-domain-service`: Dept 子域 DomainService 完整实现（CRUD）
- `oauth-client-domain-service`: OauthClient 子域 DomainService 完整实现（CRUD + queryByClientId）

### Modified Capabilities

## Impact

- `kbpd-upms/kbpd-upms-domain`:
  - `domain/service/ISysMenuService.java` — 新增 2 个方法签名
  - `domain/service/ISysOauthClientService.java` — 新增 1 个方法签名
  - `domain/service/impl/SysMenuService.java` — 注入 Repository，实现全部方法
  - `domain/service/impl/SysDeptService.java` — 注入 Repository，实现全部方法
  - `domain/service/impl/SysOauthClientService.java` — 注入 Repository，实现全部方法
