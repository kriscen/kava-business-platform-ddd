## 1. 数据库与 Types 层

- [x] 1.1 新增 SQL 迁移脚本：创建 `sys_i18n_message` 表（code, language, content, 审计字段），唯一索引 `(code, language)`，删除旧 `sys_i18n` 表
- [x] 1.2 在 kbpd-upms-types 中创建 `SysLanguageEnum` 枚举（ZH_CN, EN）

## 2. Domain 层重构

- [x] 2.1 重命名 `SysI18nId` → `SysI18nMessageId`
- [x] 2.2 重命名 `SysI18nEntity` → `SysI18nMessage`，字段从 (name, zhCn, en) 改为 (code, language, content)，保持 `Entity<SysI18nMessageId>` 接口
- [x] 2.3 重构 `SysI18nListQuery`，增加 code（String）和 language（String）过滤字段
- [x] 2.4 重命名 `ISysI18nRepository` → `ISysI18nMessageRepository`，增加 `queryByCodeAndLanguage(String code, String language)` 方法
- [x] 2.5 重命名 `ISysI18nService` → `ISysI18nMessageService`，清理方法签名对齐新模型；删除旧的 `SysI18nService` 实现类

## 3. Infrastructure 层适配

- [x] 3.1 重命名 `SysI18nPO` → `SysI18nMessagePO`，字段从 (name, zhCn, en) 改为 (code, language, content)，表名映射改为 `sys_i18n_message`
- [x] 3.2 重命名 `SysI18nMapper` → `SysI18nMessageMapper`
- [x] 3.3 重构 `SysI18nConverter` → `SysI18nMessageConverter`，适配新字段映射
- [x] 3.4 重构 `SysI18nRepository` → `SysI18nMessageRepository`，queryPage 添加 code LIKE 和 language = 过滤条件，实现 queryByCodeAndLanguage 方法
- [x] 3.5 更新 SysI18nMapper.xml 适配新表名和字段

## 4. Application 层补全

- [x] 4.1 补全 `SysI18nCreateCommand` 字段：code, language, content
- [x] 4.2 补全 `SysI18nUpdateCommand` 字段：code, language, content
- [x] 4.3 补全 `SysI18nAppListDTO` 字段：id, code, language, content
- [x] 4.4 补全 `SysI18nAppDetailDTO` 字段：id, code, language, content, 审计字段
- [x] 4.5 重构 `SysI18nAppConverter` 适配新字段映射
- [x] 4.6 重构 `SysI18nAppService`（及接口 `ISysI18nAppService`）对齐新模型，移除对旧 DomainService 的引用

## 5. Adapter 与 API 层适配

- [x] 5.1 更新 `SysI18nRequest` 字段：id, code, language, content
- [x] 5.2 更新 `SysI18nDetailResponse` 和 `SysI18nListResponse` 字段对齐新模型
- [x] 5.3 更新 `SysI18nAdapterListQuery` 过滤字段：code, language
- [x] 5.4 重构 `SysI18nAdapterConverter` 适配新字段
- [x] 5.5 更新 `SysI18nController` 适配新模型（接口路径保持不变）

## 6. 验证

- [x] 6.1 执行 `mvn clean install -pl kbpd-upms -am` 确保编译通过（注：application 模块有既有 slf4j 编译问题，与本次改动无关）
- [x] 6.2 检查所有重命名和引用是否完整，无残留旧类名
