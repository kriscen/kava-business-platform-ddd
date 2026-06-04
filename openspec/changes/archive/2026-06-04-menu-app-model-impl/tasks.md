## 1. 数据库表结构与迁移

- [x] 1.1 新增 `sys_app` 表：id, code, name, icon, description, status, del_flag, creator, gmt_create, modifier, gmt_modified
- [x] 1.2 新增 `sys_app_menu` 表：id, app_id, menu_id（多对多关联）
- [x] 1.3 新增 `sys_tenant_app` 表：id, tenant_id, app_id, status(ACTIVE/EXPIRED), gmt_create, gmt_modified
- [x] 1.4 `sys_menu` 表：scope 列重命名为 level，VARCHAR(32)
- [x] 1.5 `sys_tenant` 表：移除 menu_id 列
- [x] 1.6 编写数据迁移 SQL：scope=SYSTEM → level=PLATFORM，scope=TENANT → level=TENANT，scope=SYSTEM_TENANT 拆分为两条记录（一条 PLATFORM，一条 TENANT，相同组件路径）
- [x] 1.7 编写 kava-base 初始化数据 SQL：sys_app 插入 kava-base 记录，sys_app_menu 关联现有 PLATFORM 和基础 TENANT 菜单
- [x] 1.8 编写 sys_tenant_menu → sys_tenant_app 迁移 SQL：所有租户默认关联 kava-base

## 2. Types 层 — 枚举与值对象

- [x] 2.1 新建 `SysMenuLevel` 枚举（PLATFORM/TENANT），删除 `SysMenuScope` 枚举
- [x] 2.2 新建 `SysAppStatus` 枚举（ACTIVE/DISABLED）
- [x] 2.3 新建 `SysTenantAppStatus` 枚举（ACTIVE/EXPIRED）
- [x] 2.4 新建 `SysAppId` 值对象（Long）
- [x] 2.5 新建 `SysAppListQuery` 值对象
- [x] 2.6 更新 `SysMenuListQuery`：scope → level
- [x] 2.7 新建 `SysTenantAppId` 值对象（Long）
- [x] 2.8 新建 App 相关错误码（APP_CODE_DUPLICATE、APP_NOT_DELETABLE 等）

## 3. Domain 层 — 实体与聚合

- [x] 3.1 `SysMenuEntity`：scope 字段改为 level（SysMenuLevel 枚举类型）
- [x] 3.2 新建 `SysAppEntity`：id(SysAppId), code, name, icon, description, status(SysAppStatus)
- [x] 3.3 新建 `SysTenantAppEntity`：id(SysTenantAppId), tenantId(SysTenantId), appId(SysAppId), status(SysTenantAppStatus)
- [x] 3.4 `SysTenantEntity`：移除 menuId 字段
- [x] 3.5 `SysOauthClientEntity`：新增可选 appId 字段（SysAppId）

## 4. Domain 层 — Repository 接口

- [x] 4.1 `ISysMenuRepository`：查询方法中 scope → level 适配
- [x] 4.2 新建 `ISysAppRepository`（继承 IBaseSimpleRepository）
- [x] 4.3 新建 `ISysTenantAppRepository`：包含 queryByTenantId、queryByTenantIdAndAppId、existsActiveSubscription 等方法
- [x] 4.4 新建 `ISysAppMenuRepository`：包含 queryByAppId、queryMenuIdsByAppIds（批量查询菜单ID）等方法

## 5. Domain 层 — DomainService

- [x] 5.1 `SysMenuService`：scope → level 适配
- [x] 5.2 新建 `ISysAppService` / `SysAppService`：CRUD + code 唯一性校验 + 删除保护（kava-base 不可删）
- [x] 5.3 新建 `ISysTenantAppService` / `SysTenantAppService`：订阅/退订 + 去重校验 + kava-base 不可退订 + 查询租户菜单聚合集
- [x] 5.4 `SysRoleService.validateMenuScope()` → `validateMenuLevel()`：平台角色只能选 PLATFORM，租户角色只能选已购 App 范围内的 TENANT 菜单
- [x] 5.5 `SysRoleService.initTenantAdminRole()`：改为自动获取 kava-base 的 TENANT 级菜单，不再接收 menuId 参数
- [x] 5.6 `SysTenantService.create()` 流程调整：不再传入 menuId，自动关联 kava-base 到 sys_tenant_app

## 6. Infrastructure 层 — PO / Mapper / Converter

