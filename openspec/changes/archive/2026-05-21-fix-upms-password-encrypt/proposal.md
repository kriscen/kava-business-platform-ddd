## Why

用户创建/更新时密码以明文入库，auth 模块登录时 BCrypt 校验永远不匹配，属于安全红线缺陷。同时 `business-rules.md` 中记录的 SysLogPO 表名问题已修复但文档未同步。

## What Changes

- `SysUserAppService.createUser()` 中对密码字段执行 BCrypt 加密后再传入 domain 层
- `SysUserAppService.updateUser()` 中对非空密码字段执行加密，空密码跳过（保留原值）
- `kbpd-upms-application/pom.xml` 新增 `spring-security-crypto` 依赖（仅 crypto，非 starter）
- `docs/06-modules/kbpd-upms/business-rules.md` 更新 SysLogPO 表名条目为"已修复"，密码加密条目为"已实现"

## Capabilities

### New Capabilities

_无新增能力_

### Modified Capabilities

- `user-role-binding`: 用户创建/更新时密码必须加密存储，明文不得持久化

## Impact

- **代码文件**:
  - `kbpd-upms/kbpd-upms-application/src/main/java/.../SysUserAppService.java` — 注入 PasswordEncoder，create/update 时加密
  - `kbpd-upms/kbpd-upms-application/pom.xml` — 新增 spring-security-crypto 依赖
- **文档文件**:
  - `docs/06-modules/kbpd-upms/business-rules.md` — 更新待实现条目状态
- **依赖**: `spring-security-crypto`（Spring Security 轻量子模块，仅密码编码接口）
- **不涉及**: domain 层、infrastructure 层、controller 层均无改动
