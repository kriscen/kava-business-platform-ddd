## Context

UPMS 的 I18n 子域是 Admin 管理后台专用的翻译管理功能，属于平台级资源（不带 tenant_id）。当前采用列模式存储（name, zh_cn, en），存在以下问题：

- 数据模型硬编码语言列，扩展新语言需 ALTER TABLE
- Application 层的 CreateCommand、UpdateCommand、AppListDTO、AppDetailDTO 全部是空类，CRUD 实际不可用
- ListQuery 无过滤字段，分页查询无法筛选
- 缺少按 code + language 查找的核心方法
- DomainService 是纯存根（所有方法 throw UnsupportedOperationException）

## Goals / Non-Goals

**Goals:**
- 将数据模型从列模式重构为 KV 模式（code, language, content）
- 补全所有空类，使 CRUD 全链路可用
- 增加 code 和 language 过滤查询能力
- 新增 SysLanguageEnum 规范语言标识
- 保持简单子域设计（Entity + 独立 Repository，不提升为 AggregateRoot）

**Non-Goals:**
- 不实现缓存层（后续按需加入）
- 不实现 Spring MessageSource 集成（后续按需加入）
- 不实现租户级 i18n（平台级资源，不涉及租户隔离）
- 不实现批量导入/导出翻译
- 不重构其他子域

## Decisions

### D1: 数据模型采用单表 KV 模式

**选择**: 单表 `sys_i18n_message`，字段 `code | language | content`

**替代方案**:
- 列模式（当前）：加语言需改表，不符合扩展预期
- 双表（key + value）：管理体验好但增加复杂度，i18n 是附属功能不值得
- JSON 字段：MySQL 下 TypeHandler 额外工作，收益不大

**Because**: i18n 是附属功能，单表 KV 在灵活性和复杂度之间取得平衡。加新语言只需插入数据行。

### D2: 保持 Entity 模式，不提升为 AggregateRoot

**选择**: `SysI18nMessage` 实现 `Entity<SysI18nMessageId>`

**Because**: SysI18nMessage 是单实体、无跨对象不变量、简单 CRUD。作为简单子域，Entity + 独立 Repository 足够。强行加 AggregateRoot 语义不准确。

### D3: 新增 SysLanguageEnum 但不做强制校验

**选择**: 在 kbpd-upms-types 中创建 `SysLanguageEnum`（ZH_CN, EN），language 字段类型为 String

**Because**: 枚举提供管理端下拉约束和代码可读性，但 language 字段保持 String 类型避免加新语言时必须改枚举。枚举作为推荐值，不作为硬约束。

### D4: DomainService 只创建接口，不创建实现

**选择**: 创建 `ISysI18nMessageService` 接口，不创建实现类

**Because**: 当前只有简单 CRUD，AppService 直接编排 Repository 即可。保留接口作为扩展点，按需实现。

### D5: 表名从 sys_i18n 改为 sys_i18n_message

**Because**: KV 模式下一行代表一条翻译消息，`message` 后缀更准确表达语义，也与领域对象名 `SysI18nMessage` 对齐。

## Risks / Trade-offs

- **[数据迁移]** 现有 sys_i18n 表数据需迁移到新结构 → 提供迁移 SQL，当前为开发阶段数据量小可手动处理
- **[命名变更]** 所有 I18n 相关类重命名 → 全局影响但当前无外部依赖，IDE 重构可控
- **[language 字段为 String]** 可能写入非枚举值 → 管理端 API 层通过校验约束，数据库层不做 CHECK
