## Context

UPMS 模块中存在完整的"分组（Dept）"子域，包含 6 个 DDD 层约 30+ 个 Java 文件、数据库表 `sys_group`、以及 2 个 OpenSpec specs。该子域承载树形组织结构和数据权限范围过滤功能，业务逻辑已完整实现。

当前命名为"分组"，源自企业 ERP/RBAC 系统的通用术语，但 Kava 定位为家庭 SaaS 平台，"分组"语义不匹配家庭场景。需要将全栈命名从 Dept 改为 Group，保持业务逻辑和树形结构不变。

## Goals / Non-Goals

**Goals:**
- 将 `SysGroup*` 系列标识符全面重命名为 `SysGroup*`
- 将数据库表 `sys_group` 重命名为 `sys_group`，字段 `dept_id` 重命名为 `group_id`
- 将 `SysGroupId` 值对象重命名为 `SysGroupId`
- 将 `SysRoleDataScope` 中 `GROUP_*` 枚举值改为 `GROUP_*`
- 更新 `UserContext.groupId` → `groupId` 及 JWT claim key
- 更新所有文档和 specs 中的"分组"用语为"分组"
- 提供 SQL 迁移脚本

**Non-Goals:**
- 不改变任何业务逻辑、树形验证规则或数据权限过滤行为
- 不重构 Dept 模块的代码结构或设计模式
- 不涉及前端代码（前端由独立仓库管理）
- 不改变 API 路径中的 `/group/` — 仅改内部命名，API 路径改为 `/group/`

## Decisions

### 1. 一次性全量重命名，不保留兼容层
**Because**: 项目处于早期开发阶段，无线上用户和外部集成方，保留双名兼容层增加维护成本无收益。

### 2. 数据库使用 DDL 迁移脚本（ALTER TABLE），而非新建表
**Because**: `sys_group` 表目前无生产数据，直接 `RENAME TABLE` + `RENAME COLUMN` 即可，避免数据迁移复杂度。

### 3. API 路径同步修改为 `/group/`
**Because**: 既然是 BREAKING 改动且无线上用户，API 路径一并改为 `/api/v1/sys/group/`，保持内外一致。

### 4. 错误码保持 8 位格式不变，仅修改描述文本
**Because**: 错误码数值（A0050x）是内部标识，描述文本从"分组"改为"分组"即可。错误码数值不变避免影响日志检索。

### 5. OpenSpec specs 目录重命名
**Because**: `group-domain-service` → `group-domain-service`、`group-tree-validation` → `group-tree-validation`，spec 名称应与代码命名保持一致。

## Risks / Trade-offs

- **遗漏引用风险**: `dept`/`Dept`/`GROUP` 在代码、配置、SQL、文档中分布广泛，可能遗漏个别引用 → 通过全局搜索验证
- **kbpd-common-core 影响**: `UserContext.groupId` 和 `JwtClaimConstants` 在 `kbpd-common-core` 中，需跨模块修改 → 改动量小（2-3 个文件），可合并入同一 change
