## Purpose

定义菜单作用域控制：三值枚举（system / tenant / system_tenant）控制菜单对不同管理员身份的可见性。

## Requirements

### Requirement: 菜单作用域三值枚举
系统 SHALL 将 `SysMenuScope` 枚举扩展为三个值：SYSTEM（平台专属）、TENANT（租户专属）、SYSTEM_TENANT（双方可见）。

#### Scenario: 创建平台专属菜单
- **WHEN** 平台管理员创建菜单并设置 scope 为 SYSTEM
- **THEN** 该菜单仅对平台管理员可见
- **AND** 租户管理员无法看到此菜单

#### Scenario: 创建租户专属菜单
- **WHEN** 租户管理员创建菜单并设置 scope 为 TENANT
- **THEN** 该菜单仅在本租户范围内可见
- **AND** 平台管理员无法看到此菜单（除非平台管理员查看租户配置）

#### Scenario: 创建双方可见菜单
- **WHEN** 平台管理员创建菜单并设置 scope 为 SYSTEM_TENANT
- **THEN** 该菜单在平台管理和租户管理界面均可见
- **AND** 租户是否实际可见取决于租户是否被分配了此菜单

### Requirement: 租户菜单可见性过滤
查询菜单时，系统 SHALL 根据当前用户的身份和租户的菜单分配情况过滤可见菜单。

#### Scenario: 平台管理员查询菜单列表
- **WHEN** 平台管理员查询菜单列表
- **THEN** 返回所有 scope 为 SYSTEM 和 SYSTEM_TENANT 的菜单
- **AND** 不返回 scope 为 TENANT 的租户自有菜单

#### Scenario: 租户管理员查询菜单列表
- **WHEN** 租户管理员查询菜单列表
- **THEN** 返回租户已分配的 SYSTEM_TENANT 菜单 + 本租户自建的 TENANT 菜单
- **AND** 不返回 scope 为 SYSTEM 的平台专属菜单
- **AND** 不返回其他租户的 TENANT 菜单

### Requirement: 角色绑定时校验菜单作用域
角色绑定菜单时，系统 SHALL 校验菜单是否在当前上下文的可见范围内。

#### Scenario: 租户管理员绑定租户范围外的菜单
- **WHEN** 租户管理员创建/更新角色，尝试绑定未被分配的 SYSTEM_TENANT 菜单
- **THEN** 系统拒绝操作并返回权限错误

#### Scenario: 平台管理员绑定任意菜单
- **WHEN** 平台管理员创建/更新角色
- **THEN** 可绑定 SYSTEM 和 SYSTEM_TENANT 范围的菜单
