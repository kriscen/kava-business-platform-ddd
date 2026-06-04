## Why

"分组（Dept）"是企业场景的概念，与 Kava 家庭 SaaS 平台的产品定位不符。家庭中的成员分组更自然地表达为"分组（Group）"——如"大人组"、"小孩组"。保留树形结构以支持未来扩展（大家庭、社区等），仅做语义层面的重命名，业务逻辑不变。

## What Changes

- **BREAKING**: 将 `SysGroup*` 系列类重命名为 `SysGroup*`，包括 Entity、Id、ListQuery、Repository、Service、Controller、Converter、Command、DTO、Request、Response、PO、Mapper 等
- **BREAKING**: 数据库表 `sys_group` 重命名为 `sys_group`，提供 SQL 迁移脚本
- **BREAKING**: `SysUserEntity` 中 `groupId` 字段重命名为 `groupId`，所有引用处同步更新
- **BREAKING**: `SysRoleDataScope` 枚举中 `GROUP_AND_CHILD` → `GROUP_AND_CHILD`、`GROUP_ONLY` → `GROUP_ONLY`
- 将 `SysGroupId` 值对象重命名为 `SysGroupId`
- 更新错误码注释中的"分组"描述为"分组"
- 更新所有文档和 specs 中的 dept 相关引用

## Capabilities

### New Capabilities

（无新增能力）

### Modified Capabilities

- `group-domain-service`: 重命名为 `group-domain-service`，所有标识符从 Dept 替换为 Group
- `group-tree-validation`: 重命名为 `group-tree-validation`，描述从"分组"改为"分组"
- `permission-system`: 数据范围枚举值从 GROUP_* 改为 GROUP_*
- `unified-user-context`: UserContext 中 groupId 改为 groupId，JWT claim 同步更新
- `dropdown-selector-api`: 分组下拉改为分组下拉
- `response-enrichment`: groupName 改为 groupName 的响应富化
- `list-query-filter`: dept 查询过滤改为 group 查询过滤

## Impact

- **kbpd-upms 全模块**: 6 个 DDD 层约 30+ 个 Java 文件涉及重命名
- **kbpd-common-core**: `UserContext.groupId` → `groupId`，`JwtClaimConstants` 中的 claim key
- **数据库**: `sys_group` → `sys_group` 迁移脚本，`sys_user.dept_id` → `group_id` 列重命名
- **文档**: `docs/06-modules/kbpd-upms/`、`docs/05-frontend/`、`docs/02-architecture/`、`docs/07-product/` 中的引用
- **OpenSpec specs**: `group-domain-service` 和 `group-tree-validation` 目录重命名及内容更新
