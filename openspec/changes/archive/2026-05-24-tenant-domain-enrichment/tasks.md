## 1. Types 层基础准备

- [x] 1.1 在 `kbpd-upms-types` 新增 `SysTenantStatus` 枚举（NORMAL、DISABLED），包含 code 值和描述
- [x] 1.2 在 `UpmsBizErrorCodeEnum` 中补充租户相关错误码：TENANT_CODE_DUPLICATE（A00402）、TENANT_STATUS_INVALID_TRANSITION（A00403）

## 2. Domain 层模型与业务规则

- [x] 2.1 修改 `SysTenantEntity`：`status` 字段类型从 `String` 改为 `SysTenantStatus`，新增 `isExpired()` 方法判断到期
- [x] 2.1 在 `ISysTenantRepository` 新增 `queryByCode(String code)` 方法
- [x] 2.3 实现 `SysTenantService` 业务逻辑：`create` 方法加入编码唯一性校验（调用 `queryByCode`），`update` 方法加入编码唯一性校验（排除自身）
- [x] 2.4 在 `ISysTenantService` 新增 `enable(SysTenantId id)` 和 `disable(SysTenantId id)` 方法，实现状态流转校验
- [x] 2.5 在 `ISysTenantService` 新增 `queryEffectiveStatus(SysTenantId id)` 方法，结合 status 枚举和到期判定返回最终状态

## 3. Infrastructure 层持久化

- [x] 3.1 修改 `SysTenantPO`：`status` 字段类型改为 `String`（与数据库一致），新增 `startTime`（LocalDateTime）和 `endTime`（LocalDateTime）字段
- [x] 3.2 编写 SQL 迁移脚本：`sys_tenant` 表新增 `start_time` 和 `end_time` 列（DATETIME, 允许 NULL）
- [x] 3.3 修改 `SysTenantConverter`：处理 `SysTenantStatus` ↔ String 转换、新增 startTime/endTime 映射
- [x] 3.4 实现 `SysTenantRepository.queryByCode()`：按 code 查询，排除已软删除记录

## 4. Application 层数据管道修复

- [x] 4.1 填充 `SysTenantCreateCommand` 字段：name、code、tenantDomain、websiteName、logo、footer、miniQr、background、startTime、endTime、menuId、adminUsername、adminPassword（管理员信息可选）
- [x] 4.2 填充 `SysTenantUpdateCommand` 字段：id、name、code、tenantDomain、websiteName、logo、footer、miniQr、background、startTime、endTime、menuId
- [x] 4.3 填充 `SysTenantAppDetailDTO` 字段：id、name、code、tenantDomain、websiteName、logo、footer、miniQr、background、status、startTime、endTime、menuId、gmtCreate、gmtModified
- [x] 4.4 填充 `SysTenantAppListDTO` 字段：id、name、code、status、startTime、endTime、gmtCreate
- [x] 4.5 修复 `SysTenantAppConverter` MapStruct 映射，确保所有字段正确转换
- [x] 4.6 修改 `SysTenantAppService.createTenant()`：传入 `ISysUserWriteRepository` 和 `PasswordEncoder`，编排管理员用户创建 + 角色绑定

## 5. Adapter 层接口

- [x] 5.1 在 `SysTenantController` 新增 `PUT /{id}/enable` 和 `PUT /{id}/disable` 端点
- [x] 5.2 修复 `SysTenantAdapterConverter` 确保请求/响应字段映射正确
- [x] 5.3 新增 Dubbo RPC 服务 `RemoteTenantService`，实现 `checkTenantStatus(Long tenantId)` 方法，返回 `TenantStatusDTO`（status + expired 标志）

## 6. 验证

- [x] 6.1 编译验证：`mvn clean compile -pl kbpd-upms -am` 确保无编译错误
- [x] 6.2 检查 DDD 合规：运行 `/check-ddd` 验证分层依赖、构造器注入等规则
- [x] 6.3 逐场景验证：租户编码唯一性、状态流转、到期判定、管理员用户创建、RPC 接口
