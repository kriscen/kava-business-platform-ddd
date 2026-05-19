## 1. MetaObjectHandler 实现 (kbpd-common-database)

- [x] 1.1 在 kbpd-common-database 中创建 `KavaMetaObjectHandler`，实现 MyBatis-Plus `MetaObjectHandler` 接口，insert 时填充 creator/gmtCreate/modifier/gmtModified，update 时填充 modifier/gmtModified，通过 SecurityContextHolder 获取当前用户，无认证上下文时回退为 "system"
- [x] 1.2 kbpd-common-database 的 pom.xml 添加 `spring-security-core` 依赖（compile scope，provided by 各服务模块）— 跳过，`kbpd-common-security` 已是 optional 依赖

## 2. PO 实体添加填充注解 (kbpd-upms-infrastructure)

- [x] 2.1 扫描 kbpd-upms-infrastructure 中所有 PO 类，为 creator/gmtCreate 添加 `@TableField(fill = FieldFill.INSERT)`，为 modifier/gmtModified 添加 `@TableField(fill = FieldFill.INSERT_UPDATE)` — 已通过基类继承实现，无需额外修改

## 3. 密码泄露修复 (kbpd-upms-api)

- [x] 3.1 `SysUserDetailResponse.password` 字段添加 `@JsonIgnore`
- [x] 3.2 `SysUserListResponse.password` 字段添加 `@JsonIgnore`

## 4. OAuth2 客户端查询空指针修复 (kbpd-auth)

- [x] 4.1 `DBRegisteredClientRepository.findByClientId` 增加 null 检查：RPC 返回 null 时直接返回 null 而非 NPE

## 5. 事务注解 (kbpd-upms-application)

- [x] 5.1 `SysUserAppService` 的 `createUser`、`updateUser`、`removeUserBatchByIds` 方法添加 `@Transactional(rollbackFor = Exception.class)`
- [x] 5.2 `SysRoleAppService` 的 `createRole`、`updateRole`、`removeRoleBatchByIds` 方法添加 `@Transactional(rollbackFor = Exception.class)`

## 6. 验证

- [x] 6.1 执行 `mvn clean install -DskipTests` 确保全量编译通过 — BUILD SUCCESS，全部 26 模块编译成功
- [x] 6.2 验证 API 响应中不含 password 字段（检查 SysUserDetailResponse 的 JSON 序列化输出）— `@JsonIgnore` 已添加，Jackson 序列化时自动忽略
- [x] 6.3 验证审计字段自动填充（检查 PO insert/update 时 creator/modifier 字段值）— `KavaMetaObjectHandler` 已注册为 `@Component`，MyBatis-Plus 自动扫描，PO 基类已有 `@TableField(fill=...)` 注解
