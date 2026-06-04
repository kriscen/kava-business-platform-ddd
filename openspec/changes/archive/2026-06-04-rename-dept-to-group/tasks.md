## 1. Domain 层重命名

- [x] 1.1 `SysDeptId` → `SysGroupId`（值对象，含静态工厂方法）
- [x] 1.2 `SysGroupEntity` → `SysGroupEntity`（字段 pid 类型同步改为 SysGroupId）
- [x] 1.3 `SysGroupListQuery` → `SysGroupListQuery`（字段 deptName → groupName）
- [x] 1.4 `ISysDeptRepository` → `ISysGroupRepository`
- [x] 1.5 `ISysDeptService` → `ISysGroupService`，`SysGroupService` → `SysGroupService`（错误码描述文本更新）

## 2. Application 层重命名

- [x] 2.1 `SysGroupCreateCommand` → `SysGroupCreateCommand`，`SysGroupUpdateCommand` → `SysGroupUpdateCommand`
- [x] 2.2 `SysGroupAppDetailDTO` → `SysGroupAppDetailDTO`，`SysGroupAppListDTO` → `SysGroupAppListDTO`
- [x] 2.3 `ISysGroupAppService` → `ISysGroupAppService`，`SysGroupAppService` → `SysGroupAppService`
- [x] 2.4 `SysGroupAppConverter` → `SysGroupAppConverter`

## 3. Adapter 层重命名

- [x] 3.1 `SysGroupController` → `SysGroupController`（API 路径 `/group/` → `/group/`）
- [x] 3.2 `SysGroupAdapterConverter` → `SysGroupAdapterConverter`
- [x] 3.3 `SysGroupRequest` → `SysGroupRequest`，`SysGroupDetailResponse` → `SysGroupDetailResponse`，`SysGroupListResponse` → `SysGroupListResponse`，`SysGroupAdapterListQuery` → `SysGroupAdapterListQuery`

## 4. Infrastructure 层重命名

- [x] 4.1 `SysGroupPO` → `SysGroupPO`（表名注解 `sys_group` → `sys_group`）
- [x] 4.2 `SysGroupMapper` → `SysGroupMapper`，`SysGroupMapper.xml` → `SysGroupMapper.xml`
- [x] 4.3 `SysGroupConverter` → `SysGroupConverter`
- [x] 4.4 `SysGroupRepository` → `SysGroupRepository`（引用的 Mapper 和用户引用检查同步更新）

## 5. Types 层重命名

- [x] 5.1 `SysRoleDataScope` 枚举值 `DEPT_AND_CHILD` → `GROUP_AND_CHILD`、`DEPT_ONLY` → `GROUP_ONLY`
- [x] 5.2 `UpmsBizErrorCodeEnum` 中 dept 相关错误码描述文本"部门"→"分组"

## 6. User 聚合关联字段重命名

- [x] 6.1 `SysUserEntity` 中 `deptId` → `groupId`（类型 SysDeptId → SysGroupId）
- [x] 6.2 User API Request/Response 中 `deptId` → `groupId`、`deptName` → `groupName`
- [x] 6.3 User Application 层 DTO 和 Command 中 `deptId` → `groupId`、`deptName` → `groupName`
- [x] 6.4 User AppService 中引用 Dept 的调用改为 Group
- [x] 6.5 `SysUserPO` 中 `deptId` → `groupId`（列名注解同步更新）
- [x] 6.6 `SysUserListQuery` 中 `deptId` → `groupId`

## 7. kbpd-common-core 跨模块修改

- [x] 7.1 `UserContext` 中 `deptId` → `groupId`
- [x] 7.2 `JwtClaimConstants` 中 dept 相关 claim key 更新
- [x] 7.3 `SecurityUtils` 中 `getDeptId()` → `getGroupId()`（如存在）

## 8. 数据库迁移

- [x] 8.1 创建 SQL 迁移脚本 `V4__rename_dept_to_group.sql`：`RENAME TABLE sys_dept TO sys_group`，`ALTER TABLE sys_group RENAME COLUMN ... TO ...`，`ALTER TABLE sys_user RENAME COLUMN dept_id TO group_id`

## 9. 文档和 Specs 更新

- [x] 9.1 `openspec/specs/dept-domain-service/` → `group-domain-service/`，内容中 Dept→Group 替换
- [x] 9.2 `openspec/specs/dept-tree-validation/` → `group-tree-validation/`，内容中 Dept→Group 替换
- [x] 9.3 更新 `openspec/specs/` 下其他 specs 中的 dept 引用（permission-system、unified-user-context、dropdown-selector-api、response-enrichment、list-query-filter）
- [x] 9.4 更新 `docs/06-modules/kbpd-upms/` 下所有文档中的 dept 引用
- [x] 9.5 更新 `docs/05-frontend/upms-api.md` 中 API 路径和字段名
- [x] 9.6 更新 `docs/02-architecture/` 和 `docs/07-product/` 中的 dept 引用
- [x] 9.7 更新 `kbpd-upms.sql` 中的表名和列名
- [x] 9.8 运行 `/update-docs-map` 同步索引

## 10. 验证

- [x] 10.1 全局搜索 `dept`/`Dept`/`DEPT`（不区分大小写），确认无遗漏引用
- [x] 10.2 `mvn clean install -pl kbpd-upms -am -DskipTests` 编译通过
- [x] 10.3 `mvn clean install -pl kbpd-common -am -DskipTests` 编译通过
