## 1. MetaObjectHandler 实现（kbpd-common-database）

- [x] 1.1 在 `kbpd-common-database` 中创建 `MybatisMetaObjectHandler` 类，实现 `MetaObjectHandler` 接口，`insertFill` 方法填充 creator/modifier（从 SecurityUtils.getUserContext() 获取 userId，无上下文时回退 "system"）、gmtCreate/gmtModified（当前时间）、delFlag（"0"）；`updateFill` 方法填充 modifier/gmtModified
- [x] 1.2 确认 `MybatisMetaObjectHandler` 位于 `kbpd-common-database` 的 Spring 组件扫描路径下，能被自动注册

## 2. SysUserId 值对象补齐（kbpd-upms-domain）

- [x] 2.1 在 `kbpd-upms-domain/model/valobj/` 下创建 `SysUserId` 值对象，包含 `Long value` 字段、`getValue()` 方法、`of(Long value)` 静态工厂方法
- [x] 2.2 修改 `SysUserEntity`（聚合根），将 ID 类型从 `Long` 改为 `SysUserId`，更新构造器和 getter
- [x] 2.3 修改 `ISysUserReadRepository` 和 `ISysUserWriteRepository` 接口，将 ID 参数类型从 `Long` 改为 `SysUserId`
- [x] 2.4 修改 `SysUserService`（Domain Service）中引用 User ID 的方法签名
- [x] 2.5 修改 infrastructure 层 `SysUserConverter`，添加 `SysUserId` ↔ `Long` 的转换逻辑
- [x] 2.6 修改 infrastructure 层 `SysUserReadRepository` 和 `SysUserWriteRepository` 实现，适配 `SysUserId` 参数

## 3. SysTenantId 值对象补齐（kbpd-upms-domain）

- [x] 3.1 在 `kbpd-upms-domain/model/valobj/` 下创建 `SysTenantId` 值对象，包含 `Long value` 字段、`getValue()` 方法、`of(Long value)` 静态工厂方法
- [x] 3.2 修改 `SysTenantEntity`，将 ID 类型从 `Long` 改为 `SysTenantId`，更新构造器和 getter
- [x] 3.3 修改 `ISysTenantRepository` 接口，将 ID 参数类型从 `Long` 改为 `SysTenantId`
- [x] 3.4 修改 `SysTenantService`（Domain Service）中引用 Tenant ID 的方法签名
- [x] 3.5 修改 infrastructure 层 `SysTenantConverter`，添加 `SysTenantId` ↔ `Long` 的转换逻辑
- [x] 3.6 修改 infrastructure 层 `SysTenantRepository` 实现，适配 `SysTenantId` 参数

## 4. loginByPwd 实现（kbpd-upms-adapter）

- [x] 4.1 删除 `loginByPwd` 死方法（实际认证链路已通过 findByUsername + CustomerAuthenticationProvider 走通，该方法从未被调用）

## 5. FileGroup 路径修正（kbpd-upms-adapter）

- [x] 5.1 修改 `SysFileGroupController` 的 `@RequestMapping` 注解，将 `FileGroup-group` 改为 `file-group`

## 6. 验证

- [x] 6.1 执行 `mvn clean install -DskipTests`，确认编译通过无错误
- [x] 6.2 检查所有修改文件的 import 语句，确认无循环依赖和错误引用
