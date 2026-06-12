## ADDED Requirements

### Requirement: Member 聚合根定义
系统 SHALL 定义 `MemberEntity` 聚合根，实现 `AggregateRoot<MemberId>` 接口。

#### Scenario: Member 聚合根包含 MVP 字段
- **WHEN** 创建 MemberEntity 实例
- **THEN** 必须包含以下字段：id (MemberId), mobile (String), password (String), tenantId (SysTenantId), appId (SysAppId), enabled (Boolean)

#### Scenario: Member 聚合根实现 identifier 方法
- **WHEN** 调用 MemberEntity.identifier()
- **THEN** 返回 MemberId 类型的 id 字段

### Requirement: MemberId 值对象定义
系统 SHALL 在 `kbpd-common-core` 中定义 `MemberId` 值对象，实现 `Identifier` 接口。

#### Scenario: MemberId 提供工厂方法
- **WHEN** 调用 MemberId.of(Long id)
- **THEN** 返回 MemberId 实例；若 id 为 null 则返回 null

#### Scenario: MemberId 不可变
- **WHEN** 创建 MemberId 实例
- **THEN** 所有字段为 final，不提供 setter

### Requirement: SysAppId 提升到 common-core
系统 SHALL 将 `SysAppId` 从 `kbpd-upms-domain` 移动到 `kbpd-common-core`。

#### Scenario: SysAppId 保持原有功能
- **WHEN** 使用 SysAppId.of(Long id) 创建实例
- **THEN** 行为与原实现一致

#### Scenario: UPMS 模块更新 import
- **WHEN** UPMS 模块引用 SysAppId
- **THEN** import 路径指向 `com.kava.kbpd.common.core.model.valobj.SysAppId`

### Requirement: Member Repository 接口定义
系统 SHALL 定义读写分离的 Repository 接口。

#### Scenario: ReadRepository 提供查询方法
- **WHEN** 需要查询 Member 数据
- **THEN** 提供 IMemberReadRepository 接口，包含按 ID 查询、按 mobile 查询等方法

#### Scenario: WriteRepository 提供持久化方法
- **WHEN** 需要保存/更新/删除 Member 数据
- **THEN** 提供 IMemberWriteRepository 接口，包含 save、update、delete 等方法
