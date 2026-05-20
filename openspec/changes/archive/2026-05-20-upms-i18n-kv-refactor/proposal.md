## Why

UPMS 的 I18n 子域当前是列模式（硬编码 zhCn/en 两列），存在多个结构性问题：CreateCommand/UpdateCommand/DTO 全部是空类导致 CRUD 不可用、ListQuery 无过滤字段、缺少按 key 查询的核心方法、数据模型无法灵活扩展新语言。趁早期阶段将数据模型从列模式重构为 KV 模式，并补全所有空类，使 I18n 子域真正可用。

## What Changes

- **BREAKING**: 数据模型从列模式 (`name, zh_cn, en`) 重构为 KV 模式 (`code, language, content`)，数据库表 `sys_i18n` 改为 `sys_i18n_message`
- 重命名领域对象：`SysI18nEntity` → `SysI18nMessage`，`SysI18nId` → `SysI18nMessageId`，对齐 KV 模型语义
- 新增 `SysLanguageEnum` 枚举（kbpd-upms-types），规范语言标识
- 补全所有空类：`SysI18nCreateCommand`、`SysI18nUpdateCommand`、`SysI18nAppListDTO`、`SysI18nAppDetailDTO`
- `SysI18nListQuery` 增加过滤字段（code, language）
- Repository 增加按 `code + language` 查询方法
- 创建 `ISysI18nMessageService` 接口（无实现，保留扩展点）
- 保持 Entity + 独立 Repository 模式（简单子域，不提升为 AggregateRoot）

## Capabilities

### New Capabilities
- `i18n-kv-management`: I18n 翻译管理——基于 KV 模型的 CRUD、按 code/language 过滤查询、按 key 精确查找

### Modified Capabilities
- `list-query-filter`: SysI18nListQuery 需要新增 code 和 language 过滤字段

## Impact

- **kbpd-upms-domain**: SysI18nEntity 重命名为 SysI18nMessage，字段重构；SysI18nId 重命名为 SysI18nMessageId；SysI18nListQuery 增加过滤字段；Repository 接口增加查询方法；新增 ISysI18nMessageService 接口
- **kbpd-upms-application**: 补全 CreateCommand/UpdateCommand/AppListDTO/AppDetailDTO 的字段；AppService 适配新模型
- **kbpd-upms-infrastructure**: PO 重命名并适配新字段；Converter 适配；Repository 实现增加过滤和查询逻辑；Mapper XML 适配
- **kbpd-upms-adapter**: Controller 和 AdapterConverter 适配新模型
- **kbpd-upms-api**: Request/Response 适配新字段（code, language, content）
- **kbpd-upms-types**: 新增 SysLanguageEnum
- **kbpd-upms-bootstrap**: SysI18nMapper.xml 适配；SQL 迁移脚本
- **docs/01-sql**: 表结构 DDL 变更
