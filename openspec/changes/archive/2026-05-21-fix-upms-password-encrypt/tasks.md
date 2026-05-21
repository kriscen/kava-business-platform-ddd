## 1. 依赖配置

- [x] 1.1 `kbpd-upms-application/pom.xml` 新增 `spring-security-crypto` 依赖

## 2. 密码加密实现

- [x] 2.1 `SysUserAppService` 注入 `PasswordEncoder`（使用 `DelegatingPasswordEncoder`）
- [x] 2.2 `createUser()` 中对 `command.password` 执行加密后再转换为 entity
- [x] 2.3 `updateUser()` 中判断 `command.password` 非空时加密，空值跳过

## 3. 文档更新

- [x] 3.1 `business-rules.md` 将 SysLogPO 表名条目标记为"已修复"
- [x] 3.2 `business-rules.md` 将密码加密存储条目标记为"已实现"

## 4. 验证

- [x] 4.1 `mvn clean install -pl kbpd-upms/kbpd-upms-application -am` 构建通过
- [x] 4.2 确认 SysUserAppService 中 create/update 流程密码已加密
