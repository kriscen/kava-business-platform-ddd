# Capability: user-uniqueness-validation

## Purpose

用户子域用户名唯一性校验：在创建和更新用户时，确保同一租户范围内用户名不重复。

## Requirements

### Requirement: 创建用户时校验用户名唯一性

创建用户时，SysUserService SHALL 在同一租户范围内校验用户名唯一性。若用户名已被占用，MUST 抛出 `USER_USERNAME_DUPLICATE` 业务异常。

#### Scenario: 用户名未被占用，创建成功
- **WHEN** 调用 `SysUserService.create(entity)` 且该租户下不存在相同 username 的用户
- **THEN** 正常创建用户并返回 `SysUserId`

#### Scenario: 用户名已被占用，创建失败
- **WHEN** 调用 `SysUserService.create(entity)` 且该租户下已存在相同 username 的用户
- **THEN** 抛出 `UpmsBizException(USER_USERNAME_DUPLICATE)`

### Requirement: 更新用户时校验用户名唯一性

更新用户时，若 username 发生变更，SysUserService SHALL 在同一租户范围内校验新用户名的唯一性。

#### Scenario: 更新时用户名不变
- **WHEN** 调用 `SysUserService.update(entity)` 且 username 与现有记录相同
- **THEN** 不触发唯一性校验，正常更新

#### Scenario: 更新为新用户名且未被占用
- **WHEN** 调用 `SysUserService.update(entity)` 且 username 变更为新值，该租户下无其他用户使用此用户名
- **THEN** 正常更新

#### Scenario: 更新为新用户名但已被占用
- **WHEN** 调用 `SysUserService.update(entity)` 且 username 变更为新值，但该租户下已有其他用户使用此用户名
- **THEN** 抛出 `UpmsBizException(USER_USERNAME_DUPLICATE)`
