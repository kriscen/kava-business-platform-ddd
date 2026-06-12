## Why

Member 模块目前只有一个 `MemberInfoDTO`，缺少完整的 DDD 领域模型。需要初始化 MVP 版本的 Member 领域模型，为后续 C 端用户功能（注册、登录、个人中心等）奠定基础。

## What Changes

- 在 `kbpd-common-core` 中新增 `MemberId` 值对象
- 在 `kbpd-common-core` 中提升 `SysAppId`（从 `kbpd-upms-domain` 移入）
- 在 `kbpd-member-domain` 中创建 MVP 版本的 `MemberEntity` 聚合根
- 在 `kbpd-member-domain` 中创建读写分离的 Repository 接口
- 在 `kbpd-member-infrastructure` 中实现持久化层（PO、Mapper、Repository 实现）
- 在 `kbpd-member-api` 中更新 `MemberInfoDTO` 对齐 MVP 模型
- 提供 `mbr_member` 建表脚本

**MVP 字段范围**：
- `id`: MemberId
- `mobile`: 登录标识
- `password`: 认证凭证
- `tenantId`: 租户归属
- `appId`: App 归属（Member 关联到具体 App）
- `enabled`: 启用状态

## Capabilities

### New Capabilities
- `member-domain`: Member 领域模型（聚合根、值对象、Repository 接口）
- `member-persistence`: Member 持久化实现（PO、Mapper、Repository 实现）

### Modified Capabilities

## Impact

**涉及模块**：
- `kbpd-common-core`: 新增 MemberId，提升 SysAppId
- `kbpd-member-domain`: 新增领域模型
- `kbpd-member-infrastructure`: 新增持久化实现
- `kbpd-member-api`: 更新 DTO

**前置依赖**：
- 需要先完成 `kbpd-common-core` 的改动（MemberId、SysAppId），再进行 member 模块开发

**数据库**：
- 新增 `mbr_member` 表（表名前缀 `mbr_` 遵循项目约定）
