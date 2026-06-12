# member-persistence

## Purpose

定义会员（Member）持久化层的实现规范，包括 PO 对象、MyBatis Mapper、PO-DO 转换器和 Repository 实现。负责将 Member 领域模型持久化到数据库。

## Requirements

### Requirement: Member 持久化对象定义

系统 SHALL 定义 `MemberPO` 持久化对象，映射 `mbr_member` 表。

#### Scenario: PO 字段与表结构对应

- **WHEN** MemberPO 映射到 mbr_member 表
- **THEN** 字段对应关系：id→id, mobile→mobile, password→password, tenant_id→tenantId, app_id→appId, enabled→enabled

#### Scenario: PO 包含审计字段

- **WHEN** MemberPO 持久化
- **THEN** 包含 create_time、update_time 字段

### Requirement: Member MyBatis Mapper

系统 SHALL 定义 `MemberMapper` 接口，继承 `BaseMapper<MemberPO>`。

#### Scenario: Mapper 提供基础 CRUD

- **WHEN** 使用 MemberMapper
- **THEN** 继承 MyBatis-Plus 的 BaseMapper，提供标准 CRUD 操作

### Requirement: Member PO-DO 转换器

系统 SHALL 使用 MapStruct 实现 `MemberConverter`，完成 PO 与领域对象的双向转换。

#### Scenario: PO 转换为领域对象

- **WHEN** 调用 MemberConverter.toEntity(MemberPO)
- **THEN** 返回 MemberEntity，字段正确映射，ID 转换为值对象

#### Scenario: 领域对象转换为 PO

- **WHEN** 调用 MemberConverter.toPO(MemberEntity)
- **THEN** 返回 MemberPO，字段正确映射，值对象拆解为基础类型

### Requirement: Member Repository 实现

系统 SHALL 实现 `MemberReadRepository` 和 `MemberWriteRepository`。

#### Scenario: ReadRepository 实现查询

- **WHEN** 调用 IMemberReadRepository 的查询方法
- **THEN** 通过 MemberMapper 查询数据库，使用 MemberConverter 转换后返回领域对象

#### Scenario: WriteRepository 实现持久化

- **WHEN** 调用 IMemberWriteRepository 的持久化方法
- **THEN** 使用 MemberConverter 转换为 PO，通过 MemberMapper 写入数据库

### Requirement: mbr_member 建表脚本

系统 SHALL 提供 `mbr_member` 表的建表 SQL 脚本。

#### Scenario: 表结构包含 MVP 字段

- **WHEN** 执行建表脚本
- **THEN** 创建 mbr_member 表，包含 id, mobile, password, tenant_id, app_id, enabled, create_time, update_time 字段

#### Scenario: 表包含必要索引

- **WHEN** 执行建表脚本
- **THEN** 创建唯一索引：uk_mobile_app (mobile, app_id)
