## 1. Types 层 — 新增 Dept 错误码

- [x] 1.1 在 `UpmsBizErrorCodeEnum` 中新增 4 个 dept 错误码：`DEPT_PID_SELF_REFERENCE`(A00501)、`DEPT_PID_CIRCULAR`(A00502)、`DEPT_HAS_CHILDREN`(A00503)、`DEPT_REFERENCED_BY_USER`(A00504)

## 2. Domain 层 — Repository 接口扩展

- [x] 2.1 在 `ISysDeptRepository` 中新增方法：`List<SysDeptEntity> queryAll()`、`List<SysDeptEntity> queryByPid(SysDeptId pid)`、`boolean existsUserReference(SysDeptId deptId)`
- [x] 2.2 在 `ISysDeptService` 接口中新增 `List<SysDeptEntity> queryAll()` 方法

## 3. Domain 层 — DomainService 树形验证逻辑

- [x] 3.1 在 `SysDeptService` 中实现 `validatePid` 方法：pid 为 null 跳过、自引用检查、全表加载构建 childToParent 映射并遍历检测循环引用
- [x] 3.2 在 `SysDeptService` 中实现 `validateBeforeDelete` 方法：遍历每个 id 检查子节点（queryByPid）和用户引用（existsUserReference）
- [x] 3.3 修改 `SysDeptService.create()` 在委托 Repository 前调用 `validatePid`
- [x] 3.4 修改 `SysDeptService.update()` 在委托 Repository 前调用 `validatePid`
- [x] 3.5 修改 `SysDeptService.removeBatchByIds()` 在委托 Repository 前调用 `validateBeforeDelete`
- [x] 3.6 实现 `SysDeptService.queryAll()` 委托至 Repository

## 4. Infrastructure 层 — Repository 实现

- [x] 4.1 在 `SysDeptRepository` 中实现 `queryAll()`：使用 MyBatis-Plus `list()` 确保租户拦截器生效，转为 domain entity 列表
- [x] 4.2 在 `SysDeptRepository` 中实现 `queryByPid()`：使用 lambda query `eq(SysDeptPO::getPid, pid.getId())`
- [x] 4.3 在 `SysDeptRepository` 中实现 `existsUserReference()`：注入 `SysUserMapper`，执行 `selectCount` 检查 `sys_user.dept_id`

## 5. Application 层 — 填充 Command/DTO 并修正调用链

- [x] 5.1 填充 `SysDeptCreateCommand`：添加字段 name、sortOrder、pid
- [x] 5.2 填充 `SysDeptUpdateCommand`：添加字段 id、name、sortOrder、pid
- [x] 5.3 填充 `SysDeptAppDetailDTO`：添加字段 id、name、sortOrder、pid
- [x] 5.4 填充 `SysDeptAppListDTO`：添加字段 id、name、sortOrder、pid；补充 `@Data` 注解
- [x] 5.5 更新 `SysDeptAppConverter`：确保 MapStruct 映射覆盖新增字段
- [x] 5.6 修改 `SysDeptAppService`：将 `ISysDeptRepository` 注入替换为 `ISysDeptService`

## 6. 验证

- [x] 6.1 执行 `mvn clean install -pl kbpd-upms -am -DskipTests` 确认编译通过
- [x] 6.2 检查 MapStruct 生成的 Converter 实现类，确认字段映射完整
