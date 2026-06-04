## MODIFIED Requirements

### Requirement: 租户 CRUD 管理

系统 SHALL 提供平台管理员管理租户的完整 CRUD 接口（创建、查询、更新、删除），并增加状态变更端点。创建和更新接口 SHALL 使用 `SysTenantStatus` 枚举，创建时默认状态为 `NORMAL`。创建租户时系统 SHALL 自动关联 kava-base 系统应用，不再需要手动传入菜单 ID。

Tenant 实体 MUST 使用 `SysTenantId` 值对象作为 ID 类型，与其他子域保持一致的 ID 值对象模式。

#### Scenario: 创建租户
- **WHEN** 平台管理员提交租户创建请求，包含 name、code、tenantDomain、startTime、endTime 等（不包含 menuId）
- **THEN** 系统校验 code 唯一性
- **AND** 创建租户记录，状态默认为 NORMAL
- **AND** 自动在 sys_tenant_app 中创建租户与 kava-base 的关联（status=ACTIVE）
- **AND** 自动初始化 tenant_admin 角色，关联 kava-base 包含的所有 TENANT 级菜单
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

### Requirement: 新租户自动初始化必备角色

创建租户时，系统 SHALL 自动创建一个"租户管理员"角色，关联 kava-base 系统应用包含的所有 TENANT 级菜单。

#### Scenario: 创建租户时自动创建管理员角色
- **WHEN** 平台管理员创建租户
- **THEN** 系统自动创建角色 code 为 `tenant_admin` 的角色
- **AND** 该角色关联 kava-base 包含的所有 TENANT 级菜单
- **AND** 该角色的 dsType 为 ALL（租户全数据可见）

## REMOVED Requirements

### Requirement: 租户菜单分配
**Reason**: 菜单分配从手动指定 menuId 改为声明式的 App 购买关系。不再通过 sys_tenant.menu_id 分配菜单，改通过 sys_tenant_app 购买 App 自动获得菜单。
**Migration**: sys_tenant.menu_id 字段废弃，迁移到 sys_tenant_app 关系表。所有租户默认关联 kava-base。
