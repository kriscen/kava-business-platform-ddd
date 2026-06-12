## 1. kbpd-common-core：值对象准备

- [x] 1.1 在 common-core 中新增 `MemberId` 值对象（实现 Identifier 接口，提供 of 工厂方法）
- [x] 1.2 将 `SysAppId` 从 kbpd-upms-domain 移动到 kbpd-common-core
- [x] 1.3 更新 kbpd-upms 模块中所有 SysAppId 的 import 路径（约 8 个文件）

## 2. kbpd-member-domain：领域模型

- [x] 2.1 创建 `MemberEntity` 聚合根（实现 AggregateRoot<MemberId>，包含 MVP 字段）
- [x] 2.2 创建 `IMemberReadRepository` 接口（按 ID 查询、按 mobile+appId 查询）
- [x] 2.3 创建 `IMemberWriteRepository` 接口（save、update、delete 方法）

## 3. kbpd-member-infrastructure：持久化实现

- [x] 3.1 创建 `MemberPO` 持久化对象（映射 mbr_member 表）
- [x] 3.2 创建 `MemberMapper` 接口（继承 BaseMapper<MemberPO>）
- [x] 3.3 创建 `MemberConverter` 转换器（MapStruct，PO ↔ Entity 互转）
- [x] 3.4 实现 `MemberReadRepository`（注入 MemberMapper，实现查询方法）
- [x] 3.5 实现 `MemberWriteRepository`（注入 MemberMapper，实现持久化方法）

## 4. kbpd-member-api：DTO 更新

- [x] 4.1 更新 `MemberInfoDTO` 对齐 MVP 模型（移除 permissions/roles，新增 tenantId/appId）

## 5. 建表脚本

- [x] 5.1 在 docs/01-sql/ 下新增 kbpd-member.sql，创建 mbr_member 表（含唯一索引 uk_mobile_app）

## 6. 验证

- [x] 6.1 执行 `mvn clean install -pl kbpd-common/kbpd-common-core -am` 验证 common-core 编译通过
- [x] 6.2 执行 `mvn clean install -pl kbpd-member -am` 验证 member 模块编译通过
- [x] 6.3 执行 `mvn clean install` 验证全量编译通过（UPMS application 层有 pre-existing PagingInfo 类型错误，与本次变更无关）
