## Why

UPMS 模块的 CRUD 骨架已全部搭建完成，但存在若干链路断裂和一致性缺陷：MetaObjectHandler 未注册导致 PO 审计字段全部为 null、loginByPwd Dubbo 接口仍为桩实现阻塞 auth 登录链路、User/Tenant 聚合根缺失独立 ID 值对象与其他子域不一致、FileGroup 路径命名错误。这些问题是 Phase 1 打通链路的前置条件，需合并修复。

## What Changes

- **MetaObjectHandler 实现**：新增 `kbpd-common-database` 模块的 `MybatisMetaObjectHandler`，自动填充 `creator`、`gmtCreate`、`modifier`、`gmtModified` 等审计字段
- **loginByPwd 实现**：将 `RemoteUserService` 中的桩实现替换为真实查询（根据用户名查询用户、校验密码、返回用户 ID）
- **SysUserId 值对象补齐**：在 `kbpd-upms-domain` 中新增 `SysUserId` 值对象，统一 User 聚合根的 ID 类型
- **SysTenantId 值对象补齐**：在 `kbpd-upms-domain` 中新增 `SysTenantId` 值对象，统一 Tenant 实体的 ID 类型
- **FileGroup 路径修正**：修正 `SysFileGroupController` 中 `@RequestMapping` 路径从 `FileGroup-group` 为 `file-group`

## Capabilities

### New Capabilities

- `audit-fields-auto-fill`: MybatisMetaObjectHandler 自动填充 PO 审计字段（creator、gmtCreate、modifier、gmtModified）

### Modified Capabilities

- `authentication-flow`: loginByPwd Dubbo 接口从桩实现改为真实用户查询和密码校验
- `unified-user-context`: User 聚合根引入 SysUserId 值对象
- `tenant-system`: Tenant 实体引入 SysTenantId 值对象

## Impact

- `kbpd-common-database`：新增 MetaObjectHandler 类，pom 无需变更（已有 mybatis-plus 依赖）
- `kbpd-upms-domain`：新增 SysUserId、SysTenantId 值对象，修改 User/Tenant 相关引用
- `kbpd-upms-infrastructure`：SysUserConverter、SysTenantConverter 适配新值对象；SysUserReadRepository 适配 loginByPwd 查询
- `kbpd-upms-adapter`：RemoteUserService 实现替换；SysFileGroupController 路径修正
- `kbpd-upms-application`：SysUserAppConverter、SysTenantAppConverter 适配新值对象
