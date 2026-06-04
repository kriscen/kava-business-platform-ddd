## Context

项目审查发现以下不一致问题：

1. **文档过时**：`business-rules.md` 中 MetaObjectHandler 标记为"❌ 未实现"，但代码中 `KavaMetaObjectHandler` 已正确实现
2. **错误码格式不一致**：`error-codes.md` 定义 8 位数字格式（`10010001`），但 `UpmsBizErrorCodeEnum` 实际使用缩写格式（如 `A00101`）。需确认哪个是真实来源
3. **BOM 幽灵依赖**：`kbpd-common-bom` 声明了 `kbpd-auth-api`，但该模块不存在
4. **CLAUDE.md 措辞过于绝对**：写道"domain 不依赖任何外部框架"，与 `ddd-rules.md` 允许 `spring-context` 的实际规则冲突
5. **增量迁移脚本未合并**：V2/V3/V4 是增量变更，在从 0 到 1 的项目中应合并到基线 `kbpd-upms.sql`
6. **SQL 字段类型不一致**：`sys_area.id` 为 `bigint unsigned`，其他表均为 `bigint`

## Goals / Non-Goals

**Goals:**
- 文档与代码保持一致，消除误导
- SQL 基线脚本自包含，新环境部署无需执行增量迁移
- BOM 声明干净，无幽灵依赖
- CLAUDE.md 与 ddd-rules.md 约束描述对齐

**Non-Goals:**
- 不修改任何 Java 代码逻辑
- 不激活 DataScope 拦截器（属于独立 change）
- 不补充测试覆盖（属于独立 change）
- 不修改 `kbpd-member-types` 的 `spring-boot-starter-web` 依赖（属于独立 change）

## Decisions

### 决策 1：错误码格式以代码为准

**选择**：以 `UpmsBizErrorCodeEnum` 代码中的实际格式为真实来源，更新 `error-codes.md` 文档。

**Because**：代码是运行时的真实行为，文档应反映代码实际。需先确认 `CommonErrorCodeEnum` 和 `UpmsBizErrorCodeEnum` 中的实际枚举值格式，再统一文档描述。

**备选方案**：修改代码匹配文档格式 → 改动量大且涉及运行时行为，风险高。

### 决策 2：迁移脚本合并策略

**选择**：将 V2/V3/V4 中的**结构性变更**（DDL）合并到 `kbpd-upms.sql` 基线，删除 V2/V3/V4 文件。

**Because**：从 0 到 1 的项目无历史数据，增量迁移脚本中的 DDL 变更应直接体现在基线中。V3 中的数据迁移语句（UPDATE/INSERT）在空表上无意义，不合并。

**具体操作**：
- V2 的 `start_time`/`end_time` 列已存在于 `kbpd-upms.sql` 的 `sys_tenant` 表中 → 无需操作
- V3 的 `sys_app`/`sys_app_menu`/`sys_tenant_app` 表已存在于基线中 → 无需操作
- V3 的 `scope → level` 重命名 → 确认基线中 `sys_menu` 已使用 `level` 列
- V3 的 `sys_tenant DROP COLUMN menu_id` → 确认基线中已无 `menu_id` 列
- V4 的 `sys_dept → sys_group` 重命名 → 确认基线中已使用 `sys_group`
- 确认完毕后删除 V2/V3/V4 文件

### 决策 3：sys_area.id 类型对齐

**选择**：将 `sys_area.id` 从 `bigint unsigned` 改为 `bigint`。

**Because**：项目中所有其他表的主键均为 `bigint`（signed），保持一致性。`bigint unsigned` 与 `bigint` 在 ID 生成策略（如雪花算法）下无实际差异，但类型不一致会给 MapStruct 转换和 MyBatis-Plus 映射带来潜在问题。

## Risks / Trade-offs

| 风险 | 缓解措施 |
|------|---------|
| 错误码格式确认需检查代码实际返回值 | 先读取枚举源码确认真实格式，再更新文档 |
| 删除迁移脚本后 git 历史丢失 | 迁移脚本仍可通过 git log 追溯，且基线已包含所有变更 |
| SQL 基线修改可能影响已有开发环境 | 从 0 到 1 项目，无生产数据，影响可控 |
