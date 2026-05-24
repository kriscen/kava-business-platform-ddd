# Capability: tenant-system

## Purpose

定义租户体系：租户 CRUD 管理（菜单分配、角色初始化）+ 多租户数据隔离（MyBatis-Plus 拦截器自动过滤），覆盖租户从创建到数据隔离的完整生命周期。

## Requirements

### Requirement: 租户 CRUD 管理

系统 SHALL 提供平台管理员管理租户的完整 CRUD 接口（创建、查询、更新、删除），并增加状态变更端点。创建和更新接口 SHALL 使用 `SysTenantStatus` 枚举，创建时默认状态为 `NORMAL`。

Tenant 实体 MUST 使用 `SysTenantId` 值对象作为 ID 类型，与其他子域保持一致的 ID 值对象模式。

#### Scenario: 创建租户
- **WHEN** 平台管理员提交租户创建请求，包含 name、code、tenantDomain、startTime、endTime 等
- **THEN** 系统校验 code 唯一性
- **AND** 创建租户记录，状态默认为 NORMAL
- **AND** 自动初始化 tenant_admin 角色
- **AND** 若指定了管理员信息则创建管理员用户
- **AND** 返回新创建的租户 ID

#### Scenario: 查询租户列表
- **WHEN** 平台管理员查询租户列表（支持分页）
- **THEN** 返回所有租户的列表信息，包含 name、code、status、startTime、endTime

#### Scenario: 更新租户信息
- **WHEN** 平台管理员更新租户的 name、code、websiteName 等字段
- **THEN** 若 code 变更则校验唯一性
- **AND** 系统更新租户记录

#### Scenario: 删除租户
- **WHEN** 平台管理员删除租户
- **THEN** 系统软删除租户记录（设置 del_flag）
- **AND** 租户下用户无法再登录

#### Scenario: SysTenantId 值对象在 Tenant 实体中使用
- **WHEN** Tenant 实体（SysTenantEntity）需要引用自身 ID
- **THEN** MUST 使用 `SysTenantId` 值对象而非裸 Long 类型
- **AND** SysTenantId 值对象 MUST 包含 `Long getValue()` 方法
- **AND** SysTenantId MUST 提供 `of(Long value)` 静态工厂方法

#### Scenario: 停用租户
- **WHEN** 平台管理员对租户执行停用操作（PUT /api/{version}/sys/tenant/{id}/disable）
- **THEN** 系统校验当前状态并变更为 DISABLED

#### Scenario: 启用租户
- **WHEN** 平台管理员对租户执行启用操作（PUT /api/{version}/sys/tenant/{id}/enable）
- **THEN** 系统校验当前状态并变更为 NORMAL

### Requirement: 租户菜单分配

平台管理员 SHALL 能为租户分配可用的 SYSTEM_TENANT 菜单范围。

#### Scenario: 为租户分配菜单
- **WHEN** 平台管理员为租户分配 SYSTEM_TENANT 菜单 ID 列表
- **THEN** 系统更新 sys_tenant.menu_id 字段
- **AND** 租户管理员此后可看到并使用这些菜单

#### Scenario: 缩小租户菜单范围
- **WHEN** 平台管理员减少租户的菜单分配
- **THEN** 已分配但不在新范围内的菜单变为不可见
- **AND** 租户下引用了被移除菜单的角色需要清理关联（可标记异常或由管理员手动处理）

#### Scenario: 分配非 SYSTEM_TENANT 菜单
- **WHEN** 平台管理员尝试为租户分配 scope 为 SYSTEM 的菜单
- **THEN** 系统拒绝操作并返回错误

### Requirement: 新租户自动初始化必备角色

创建租户时，系统 SHALL 自动创建一个"租户管理员"角色，关联该租户所有已分配菜单的权限。

#### Scenario: 创建租户时自动创建管理员角色
- **WHEN** 平台管理员创建租户并分配了菜单
- **THEN** 系统自动创建角色 code 为 `tenant_admin` 的角色
- **AND** 该角色关联租户所有已分配的 SYSTEM_TENANT 菜单
- **AND** 该角色的 dsType 为 ALL（租户全数据可见）

### Requirement: 租户管理员初始用户绑定

创建租户时，系统 SHALL 支持指定一个初始管理员用户，并将其绑定到自动创建的租户管理员角色。

#### Scenario: 创建租户时指定管理员
- **WHEN** 平台管理员创建租户，并指定了初始管理员的 username 和 password
- **THEN** 系统创建该用户记录
- **AND** 将用户绑定到租户管理员角色
- **AND** 该用户可立即登录并管理租户

#### Scenario: 创建租户时未指定管理员
- **WHEN** 平台管理员创建租户，未指定初始管理员
- **THEN** 系统仅创建租户和管理员角色，不创建用户
- **AND** 管理员角色暂时无用户绑定，后续可手动分配

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

### Requirement: 租户状态枚举

系统 SHALL 使用 `SysTenantStatus` 枚举替代 `SysTenantEntity.status` 的裸 String 类型。枚举值包括：`NORMAL`（正常）、`DISABLED`（停用）。到期状态通过 `endTime` 实时计算得出，不作为枚举值。

