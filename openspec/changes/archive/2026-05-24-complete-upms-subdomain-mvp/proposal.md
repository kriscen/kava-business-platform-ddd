## Why

UPMS 模块中多个子域的 AppService 绕过 DomainService 直接调 Repository，违反 DDD 分层规则。具体：OauthClient 完全绕过、Area 写操作绕过、Menu 完全绕过（其 DomainService 已有树校验逻辑却未被使用）、Tenant 的删除操作绕过。此外 User 子域缺少用户名唯一性校验，Dept 子域写操作缺少 `@Transactional`。MVP 需要所有子域的链路合规并补齐基本数据完整性校验。

## What Changes

- **OauthClient 子域**：AppService 改为通过 DomainService 调用 Repository（修复完全绕过），DomainService 增加 clientId 唯一性校验、clientSecret 非空校验、token 有效期范围校验、authorizedGrantTypes 合法性校验
- **Area 子域**：AppService 写操作（create/update/delete）改为通过 DomainService 调用 Repository
- **Menu 子域**：AppService CRUD 操作改为通过 DomainService 调用 Repository（DomainService 已有 validatePid/validateBeforeDelete 等逻辑，但 AppService 未调用）
- **Tenant 子域**：AppService 的 `removeTenantBatchByIds` 改为通过 DomainService 调用
- **User 子域**：在 DomainService 中增加用户名唯一性校验（创建和更新时）
- **Dept 子域**：AppService 写操作方法补加 `@Transactional`

## Capabilities

### New Capabilities
- `user-uniqueness-validation`: 用户创建和更新时的用户名唯一性校验规则

### Modified Capabilities
- `oauth-client-domain-service`: 从纯 CRUD 委托升级为包含客户端校验规则的 DomainService

## Impact

- `kbpd-upms-domain`: 修改 SysOauthClientService（增加校验）、SysUserService（增加唯一性校验）、SysTenantService（增加删除逻辑）
- `kbpd-upms-application`: 修改 SysOauthClientAppService、SysAreaAppService、SysMenuAppService、SysTenantAppService、SysUserAppService、SysDeptAppService 的调用链路
- `kbpd-upms-infrastructure`: 无变更（Repository 已存在）
- `kbpd-upms-adapter`: 无变更（Controller 已存在）
