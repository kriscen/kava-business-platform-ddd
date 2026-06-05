## Context

kbpd-upms 模块有 15 个 REST Controller，路径风格整体一致（`/api/{ver}/sys/{resource}/`），但存在几处具体问题：

1. `SysAuditLogController` 和 `SysLogController` 暴露了完整的 CRUD 接口，但日志数据应由系统写入，前端只需查询
2. `SysRoleController` 和 `SysUserController` 的 PUT 端点缺少 `{id}` 路径参数，与其他资源不一致
3. `SysOauthClientController` 的路径 `/sys/oauth-client-details/` 带有数据库表名后缀 "details"，不符合 API 命名惯例
4. `SysTenantAppController` 使用动词式路径（/subscribe、/unsubscribe），不符合 RESTful 资源设计
5. 所有 Controller 的 class-level `@RequestMapping` 以 `/` 结尾，虽不影响功能但不够干净

## Goals / Non-Goals

**Goals:**
- 统一 API 路径命名风格，提升一致性和可读性
- 移除不应暴露的写接口（日志类资源）
- 将动词式路径改为 RESTful 资源嵌套
- 清理路径格式细节

**Non-Goals:**
- 不修改 application/domain 层代码，仅调整 adapter 层 Controller
- 不修改 Dubbo RPC 接口路径
- 不修改 kbpd-auth 的 OAuth2 端点（`/oauth2/*` 是标准协议路径）
- 不统一 `@CrossOrigin` 到全局配置（留作后续优化）

## Decisions

### 决策 1：日志类 Controller 只保留查询端点

**选择**：从 `SysAuditLogController` 和 `SysLogController` 中移除 POST/PUT/DELETE 方法，仅保留 GET /page 和 GET /{id}

**替代方案**：保留写接口但加权限注解限制

**Because**：日志是系统行为的记录，由 domain 事件或拦截器自动写入。暴露写接口会带来数据篡改风险，且没有合理的使用场景。直接移除比加权限更简洁安全。

### 决策 2：RESTful 资源嵌套替代动词式路径

**选择**：将 `SysTenantAppController` 的路径改为：
- `POST /sys/tenant/{tenantId}/apps` — 订阅（创建关联）
- `DELETE /sys/tenant/{tenantId}/apps/{appId}` — 取消订阅
- `GET /sys/tenant/{tenantId}/apps` — 查询已购 App

**替代方案**：保留动词式路径但调整为 `/sys/tenant-app/tenants/{tenantId}/apps`

**Because**：租户与 App 的关系本质上是"租户拥有的 App 集合"，用资源嵌套（`/tenant/{id}/apps`）比动词（/subscribe、/unsubscribe）更符合 REST 语义，前端理解成本更低。DELETE 语义明确对应"取消订阅"，无需额外解释。

### 决策 3：OAuth Client 路径简化

**选择**：`/sys/oauth-client-details/` → `/sys/oauth-client/`

**Because**："details" 是数据库表名 `sys_oauth_client_details` 的遗留，API 面向的是 OAuth Client 这个业务概念，不需要暴露底层表结构。

### 决策 4：PUT 路径统一加 {id}

**选择**：`PUT /sys/role/` → `PUT /sys/role/{id}`，`PUT /sys/user/` → `PUT /sys/user/{id}`

**Because**：REST 惯例中更新操作通过路径标识资源（`/resource/{id}`），当前项目中其他 13 个资源都已遵循此规范，仅 role 和 user 遗漏。

## Risks / Trade-offs

- **[Breaking Change]** 所有路径变更都是不兼容的，前端必须同步修改。→ 配合前端发版节奏，本次变更与前端对齐后合并
- **[应用层影响]** PUT 加 {id} 后，Controller 需要从路径提取 id 并传给 AppService。如果当前 AppService 的 update 方法从 request body 中取 id，需要确认是否需要调整 → 仅影响 adapter 层参数绑定，不影响 domain
- **[TenantApp Controller 重构]** 路径嵌套后需要新增 `@PathVariable` 参数绑定，Controller 方法签名会变化 → 纯 adapter 层改动，AppService 接口不变
