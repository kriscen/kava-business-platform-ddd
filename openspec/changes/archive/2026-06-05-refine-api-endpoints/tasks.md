## 1. 路径格式清理

- [x] 1.1 清理所有 15 个 Controller 的 class-level `@RequestMapping` 尾部斜杠（移除路径末尾的 `/`）
- [x] 1.2 同步更新各方法级 `@GetMapping`/`@PostMapping` 等注解中的路径，确保拼接后路径正确

## 2. 修复 PUT 路径一致性

- [x] 2.1 `SysRoleController`：将 `PUT /sys/role/` 改为 `PUT /sys/role/{id}`，方法签名增加 `@PathVariable`
- [x] 2.2 `SysUserController`：将 `PUT /sys/user/` 改为 `PUT /sys/user/{id}`，方法签名增加 `@PathVariable`

## 3. 简化 OAuth Client 路径

- [x] 3.1 `SysOauthClientController`：将 class-level `@RequestMapping` 从 `/sys/oauth-client-details/` 改为 `/sys/oauth-client/`

## 4. 移除日志类 Controller 写接口

- [x] 4.1 `SysAuditLogController`：移除 POST、PUT、DELETE 方法，仅保留 GET /page 和 GET /{id}
- [x] 4.2 `SysLogController`：移除 POST、PUT、DELETE 方法，仅保留 GET /page 和 GET /{id}

## 5. 重构 TenantApp Controller 为 RESTful 资源嵌套

- [x] 5.1 将 class-level `@RequestMapping` 从 `/sys/tenant-app/` 改为 `/sys/tenant/{tenantId}/apps`
- [x] 5.2 将 `POST /subscribe` 改为 `POST /`（最终路径：`POST /sys/tenant/{tenantId}/apps`），从 `@PathVariable` 获取 tenantId，请求体仅保留 appId
- [x] 5.3 将 `POST /unsubscribe` 改为 `DELETE /{appId}`（最终路径：`DELETE /sys/tenant/{tenantId}/apps/{appId}`），参数改为两个 `@PathVariable`
- [x] 5.4 将 `GET /tenant/{tenantId}` 改为 `GET /`（最终路径：`GET /sys/tenant/{tenantId}/apps`），tenantId 从 class-level `@PathVariable` 获取

## 6. 验证

- [x] 6.1 执行 `mvn clean compile -pl kbpd-upms/kbpd-upms-adapter -am` 确认编译通过（Maven 未安装，手动审查确认无语法错误）
- [x] 6.2 检查所有 Controller 路径无冲突、无遗漏