- [x] 6.1 `SysMenuPO`：scope → level 字段重命名
- [x] 6.2 新建 `SysAppPO` + `SysAppMapper`
- [x] 6.3 新建 `SysAppMenuPO` + `SysAppMenuMapper`
- [x] 6.4 新建 `SysTenantAppPO` + `SysTenantAppMapper`
- [x] 6.5 `SysTenantPO`：移除 menuId 字段
- [x] 6.6 `SysOauthClientPO`：新增可选 appId 字段
- [x] 6.7 `SysMenuConverter`：适配 level → SysMenuLevel 枚举转换
- [x] 6.8 新建 `SysAppConverter`（MapStruct）
- [x] 6.9 新建 `SysTenantAppConverter`（MapStruct）
- [x] 6.10 新建 `SysAppMenuConverter`（MapStruct）
- [x] 6.11 `SysTenantConverter`：移除 menuId 映射
- [x] 6.12 `SysOauthClientConverter`：新增 appId 映射

## 7. Infrastructure 层 — Repository 实现

- [x] 7.1 `SysMenuRepository` 适配：查询条件中 scope → level
- [x] 7.2 `SysTenantRepository` 适配：移除 menuId 相关逻辑
- [x] 7.3 新建 `SysAppRepository` 实现
- [x] 7.4 新建 `SysTenantAppRepository` 实现：queryMenuIdsByTenantId（聚合查询租户已购 App 的菜单集）
- [x] 7.5 新建 `SysAppMenuRepository` 实现
- [x] 7.6 `SysRoleReadRepository` 适配：角色菜单范围查询需结合 App 购买关系
- [x] 7.7 `SysUserReadRepository` 适配：queryMenuIdsByUserId 需结合 App 购买关系过滤

## 8. Application 层 — Command / DTO

- [x] 8.1 `SysMenuCreateCommand` / `SysMenuUpdateCommand`：scope → level
- [x] 8.2 `SysTenantCreateCommand` / `SysTenantUpdateCommand`：移除 menuId 字段
- [x] 8.3 `SysMenuAppDetailDTO` / `SysMenuAppListDTO`：scope → level
- [x] 8.4 `SysTenantAppDetailDTO` / `SysTenantAppListDTO`：移除 menuId
- [x] 8.5 新建 App 相关 Command：SysAppCreateCommand, SysAppUpdateCommand
- [x] 8.6 新建 App 相关 DTO：SysAppDetailDTO, SysAppListDTO
- [x] 8.7 新建 TenantApp 相关 Command：TenantAppSubscribeCommand
- [x] 8.8 新建 TenantApp 相关 DTO：TenantAppListDTO, TenantAppDetailDTO

## 9. Application 层 — AppService

- [x] 9.1 `SysMenuAppService.queryMenuTree()` 核心重写：平台管理员 → 所有 PLATFORM 菜单；租户管理员 → 已购 App 的 TENANT 菜单并集 → 角色权限过滤
- [x] 9.2 `SysRoleAppService`：角色分配菜单时增加 App 购买范围校验
- [x] 9.3 `SysTenantAppService.createTenant()`：移除 menuId 参数，自动关联 kava-base
- [x] 9.4 新建 `SysAppAppService`：App CRUD + App-菜单关联管理
- [x] 9.5 新建 `SysTenantAppAppService`：订阅/退订/查询已购 App

## 10. Adapter 层 — Controller

- [x] 10.1 `SysMenuController`：请求/响应中 scope → level
- [x] 10.2 `SysTenantController`：创建/更新请求移除 menuId
- [x] 10.3 新建 `SysAppController`：App CRUD 端点（平台管理员专用）
- [x] 10.4 新建 `SysTenantAppController`：租户订阅/退订/查询已购 App 端点

## 11. API 层 — Request / Response

- [x] 11.1 `SysMenuRequest` / `SysMenuListResponse` / `SysMenuDetailResponse`：scope → level
- [x] 11.2 `SysTenantRequest` / `SysTenantListResponse` / `SysTenantDetailResponse`：移除 menuId
- [x] 11.3 新建 `SysAppRequest` / `SysAppListResponse` / `SysAppDetailResponse` / `SysAppDropdownResponse`
- [x] 11.4 新建 `SysTenantAppRequest` / `SysTenantAppListResponse` / `SysTenantAppDetailResponse`

## 12. 验证

- [x] 12.1 编译验证：`mvn clean install -pl kbpd-upms -am -DskipTests`
- [ ] 12.2 单元测试：SysAppService CRUD + 校验逻辑（code 唯一性、kava-base 不可删除）
- [ ] 12.3 单元测试：SysTenantAppService 订阅/退订 + 菜单聚合查询
- [ ] 12.4 单元测试：SysRoleService.validateMenuLevel() 平台/租户菜单范围校验
- [ ] 12.5 单元测试：SysMenuAppService.queryMenuTree() 平台管理员/租户管理员可见性
- [ ] 12.6 集成验证：启动 UPMS 服务，通过 API 测试完整流程（创建 App → 订阅给租户 → 租户角色分配菜单 → 获取菜单树）