#### Scenario: 创建租户时默认状态为 NORMAL
- **WHEN** 平台管理员创建新租户
- **THEN** 系统自动设置 `status` 为 `NORMAL`
- **AND** 无需调用方手动传入 status

#### Scenario: Entity 内部使用枚举
- **WHEN** Domain 层操作 `SysTenantEntity.status`
- **THEN** 字段类型为 `SysTenantStatus` 枚举
- **AND** PO 层通过 converter 在枚举和数据库 String 之间转换

### Requirement: 租户编码唯一性

创建和更新租户时，系统 SHALL 校验 `code` 字段在全表范围内（含软删除数据）的唯一性。

#### Scenario: 创建租户时编码重复
- **WHEN** 平台管理员创建租户，`code` 与已存在的租户重复
- **THEN** 系统拒绝创建并返回错误码 `TENANT_CODE_DUPLICATE`（A00402）

#### Scenario: 更新租户时编码与其他租户冲突
- **WHEN** 平台管理员更新租户 `code`，新 `code` 与其他租户重复
- **THEN** 系统拒绝更新并返回错误码 `TENANT_CODE_DUPLICATE`（A00402）

#### Scenario: 更新租户时编码不变
- **WHEN** 平台管理员更新租户，`code` 未变更
- **THEN** 系统不触发唯一性校验，正常更新

### Requirement: 租户生命周期状态管理

系统 SHALL 支持通过显式操作变更租户状态，并校验状态流转合法性。允许的流转：NORMAL → DISABLED、DISABLED → NORMAL。

#### Scenario: 停用正常租户
- **WHEN** 平台管理员对状态为 `NORMAL` 的租户执行停用操作
- **THEN** 系统将租户状态变更为 `DISABLED`

#### Scenario: 启用已停用租户
- **WHEN** 平台管理员对状态为 `DISABLED` 的租户执行启用操作
- **THEN** 系统将租户状态变更为 `NORMAL`

#### Scenario: 重复停用
- **WHEN** 平台管理员对状态已为 `DISABLED` 的租户执行停用操作
- **THEN** 系统拒绝操作并返回错误码 `TENANT_STATUS_INVALID_TRANSITION`（A00403）

#### Scenario: 重复启用
- **WHEN** 平台管理员对状态已为 `NORMAL` 的租户执行启用操作
- **THEN** 系统拒绝操作并返回错误码 `TENANT_STATUS_INVALID_TRANSITION`（A00403）

### Requirement: 租户到期自动判定

系统 SHALL 在查询租户状态时自动判断租户是否到期。当 `endTime` 不为空且小于当前时间时，租户视为已到期，无论 `status` 字段值为何。

#### Scenario: 租户未到期
- **WHEN** 查询租户状态，`endTime` 为空或大于当前时间
- **THEN** 系统返回租户的 `status` 字段值（NORMAL 或 DISABLED）

#### Scenario: 租户已到期
- **WHEN** 查询租户状态，`endTime` 不为空且小于当前时间
- **THEN** 系统返回到期状态，覆盖 `status` 字段值

#### Scenario: 租户无结束时间
- **WHEN** 查询租户状态，`endTime` 为空
- **THEN** 系统返回租户的 `status` 字段值，不做到期判定

### Requirement: 创建租户时可选生成管理员用户

创建租户时，系统 SHALL 支持可选传入管理员账号信息。若传入则自动创建用户并绑定到租户管理员角色。

#### Scenario: 创建租户时指定管理员
- **WHEN** 平台管理员创建租户，传入管理员 `username` 和 `password`
- **THEN** 系统创建租户记录
- **AND** 初始化 `tenant_admin` 角色
- **AND** 创建用户记录（密码 BCrypt 加密存储）
- **AND** 将用户绑定到 `tenant_admin` 角色
- **AND** 用户归属于该租户

#### Scenario: 创建租户时未指定管理员
- **WHEN** 平台管理员创建租户，未传入管理员信息
- **THEN** 系统仅创建租户和 `tenant_admin` 角色，不创建用户

#### Scenario: 管理员用户名在租户内重复
- **WHEN** 平台管理员创建租户，传入的 `username` 在目标租户下已存在
- **THEN** 系统拒绝创建并返回错误码 `USERNAME_EXISTS`（A00202）

### Requirement: 租户状态查询 RPC

系统 SHALL 通过 Dubbo RPC 暴露租户状态查询接口，供 Auth 模块在认证时校验租户可用性。

#### Scenario: 查询正常租户状态
- **WHEN** Auth 模块通过 RPC 查询租户 ID 为 N 的状态
- **AND** 租户存在且状态为 NORMAL 且未到期
- **THEN** 返回状态信息包含 `status=NORMAL`、`expired=false`

#### Scenario: 查询已停用租户状态
- **WHEN** Auth 模块通过 RPC 查询租户状态
- **AND** 租户状态为 DISABLED
- **THEN** 返回状态信息包含 `status=DISABLED`、`expired=false`

#### Scenario: 查询已到期租户状态
- **WHEN** Auth 模块通过 RPC 查询租户状态
- **AND** 租户 endTime 已过
- **THEN** 返回状态信息包含 `status=NORMAL`（或 DISABLED）、`expired=true`

#### Scenario: 查询不存在的租户
- **WHEN** Auth 模块通过 RPC 查询不存在的租户 ID
- **THEN** 返回 null
