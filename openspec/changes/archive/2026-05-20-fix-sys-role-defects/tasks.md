## 1. API 层字段清理

- [x] 1.1 清理 SysRoleRequest — 去掉 creator、gmtCreate、modifier、gmtModified、delFlag 字段
- [x] 1.2 清理 SysRoleDetailResponse — 去掉 creator、modifier、delFlag，保留 gmtCreate、gmtModified
- [x] 1.3 清理 SysRoleListResponse — 去掉 delFlag
- [x] 1.4 清理 SysRoleAdapterListQuery — 去掉 creator、modifier、gmtCreate、gmtModified、delFlag 等非业务过滤字段

## 2. 补全 ListDTO

- [x] 2.1 补全 SysRoleAppListDTO — 添加 id、roleName、roleCode、roleDesc、dsType、dsScope、gmtCreate、gmtModified 字段
- [x] 2.2 补全 SysRoleAppConverter 的 convertEntity2DTO 映射 — 确保 id、tenantId 等值对象字段正确映射

## 3. Domain 层：校验逻辑调整

- [x] 3.1 修改 SysRoleService.validateMenuBinding() — 移除 menuIds 空值检查，仅保留 scope 校验
- [x] 3.2 ISysRoleReadRepository 增加 queryByRoleCode 方法 — 按租户 + roleCode 查询角色是否存在
- [x] 3.3 SysRoleService.create() 和 update() 增加 roleCode 唯一性校验 — 查询同租户下是否存在相同 roleCode

## 4. Domain 层：租户隔离

- [x] 4.1 SysRoleListQuery 增加 tenantId 字段
- [x] 4.2 ISysRoleReadRepository.queryPage 支持按 tenantId 过滤（tenantId 为 null 时不追加过滤，供平台管理员使用）

## 5. Infrastructure 层

- [x] 5.1 SysRoleReadRepository 实现 queryByRoleCode — 按 tenantId + roleCode 查询
- [x] 5.2 SysRoleReadRepository.queryPage 增加 tenantId 过滤条件
- [x] 5.3 SysRoleReadRepository 和 SysRoleWriteRepository 改为构造器注入
- [x] 5.4 SysRoleConverter 补充 tenantId 映射（SysRolePO → SysRoleEntity）— 已有映射，无需修改

## 6. Application 层

- [x] 6.1 SysRoleAppService.queryRolePage() 从 Security Context 获取 tenantId 并传入 query — 改为在 Controller 层获取 tenantId 传入 query，保持 application 层不依赖 security 模块

## 7. Adapter 层

- [x] 7.1 SysRoleController 改为构造器注入

## 8. 验证

- [x] 8.1 编译通过：`mvn clean install -pl kbpd-upms -am -DskipTests`
- [x] 8.2 验证 roleCode 唯一性校验逻辑：创建同名角色应被拒绝
- [x] 8.3 验证空 menuIds 创建角色成功
