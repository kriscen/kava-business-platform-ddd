## 1. Domain 层构造器注入改造

- [x] 1.1 kbpd-upms-domain 的 pom.xml 依赖从 `spring-boot-starter` 改为 `spring-context`
- [x] 1.2 将 4 个有逻辑的 DomainService 实现（SysUserServiceImpl、SysRoleServiceImpl、SysTenantServiceImpl、SysAreaServiceImpl）从 @Resource 字段注入改为构造器注入
- [x] 1.3 将 10 个透传 DomainService 实现移除 Repository 注入，方法体改为 `throw new UnsupportedOperationException("暂未实现")`

## 2. 跨聚合写逻辑迁移

- [x] 2.1 在 ISysRoleService 接口新增 `initTenantAdminRole(SysTenantId tenantId, String menuIdStr)` 方法
- [x] 2.2 在 SysRoleServiceImpl 中实现 initTenantAdminRole，将 SysTenantServiceImpl.initTenantAdminRole 的角色创建逻辑搬入
- [x] 2.3 从 SysTenantServiceImpl 中移除 ISysRoleWriteRepository 注入和 initTenantAdminRole 私有方法，create 方法只负责租户创建

## 3. Application 层改造

- [x] 3.1 将 14 个 AppService 实现从 @Resource 字段注入改为构造器注入
- [x] 3.2 从 10 个未使用 DomainService 的 AppService 中移除对应的 DomainService 注入（SysAuditLogAppService、SysDeptAppService、SysFileAppService、SysFileGroupAppService、SysI18nAppService、SysLogAppService、SysMenuAppService、SysOauthClientAppService、SysPublicParamAppService、SysRouteConfAppService）
- [x] 3.3 在 SysTenantAppService.createTenant 中增加跨聚合编排：先调 sysTenantService.create，再调 sysRoleService.initTenantAdminRole
- [x] 3.4 编译验证：`mvn clean compile -pl kbpd-upms -am`

## 4. ListQuery 过滤字段补充

- [x] 4.1 为 5 个优先 ListQuery 添加过滤字段：SysUserListQuery（username, phone, lockFlag, deptId）、SysRoleListQuery（roleName, roleCode）、SysDeptListQuery（deptName）、SysMenuListQuery（menuName, type, scope）、SysTenantListQuery（tenantName）
- [x] 4.2 更新 AppConverter 中 Command → Entity 和 Entity → AppDTO 的映射，确保 ListQuery 新字段能正确传递
- [x] 4.3 更新 AdapterConverter 中 AdapterListQuery → ListQuery 的映射，补全过滤字段转换
- [x] 4.4 更新 Infrastructure 层的 Repository 实现，在 queryPage 方法中使用 ListQuery 的过滤字段构建 MyBatis-Plus 查询条件

## 5. Adapter 违规清理

- [x] 5.1 移除 SysFileAdapterConverter 中对 domain.model.entity.SysFileEntity 的未使用 import
- [x] 5.2 移除 SysI18nAdapterConverter 中对 domain.model.entity.SysI18nEntity 的未使用 import

## 6. 验证

- [x] 6.1 全模块编译验证：`mvn clean install -pl kbpd-upms -am -DskipTests`
- [x] 6.2 运行现有测试：`mvn test -pl kbpd-upms/kbpd-upms-bootstrap`
- [x] 6.3 代码审查：确认无 domain.entity / domain.service / domain.repository 的 import 出现在 adapter 包中
