## ADDED Requirements

### Requirement: SysI18nListQuery 包含翻译消息过滤字段
SysI18nListQuery MUST 包含对应实体的实际过滤条件字段。

#### Scenario: SysI18nListQuery 包含过滤字段
- **WHEN** 查询翻译消息列表
- **THEN** SysI18nListQuery MUST 包含以下可选过滤字段：code（String）、language（String）
- **AND** 所有过滤字段为可选（nullable）

#### Scenario: 翻译消息列表查询按条件过滤
- **WHEN** SysI18nListQuery 中 code 不为空
- **THEN** Repository 实现 MUST 在 SQL 查询中添加 code LIKE 条件
- **WHEN** SysI18nListQuery 中 language 不为空
- **THEN** Repository 实现 MUST 在 SQL 查询中添加 language = 条件
