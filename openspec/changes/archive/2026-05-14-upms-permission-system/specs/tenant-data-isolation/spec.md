## ADDED Requirements

### Requirement: 租户数据自动隔离
系统 SHALL 通过 MyBatis-Plus 拦截器自动为所有租户表查询注入 `tenant_id` 过滤条件，确保租户间数据严格隔离。

#### Scenario: 普通租户用户查询数据
- **WHEN** 租户用户执行数据库查询，且查询的表继承自 TenantDeletablePO
- **THEN** 拦截器自动在 SQL 中追加 `WHERE tenant_id = {当前用户租户ID}`
- **AND** 用户只能看到本租户的数据

#### Scenario: 写入操作自动填充租户 ID
- **WHEN** 租户用户执行 INSERT 操作
- **THEN** 拦截器自动将 tenant_id 设置为当前用户的租户 ID
- **AND** 即使请求未传 tenant_id，数据也归属于正确租户

#### Scenario: 多表 JOIN 查询
- **WHEN** 查询涉及多个租户表的 JOIN
- **THEN** 拦截器为每个租户表都追加 `tenant_id` 条件
- **AND** JOIN 结果不会跨租户泄露数据

### Requirement: 平台管理员跳过租户隔离
平台管理员查询数据时，系统 SHALL 不注入 tenant_id 过滤条件，允许跨租户访问。

#### Scenario: 平台管理员查询租户数据
- **WHEN** 平台管理员执行查询
- **THEN** 拦截器不追加 tenant_id 条件
- **AND** 查询返回所有租户的数据

### Requirement: 特定表跳过租户隔离
系统 SHALL 支持配置不需要租户隔离的表（如系统配置表、平台级字典表）。

#### Scenario: 查询系统配置表
- **WHEN** 查询系统级配置表（已配置在忽略列表中）
- **THEN** 拦截器不追加 tenant_id 条件
