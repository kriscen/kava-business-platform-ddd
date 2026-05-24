## MODIFIED Requirements

### Requirement: 审计字段自动填充

系统 SHALL 在执行数据库 insert 操作时自动填充 creator、gmtCreate、modifier、gmtModified、delFlag 字段，在执行 update 操作时自动填充 modifier、gmtModified 字段。

PO 实体中需使用 MyBatis-Plus 的 `@TableField(fill = FieldFill.INSERT)` 和 `@TableField(fill = FieldFill.INSERT_UPDATE)` 注解标记待填充字段。

系统 SHALL 在 `kbpd-common-database` 模块中提供 `MybatisMetaObjectHandler` 实现 `MetaObjectHandler` 接口，通过 `@Component` 注册为 Spring Bean。

#### Scenario: 新增记录时自动填充审计字段

- **WHEN** 通过 MyBatis-Plus 向任意表插入一条新记录，且 PO 实体的 creator、gmtCreate、modifier、gmtModified 字段标注了填充注解
- **THEN** 系统 SHALL 自动将 creator 和 modifier 设为当前认证用户的标识（通过 `SecurityUtils.getUserContext().getUserId()` 获取），gmtCreate 和 gmtModified 设为当前时间，delFlag 设为 "0"

#### Scenario: 更新记录时自动填充审计字段

- **WHEN** 通过 MyBatis-Plus 更新一条记录，且 PO 实体的 modifier、gmtModified 字段标注了填充注解
- **THEN** 系统 SHALL 自动将 modifier 设为当前认证用户的标识（通过 `SecurityUtils.getUserContext().getUserId()` 获取），gmtModified 设为当前时间

#### Scenario: 无认证上下文时的回退

- **WHEN** 系统在没有认证上下文的情况下（如定时任务、系统初始化）执行数据库写入
- **THEN** creator 和 modifier SHALL 设为固定值 "system"
