## Purpose

国际化翻译消息管理，采用 KV 模式存储多语言翻译内容。

## Requirements

### Requirement: I18n 消息数据模型采用 KV 模式
SysI18nMessage 实体 MUST 使用 KV 模式存储翻译，包含 code（翻译键）、language（语言标识）、content（翻译内容）三个核心字段。表名 MUST 为 `sys_i18n_message`。

#### Scenario: KV 模型数据结构
- **WHEN** 创建一条翻译记录
- **THEN** SysI18nMessage MUST 包含 code（String）、language（String）、content（String）字段
- **AND** code 和 language 的组合 MUST 在数据库中唯一（唯一索引 uk_code_language）

#### Scenario: 同一 code 支持多语言
- **WHEN** code 为 "btn.save" 时
- **THEN** 可以存在多条记录：language="zh_CN" content="保存"、language="en" content="Save"
- **AND** 每条记录是独立的 SysI18nMessage 实体

### Requirement: I18n 消息完整 CRUD
系统 MUST 支持对 SysI18nMessage 的创建、修改、删除、分页查询和详情查询。

#### Scenario: 创建翻译消息
- **WHEN** 管理员提交 code="btn.save"、language="zh_CN"、content="保存"
- **THEN** 系统 MUST 创建一条 SysI18nMessage 记录
- **AND** code 和 language 的组合 MUST NOT 重复

#### Scenario: 创建重复翻译消息
- **WHEN** 已存在 code="btn.save" language="zh_CN" 的记录
- **AND** 管理员再次提交相同的 code + language 组合
- **THEN** 系统 MUST 返回错误提示

#### Scenario: 修改翻译内容
- **WHEN** 管理员修改某条记录的 content 为新值
- **THEN** 系统 MUST 更新该记录的 content 字段
- **AND** code 和 language MUST NOT 被修改

#### Scenario: 批量删除翻译消息
- **WHEN** 管理员选择多条记录进行删除
- **THEN** 系统 MUST 软删除（设置 del_flag=1）选中的记录

#### Scenario: 分页查询翻译消息
- **WHEN** 管理员请求翻译消息列表
- **THEN** 系统 MUST 返回分页结果，包含 code、language、content 字段

#### Scenario: 查询翻译消息详情
- **WHEN** 管理员点击某条翻译记录
- **THEN** 系统 MUST 返回该记录的完整信息（id、code、language、content、审计字段）

### Requirement: I18n 消息支持按 code 和 language 过滤查询
SysI18nListQuery MUST 包含 code 和 language 过滤字段，Repository 实现 MUST 使用这些字段构建查询条件。

#### Scenario: 按 code 模糊查询
- **WHEN** 管理员输入 code 过滤条件为 "btn"
- **THEN** 系统 MUST 返回 code 包含 "btn" 的记录（LIKE 查询）

#### Scenario: 按语言精确查询
- **WHEN** 管理员选择 language 过滤条件为 "zh_CN"
- **THEN** 系统 MUST 返回 language 等于 "zh_CN" 的记录

#### Scenario: 组合条件查询
- **WHEN** 管理员同时指定 code="btn" 和 language="en"
- **THEN** 系统 MUST 返回同时满足两个条件的记录

### Requirement: I18n 消息支持按 code 和 language 精确查找
Repository MUST 提供按 code + language 精确查找的方法，用于运行时获取翻译内容。

#### Scenario: 按 code + language 精确查找
- **WHEN** 调用 queryByCodeAndLanguage("btn.save", "zh_CN")
- **THEN** 系统 MUST 返回对应的 SysI18nMessage（del_flag=0）
- **AND** 如果不存在 MUST 返回 null

### Requirement: 语言标识枚举
系统 MUST 提供 SysLanguageEnum 枚举，定义支持的语言标识。language 字段类型保持 String，枚举作为推荐值。

#### Scenario: 枚举包含基础语言
- **WHEN** 查看SysLanguageEnum
- **THEN** MUST 包含 ZH_CN("zh_CN") 和 EN("en") 至少两个枚举值

### Requirement: DomainService 保留接口扩展点
MUST 创建 ISysI18nMessageService 接口及其实现类。实现类采用委托型模式，注入 ISysI18nMessageRepository 并将所有方法转发至 Repository。AppService 通过 Domain Service 调用 Repository。

#### Scenario: DomainService 接口和实现类存在
- **WHEN** 查看领域服务层
- **THEN** MUST 存在 ISysI18nMessageService 接口
- **AND** MUST 存在对应的实现类 SysI18nMessageService
- **AND** 实现类 MUST 注入 ISysI18nMessageRepository
- **AND** 实现类的 5 个方法（create、update、queryPage、queryById、removeBatchByIds）MUST 委托给 Repository

#### Scenario: AppService 通过 DomainService 调用
- **WHEN** SysI18nAppService 执行任何数据操作
- **THEN** MUST 通过 ISysI18nMessageService 调用
- **AND** MUST NOT 直接注入 ISysI18nMessageRepository
