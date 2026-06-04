## ADDED Requirements

### Requirement: 租户购买 App

系统 SHALL 支持为租户订阅 App，通过写入 `sys_tenant_app` 关系记录实现。订阅后租户自动获得该 App 包含的菜单能力。

#### Scenario: 为租户订阅 App
- **WHEN** 平台管理员为租户订阅某个 App
- **THEN** 系统在 sys_tenant_app 中创建记录（tenant_id, app_id, status=ACTIVE）
- **AND** 该租户的菜单查询自动包含该 App 的菜单

#### Scenario: 重复订阅同一 App
- **WHEN** 平台管理员为租户订阅已订阅的 App
- **THEN** 系统拒绝操作并返回已订阅错误

#### Scenario: 订阅不存在的 App
- **WHEN** 平台管理员为租户订阅不存在的 App
- **THEN** 系统拒绝操作并返回 App 不存在错误

#### Scenario: 订阅已停用的 App
- **WHEN** 平台管理员为租户订阅状态为 DISABLED 的 App
- **THEN** 系统拒绝操作并返回 App 不可用错误

### Requirement: 租户退订 App

系统 SHALL 支持租户退订 App，退订后租户失去该 App 的菜单能力。

#### Scenario: 退订 App
- **WHEN** 平台管理员将租户的某个 App 退订
- **THEN** 系统将 sys_tenant_app 中该记录状态变更为 EXPIRED
- **AND** 该租户的菜单查询自动排除该 App 的菜单
- **AND** 已分配给角色的菜单关联可保留，重新订阅后自动恢复

#### Scenario: 退订 kava-base
- **WHEN** 平台管理员尝试退订租户的 kava-base
- **THEN** 系统拒绝操作并返回不可退订错误

### Requirement: 查询租户已购 App 列表

系统 SHALL 支持查询指定租户已订阅的 App 列表。

#### Scenario: 查询租户已购 App
- **WHEN** 管理员查询某租户的已购 App 列表
- **THEN** 返回该租户所有 sys_tenant_app 中 status=ACTIVE 的 App 信息
- **AND** 包含 App 的 code、name、icon、订阅状态、订阅时间

#### Scenario: 租户无已购 App
- **WHEN** 查询租户的已购 App 列表，该租户仅有 kava-base
- **THEN** 返回仅包含 kava-base 的列表

### Requirement: 租户创建时自动关联 kava-base

创建租户时，系统 SHALL 自动在 sys_tenant_app 中创建租户与 kava-base 的关联记录（status=ACTIVE）。

#### Scenario: 创建租户自动关联 kava-base
- **WHEN** 平台管理员创建新租户
- **THEN** 系统自动写入 sys_tenant_app（tenant_id, app_id=kava-base的ID, status=ACTIVE）
- **AND** 租户立即获得 kava-base 包含的所有菜单

### Requirement: 订阅状态生命周期

sys_tenant_app 的 status SHALL 支持 ACTIVE 和 EXPIRED 两个状态。ACTIVE 表示有效订阅，EXPIRED 表示已过期或已退订。

#### Scenario: 有效订阅
- **WHEN** sys_tenant_app 记录 status 为 ACTIVE
- **THEN** 该 App 的菜单参与租户菜单可见性计算

#### Scenario: 过期订阅
- **WHEN** sys_tenant_app 记录 status 为 EXPIRED
- **THEN** 该 App 的菜单不参与租户菜单可见性计算
- **AND** 角色已分配的该 App 菜单关联保留但查询时过滤

### Requirement: 租户 App 菜单聚合查询

系统 SHALL 支持查询指定租户通过已购 App 获得的所有菜单 ID 集合（自动去重）。

#### Scenario: 查询租户菜单集
- **WHEN** 系统需要计算某租户的可见菜单
- **THEN** 查询 sys_tenant_app（status=ACTIVE）→ 关联 sys_app_menu → 汇总所有 menu_id
- **AND** 多个 App 包含同一菜单时自动去重

#### Scenario: 查询租户菜单集包含 kava-base
- **WHEN** 租户仅关联 kava-base
- **THEN** 返回 kava-base 关联的所有菜单 ID
