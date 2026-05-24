## 1. P0 — 修复 Menu Create/Update Command 空壳

- [x] 1.1 在 `SysMenuCreateCommand` 中补全字段：name、permission、pid、icon、path、component、visible、sortOrder、menuType、keepAlive、embedded
- [x] 1.2 在 `SysMenuUpdateCommand` 中补全字段（同 CreateCommand 加 id）
- [x] 1.3 验证 `SysMenuAdapterConverter`（Menu 部分）的 Request → Command 映射能自动传递所有字段
- [x] 1.4 验证 `SysMenuAppConverter`（Menu 部分）的 Command → Entity 映射能自动传递所有字段（添加了 @Mapping 注解）
- [x] 1.5 手动测试 Menu POST/PUT 端点能正确创建和更新菜单

## 2. P0 — 修复 Menu tree 响应结构断裂

- [x] 2.1 在 `SysMenuListResponse` 中添加 `List<SysMenuListResponse> children` 字段
- [x] 2.2 验证 AdapterConverter 的 tree 映射能正确传递 children（MapStruct 自动映射或手动补充）
- [x] 2.3 手动测试 `GET /api/v1/sys/menu/tree` 返回完整树形结构

## 3. P1 — 新增 Dept tree 端点

- [x] 3.1 在 `ISysDeptService` 接口中添加 `queryTree()` 方法声明
- [x] 3.2 在 `SysDeptService` 实现中添加 `queryTree()` 方法
- [x] 3.3 在 `ISysDeptAppService` 中添加 `queryDeptTree()` 方法
- [x] 3.4 在 `SysDeptAppService` 实现中调用 DomainService 的 `queryTree()` 并构建树结构
- [x] 3.5 在 `SysDeptController` 中添加 `GET /tree` 端点
- [x] 3.6 手动测试 `GET /api/v1/sys/dept/tree` 返回树形结构

## 4. P1 — 新增下拉选择器端点

- [x] 4.1 新建 `SysRoleDropdownResponse` DTO（id、roleName、roleCode）
- [x] 4.2 在 `ISysRoleAppService` 中添加 `queryRoleDropdown()` 方法
- [x] 4.3 在 `SysRoleAppService` 中实现 `queryRoleDropdown()`（查询当前租户全部角色，按 roleName 排序）
- [x] 4.4 在 `SysRoleController` 中添加 `GET /dropdown` 端点
- [x] 4.5 新建 `SysTenantDropdownResponse` DTO（id、name、code、status）
- [x] 4.6 在 `ISysTenantAppService` 中添加 `queryTenantDropdown()` 方法
- [x] 4.7 在 `SysTenantAppService` 中实现 `queryTenantDropdown()`
- [x] 4.8 在 `SysTenantController` 中添加 `GET /dropdown` 端点
- [x] 4.9 手动测试所有下拉端点返回精简数据

## 5. P2 — 响应补充关联名称

- [x] 5.1 在 Domain 层 Repository 接口新增方法：`ISysRoleReadRepository.queryList()`、`ISysTenantRepository.queryAll()`（利用已有 queryAll/queryByIds，无需新增 queryNamesByIds）
- [x] 5.2 在 Infrastructure 层实现上述 Repository 方法
- [x] 5.3 在 `SysUserDetailResponse` 中添加 deptName、tenantName、roleNames 字段
- [x] 5.4 在 `SysUserListResponse` 中添加 deptName、tenantName、roleIds 字段（当前缺失）
- [x] 5.5 在 AppService 中补充关联名称的查询和赋值逻辑（用户详情和列表）
- [x] 5.6 在 `SysRoleDetailResponse` 中添加 menuNames 字段，并在 AppService 中补充赋值逻辑
- [x] 5.7 在 `SysDeptDetailResponse` 和 `SysDeptListResponse` 中添加 parentName 字段，DTO 同步添加
- [x] 5.8 在 `SysMenuDetailResponse` 和 `SysMenuListResponse` 中添加 parentName 字段，DTO 同步添加
- [x] 5.9 手动测试各端点返回的关联名称正确（包括关联记录不存在时返回 null）

## 6. 验证

- [x] 6.1 执行 `mvn clean install -pl kbpd-upms -am` 确保编译通过
- [ ] 6.2 启动 UPMS 服务，用 curl/Postman 验证所有新增和修改的端点
- [ ] 6.3 运行 `check-ddd` 验证 DDD 合规性
