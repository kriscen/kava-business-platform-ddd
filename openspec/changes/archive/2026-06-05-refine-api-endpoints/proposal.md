## Why

kbpd-upms 模块的 API 路径存在几处命名不一致和语义不合理的问题：审计日志/操作日志暴露了不应开放的写接口，部分资源的 RESTful 风格不统一（如 PUT 缺少路径 ID、动词代替名词），需要统一规范以提升 API 可读性和前端对接体验。

## What Changes

- **移除审计日志和操作日志的写接口**：`SysAuditLogController` 和 `SysLogController` 只保留 GET 查询端点，删除 POST/PUT/DELETE（日志由系统写入，不应由前端操作）
- **修复 SysRole PUT 路径**：`PUT /sys/role/` → `PUT /sys/role/{id}`，保持与其他资源一致
- **修复 SysUser PUT 路径**：`PUT /sys/user/` → `PUT /sys/user/{id}`，保持与其他资源一致
- **简化 OAuth Client 路径**：`/sys/oauth-client-details/` → `/sys/oauth-client/`（去掉数据库表名后缀 "details"）
- **重构 TenantApp 为 RESTful 资源嵌套**：
  - `POST /sys/tenant-app/subscribe` → `POST /sys/tenant/{tenantId}/apps`
  - `POST /sys/tenant-app/unsubscribe` → `DELETE /sys/tenant/{tenantId}/apps/{appId}`
  - `GET /sys/tenant-app/tenant/{tenantId}` → `GET /sys/tenant/{tenantId}/apps`
- **统一 SysApp 的 dropdown 路径**：`/sys/app/dropdown` 保持现有规范（已有 spec 覆盖）
- **清理路径末尾斜杠**：去掉 class-level `@RequestMapping` 中多余的尾部 `/`

## Capabilities

### New Capabilities

（无新增能力）

### Modified Capabilities

- `tenant-app-subscription`：API 路径从动词式（/subscribe、/unsubscribe）改为 RESTful 资源嵌套式（/tenant/{tenantId}/apps），功能语义不变，仅路径变更

## Impact

- **受影响模块**：kbpd-upms（仅 adapter 层）
- **受影响文件**：
  - `kbpd-upms-adapter/src/main/java/com/kava/kbpd/upms/adapter/http/SysAuditLogController.java` — 移除写端点
  - `kbpd-upms-adapter/src/main/java/com/kava/kbpd/upms/adapter/http/SysLogController.java` — 移除写端点
  - `kbpd-upms-adapter/src/main/java/com/kava/kbpd/upms/adapter/http/SysRoleController.java` — PUT 加 {id}
  - `kbpd-upms-adapter/src/main/java/com/kava/kbpd/upms/adapter/http/SysUserController.java` — PUT 加 {id}
  - `kbpd-upms-adapter/src/main/java/com/kava/kbpd/upms/adapter/http/SysOauthClientController.java` — 路径简化
  - `kbpd-upms-adapter/src/main/java/com/kava/kbpd/upms/adapter/http/SysTenantAppController.java` — RESTful 重构
  - 所有 15 个 Controller — 清理尾部斜杠
- **前端影响**：前端需要同步更新对接路径，属 **BREAKING** 变更
- **specs 影响**：`tenant-app-subscription` spec 需要更新路径定义
