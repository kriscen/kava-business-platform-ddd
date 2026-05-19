## Why

UPMS 模块存在 4 个明显的快速修复项：密码泄露到 API 响应、OAuth2 客户端查询的空指针风险、多步写操作缺少事务保护、审计字段（creator/gmtCreate/modifier/gmtModified）始终为 null。这些问题影响安全性和数据一致性，应在深入子领域完善前先修复。

## What Changes

- 在用户响应 DTO（SysUserDetailResponse、SysUserListResponse）上对 password 字段添加 `@JsonIgnore`，防止密码哈希泄露到 API 响应
- 修复 DBRegisteredClientRepository 的空指针风险，`findByClientId` 和 `findById` 增加 null 安全检查
- 为用户/角色的多步写操作（创建+关联表写入、更新+关联表替换）添加 `@Transactional` 事务保护
- 在 kbpd-common-database 中实现 MyBatis-Plus MetaObjectHandler，自动填充审计字段（creator、gmtCreate、modifier、gmtModified），从 UserContext 获取当前用户信息

## Capabilities

### New Capabilities

- `audit-fields-auto-fill`: 审计字段自动填充能力，通过 MyBatis-Plus MetaObjectHandler 在 insert/update 时自动设置 creator、gmtCreate、modifier、gmtModified

### Modified Capabilities

（无——其余修复为实现层面的 bug 修复，不涉及规格变更）

## Impact

- `kbpd-upms/kbpd-upms-api`: SysUserDetailResponse、SysUserListResponse 添加 `@JsonIgnore`
- `kbpd-upms/kbpd-upms-application`: SysUserAppService、SysRoleAppService 添加 `@Transactional`
- `kbpd-common/kbpd-common-database`: 新增 MetaObjectHandler 实现类
- `kbpd-auth`: DBRegisteredClientRepository 增加 null 安全检查
