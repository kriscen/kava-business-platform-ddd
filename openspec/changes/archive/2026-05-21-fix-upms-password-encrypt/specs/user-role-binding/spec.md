## ADDED Requirements

### Requirement: 用户密码加密存储
创建或更新用户时，系统 SHALL 对密码字段执行 BCrypt 加密后再持久化，明文密码不得写入数据库。

#### Scenario: 创建用户时密码自动加密
- **WHEN** 管理员提交用户创建请求，password 为明文 `"123456"`
- **THEN** 系统对密码执行 BCrypt 加密后写入 sys_user 表
- **AND** 数据库中存储的是 BCrypt hash（以 `{bcrypt}` 开头），不是明文

#### Scenario: 更新用户时非空密码加密
- **WHEN** 管理员更新用户，password 字段为非空新密码
- **THEN** 系统对新密码执行 BCrypt 加密后更新 sys_user 表

#### Scenario: 更新用户时密码为空跳过加密
- **WHEN** 管理员更新用户，password 字段为 null 或空字符串
- **THEN** 系统保留 sys_user 表中已有的密码值不变
