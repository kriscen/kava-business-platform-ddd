## 1. BOM 清理

- [x] 1.1 移除 `kbpd-common/kbpd-common-bom/pom.xml` 中不存在的 `kbpd-auth-api` 依赖声明

## 2. CLAUDE.md 措辞修正

- [x] 2.1 修改 `CLAUDE.md` 中架构部分的 domain 层描述，将"domain 不依赖任何外部框架"改为"domain 不依赖 Spring Boot / MyBatis 等重型框架，允许轻量 spring-context 注解"，与 `ddd-rules.md` 保持一致

## 3. 文档同步 — business-rules.md

- [x] 3.1 更新 `docs/06-modules/kbpd-upms/business-rules.md` 中 MetaObjectHandler 状态：从"❌ 未实现"改为"✅ 已实现"，并移至"已实现的业务规则"区域
- [x] 3.2 将 `business-rules.md` 错误码表中的简写格式（A00101 等）统一为 8 位数字格式（10010001 等），与 `error-codes.md` 和代码一致
- [x] 3.3 补充 `business-rules.md` 错误码表中缺失的 App（10-09）和 TenantApp（10-10）子模块错误码

## 4. SQL 基线合并

- [x] 4.1 验证 `docs/01-sql/kbpd-upms.sql` 基线已包含 V2（start_time/end_time）、V3（sys_app/sys_app_menu/sys_tenant_app/level 列）、V4（sys_group/group_id）的所有结构变更
- [x] 4.2 将 `sys_area.id` 从 `bigint unsigned` 改为 `bigint`，与其他表主键类型一致
- [x] 4.3 删除已合并的增量迁移脚本：`V2__tenant_add_time_columns.sql`、`V3__menu_app_model.sql`、`V4__rename_dept_to_group.sql`
- [x] 4.4 更新 `docs/00-project-map.md`，移除 V2/V3/V4 的索引条目

## 5. 验证

- [x] 5.1 运行 `mvn clean install -DskipTests` 确认 BOM 修改不影响编译（Maven 不可用，已跳过）
- [x] 5.2 检查 `docs/00-project-map.md` 索引与实际文件一致
- [x] 5.3 通读修改后的文档，确认无遗漏的不一致
