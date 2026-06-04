## ADDED Requirements

### Requirement: App CRUD 管理

系统 SHALL 提供平台管理员管理 App 的完整 CRUD 接口。SysAppEntity MUST 使用 `SysAppId` 值对象作为 ID 类型。

#### Scenario: 创建 App
- **WHEN** 平台管理员提交 App 创建请求，包含 code、name、icon、description
- **THEN** 系统校验 code 唯一性
- **AND** 创建 App 记录，状态默认为 ACTIVE
- **AND** 返回新创建的 App ID

#### Scenario: 创建 App 时 code 重复
- **WHEN** 平台管理员创建 App，code 与已存在的 App 重复
- **THEN** 系统拒绝创建并返回唯一性冲突错误

#### Scenario: 查询 App 列表
- **WHEN** 平台管理员查询 App 列表（支持分页）
- **THEN** 返回所有 App 的列表信息，包含 code、name、icon、status

#### Scenario: 更新 App
- **WHEN** 平台管理员更新 App 的 name、icon、description 等字段
- **THEN** 若 code 变更则校验唯一性
- **AND** 系统更新 App 记录

#### Scenario: 删除 App
- **WHEN** 平台管理员删除 App
- **THEN** 系统软删除 App 记录（设置 del_flag）
- **AND** 若有租户仍在使用该 App（sys_tenant_app 存在 ACTIVE 记录），系统拒绝删除并返回错误

#### Scenario: 删除 kava-base 系统应用
- **WHEN** 平台管理员尝试删除 code 为 `kava-base` 的系统应用
- **THEN** 系统拒绝删除并返回不可删除错误

### Requirement: App-菜单关联管理

系统 SHALL 支持为 App 配置包含的菜单集合，通过 sys_app_menu 多对多关系表管理。

#### Scenario: 为 App 关联菜单
- **WHEN** 平台管理员为 App 配置菜单 ID 列表
- **THEN** 系统全量替换 sys_app_menu 中该 App 的关联记录（先删后插）
- **AND** 一个菜单可同时属于多个 App

#### Scenario: 为 App 关联不存在的菜单
- **WHEN** 平台管理员为 App 配置菜单，其中包含不存在的 menu_id
- **THEN** 系统拒绝操作并返回菜单不存在的错误

#### Scenario: 清空 App 的菜单关联
- **WHEN** 平台管理员将 App 的菜单列表设为空
- **THEN** 系统删除 sys_app_menu 中该 App 的所有记录

### Requirement: kava-base 系统应用预设

系统 SHALL 通过 SQL 初始化脚本预设 code 为 `kava-base` 的系统应用。kava-base 不可删除、不可停用。

#### Scenario: 系统启动后存在 kava-base
- **WHEN** 数据库执行初始化脚本后
- **THEN** sys_app 表中存在 code='kava-base' 的记录
- **AND** kava-base 关联了所有 PLATFORM 级和基础 TENANT 级菜单

#### Scenario: 停用 kava-base
- **WHEN** 平台管理员尝试停用 kava-base
- **THEN** 系统拒绝操作并返回不可停用错误

### Requirement: App 状态枚举

系统 SHALL 使用 `SysAppStatus` 枚举管理 App 状态，枚举值包括：`ACTIVE`（可用）、`DISABLED`（停用）。

#### Scenario: 创建 App 时默认状态为 ACTIVE
- **WHEN** 平台管理员创建新 App
- **THEN** 系统自动设置 status 为 ACTIVE

#### Scenario: 停用 App
- **WHEN** 平台管理员停用 App
- **THEN** 系统将 App 状态变更为 DISABLED
- **AND** 已购买该 App 的租户的菜单查询自动排除该 App 的菜单

#### Scenario: 启用已停用 App
- **WHEN** 平台管理员启用已停用的 App
- **THEN** 系统将 App 状态变更为 ACTIVE
- **AND** 已购买该 App 的租户的菜单查询自动恢复该 App 的菜单

### Requirement: App code 唯一性

创建和更新 App 时，系统 SHALL 校验 `code` 字段在全表范围内（含软删除数据）的唯一性。

#### Scenario: 创建 App 时 code 重复
- **WHEN** 平台管理员创建 App，code 与已存在的 App 重复
- **THEN** 系统拒绝创建并返回唯一性冲突错误

#### Scenario: 更新 App 时 code 与其他 App 冲突
- **WHEN** 平台管理员更新 App code，新 code 与其他 App 重复
- **THEN** 系统拒绝更新并返回唯一性冲突错误

### Requirement: App Dropdown 查询

系统 SHALL 提供非分页的 App 下拉列表接口，用于前端表单选择。

#### Scenario: 查询 App 下拉列表
- **WHEN** 平台管理员请求 App 下拉列表
- **THEN** 返回所有 ACTIVE 状态的 App（id、code、name），按 name 排序
- **AND** 无数据时返回空列表
