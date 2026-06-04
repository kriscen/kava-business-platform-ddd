## Why

项目审查发现文档与代码存在多处不一致：过时的状态标记、错误码格式描述与实际实现不符、CLAUDE.md 中 domain 层约束措辞过于绝对、增量迁移脚本未合并到基线、以及 SQL 字段类型不统一。这些不一致会误导新成员上手和日常开发判断，需要在项目早期统一修正。

## What Changes

- **文档同步**：更新 `business-rules.md` 中 MetaObjectHandler 状态（❌ → ✅），清理已完成但仍留在"待实现"表中的条目
- **错误码格式统一**：确认代码实际使用的错误码格式（`A00101` vs `10010001`），统一 `error-codes.md` 描述
- **BOM 清理**：移除 `kbpd-common-bom` 中不存在的 `kbpd-auth-api` 幽灵依赖
- **CLAUDE.md 措辞修正**：将 "domain 不依赖任何外部框架" 改为更准确的描述，与 `ddd-rules.md` 保持一致
- **迁移脚本合并**：将 V2/V3/V4 增量 SQL 的结构变更合并到 `kbpd-upms.sql` 基线脚本中，清理无用的数据迁移语句
- **SQL 类型对齐**：将 `sys_area.id` 从 `bigint unsigned` 统一为 `bigint`（与其他表一致）

## Capabilities

### New Capabilities

无新增能力。

### Modified Capabilities

- `ddd-compliance`：CLAUDE.md 中 domain 层约束措辞需要与 ddd-rules.md 对齐
- `error-code-convention`：错误码格式描述需要与代码实际实现统一

## Impact

| 影响范围 | 文件 |
|---------|------|
| 文档 | `docs/06-modules/kbpd-upms/business-rules.md` |
| 文档 | `docs/04-reference/error-codes.md` |
| 文档 | `CLAUDE.md` |
| BOM | `kbpd-common/kbpd-common-bom/pom.xml` |
| SQL | `docs/01-sql/kbpd-upms.sql` |
| SQL | `docs/01-sql/V2__tenant_add_time_columns.sql`（合并后可删除） |
| SQL | `docs/01-sql/V3__menu_app_model.sql`（合并后可删除） |
| SQL | `docs/01-sql/V4__rename_dept_to_group.sql`（合并后可删除） |
| 索引 | `docs/00-project-map.md`（删除 V2/V3/V4 后需更新） |
