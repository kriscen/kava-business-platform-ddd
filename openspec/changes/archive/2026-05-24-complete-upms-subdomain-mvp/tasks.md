## 1. OauthClient 子域改造

- [x] 1.1 在 `UpmsBizErrorCodeEnum` 中新增 OauthClient 相关错误码：`CLIENT_ID_DUPLICATE`、`CLIENT_SECRET_REQUIRED`、`CLIENT_TOKEN_VALIDITY_INVALID`、`CLIENT_GRANT_TYPES_REQUIRED`
- [x] 1.2 在 `SysOauthClientService` 中增加校验逻辑：clientId 唯一性（create 时查重）、clientSecret 非空、accessTokenValidity/refreshTokenValidity > 0、authorizedGrantTypes 非空。update 时若 clientId 变更也需校验唯一性
- [x] 1.3 修改 `SysOauthClientAppService`：移除 `ISysOauthClientRepository` 注入，改为注入 `ISysOauthClientService`，所有方法通过 DomainService 调用。写操作方法加 `@Transactional(rollbackFor = Exception.class)`

## 2. User 子域唯一性校验

- [x] 2.1 在 `SysUserService.create()` 中增加用户名唯一性校验：调用 `readRepository.queryByUsername(tenantId, username)` 检查是否已存在，存在则抛出 `USER_USERNAME_DUPLICATE`
- [x] 2.2 在 `SysUserService.update()` 中增加用户名唯一性校验：先查询当前用户获取原 username，若新 username 与原值不同则检查唯一性

## 3. Area 子域链路修复

- [x] 3.1 在 `ISysAreaService` 接口中增加 CRUD 方法签名：`create`、`update`、`removeBatchByIds`、`queryPage`、`queryById`
- [x] 3.2 在 `SysAreaService` 实现中增加对应方法，委托至 `ISysAreaRepository`
- [x] 3.3 修改 `SysAreaAppService`：写操作（create/update/delete）改为通过 `sysAreaService` 调用，移除 `ISysAreaRepository` 注入

## 4. Menu 子域链路修复

- [x] 4.1 修改 `SysMenuAppService`：注入 `ISysMenuService`，CRUD 操作（create/update/delete/queryPage/queryById）改为通过 DomainService 调用，移除 `ISysMenuRepository` 直接注入。`queryMenuTree` 中的树构建和 scope 过滤逻辑保留在 AppService

## 5. Tenant 子域链路修复

- [x] 5.1 在 `ISysTenantService` 接口中增加 `removeBatchByIds` 方法签名
- [x] 5.2 在 `SysTenantService` 实现中增加对应方法，委托至 Repository
- [x] 5.3 修改 `SysTenantAppService.removeTenantBatchByIds` 改为调用 `sysTenantService.removeBatchByIds`

## 6. Dept 子域事务补齐

- [x] 6.1 为 `SysDeptAppService` 的写操作方法（`createDept`、`updateDept`、`removeDeptBatchByIds`）添加 `@Transactional(rollbackFor = Exception.class)` 注解

## 7. 验证

- [x] 7.1 执行 `mvn clean install -pl kbpd-upms -am -DskipTests` 确保编译通过
- [x] 7.2 运行 `mvn test -pl kbpd-upms/kbpd-upms-bootstrap` 确保现有测试通过
- [x] 7.3 使用 `/check-ddd` 验证所有修改的子域符合 DDD 分层规范
