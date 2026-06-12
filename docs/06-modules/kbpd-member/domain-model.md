# Member 领域模型

## 概述

Member 领域模型采用 MVP 方式搭建，聚焦核心认证和归属字段，为后续 C 端用户功能（注册、登录、个人中心）奠定基础。

当前阶段为纯数据模型，不含业务行为。

## 聚合根：MemberEntity

`com.kava.kbpd.member.domain.model.aggregate.MemberEntity`

实现 `AggregateRoot<MemberId>` 接口，MVP 字段：

| 字段 | 类型 | 说明 |
|------|------|------|
| id | MemberId | 主键（值对象） |
| mobile | String | 登录标识 |
| password | String | 认证凭证 |
| tenantId | SysTenantId | 租户归属 |
| appId | SysAppId | App 归属 |
| enabled | Boolean | 启用状态 |

## 值对象

### MemberId

`com.kava.kbpd.common.core.model.valobj.MemberId`

位于 `kbpd-common-core`（跨模块共享），实现 `Identifier` 接口。

- 不可变（`@Value` + `@Builder`）
- 工厂方法：`MemberId.of(Long id)`，null 安全

### SysAppId（提升）

`com.kava.kbpd.common.core.model.valobj.SysAppId`

原在 `kbpd-upms-domain`，因 Member 模型需要关联 App 而提升到 `kbpd-common-core`。

## Repository 接口（读写分离）

遵循项目 CQRS 模式：

### IMemberReadRepository

`com.kava.kbpd.member.domain.repository.IMemberReadRepository`

| 方法 | 说明 |
|------|------|
| `queryById(MemberId id)` | 按 ID 查询 |
| `queryByMobileAndAppId(String mobile, SysAppId appId)` | 按手机号+App 查询 |

### IMemberWriteRepository

`com.kava.kbpd.member.domain.repository.IMemberWriteRepository`

| 方法 | 说明 |
|------|------|
| `create(MemberEntity entity)` | 创建，返回 MemberId |
| `update(MemberEntity entity)` | 更新，返回是否成功 |
| `delete(MemberId id)` | 删除，返回是否成功 |

## 持久化层

### MemberPO

`com.kava.kbpd.member.infrastructure.dao.po.MemberPO`

映射 `mbr_member` 表，继承 `TenantDeletablePO`（含租户隔离和审计字段）。

### MemberMapper

`com.kava.kbpd.member.infrastructure.dao.MemberMapper`

继承 `BaseMapper<MemberPO>`，提供标准 CRUD。

### MemberConverter

`com.kava.kbpd.member.infrastructure.converter.MemberConverter`

MapStruct `@Mapper`，处理值对象（MemberId/SysTenantId/SysAppId）与基础类型（Long）的双向转换。

## 数据库

表名：`mbr_member`（`mbr_` 前缀遵循项目命名约定）

关键索引：
- 主键：`id`
- 唯一索引：`uk_mobile_app (mobile, app_id)`

建表脚本：`docs/01-sql/kbpd-member.sql`

## 关键设计决策

| 决策 | 理由 |
|------|------|
| MemberId 放 common-core | 跨模块共享（auth、member 等），避免反向依赖 |
| SysAppId 提升到 common-core | Member 需关联 App，成为跨模块值对象 |
| 表名 `mbr_` 前缀 | 避免 `member_member` 冗余，与 `sys_` 风格统一 |
| 读写分离 Repository | 项目既定 CQRS 模式 |
| MVP 不含业务行为 | 先建模型骨架，行为后续迭代 |
