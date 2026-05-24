## Why

Tenant 是 Kava 平台的核心概念（一个家庭 = 一个租户），但当前 Tenant 子域的 Application 层 Commands 和 DTOs 全部为空类，导致 create/update/query 时数据静默丢失。同时缺少租户编码唯一性校验、生命周期状态管理（启用/停用/到期）、以及创建租户时自动生成管理员用户等核心业务规则。需要在 domain 层补齐业务逻辑，在 application 层打通数据流转。

## What Changes

- **补齐 Commands/DTOs 字段**：`SysTenantCreateCommand`、`SysTenantUpdateCommand`、`SysTenantAppDetailDTO`、`SysTenantAppListDTO` 当前全部为空类，需要填充与 `SysTenantEntity` 对应的业务字段
- **租户编码唯一性校验**：创建和更新租户时校验 `code` 在全表范围内不可重复
- **租户状态枚举**：将 `status` 字段从裸 `String` 提升为类型安全的枚举 `SysTenantStatus`（NORMAL/DISABLED/EXPIRED）
- **租户生命周期状态管理**：支持启用/停用操作，校验状态流转合法性（如已停用不可再停用）
- **租户到期检测**：基于 `startTime`/`endTime` 自动判定到期状态（需同步 PO 层补齐字段）
- **创建租户时自动生成管理员用户**：在已有 `initTenantAdminRole` 基础上，支持可选指定管理员账号密码，自动创建用户并绑定租户管理员角色
- **暴露租户状态查询 RPC**：新增 Dubbo RPC 方法供 Auth 模块在登录时校验租户状态

## Capabilities

### New Capabilities
（无新增独立能力）

### Modified Capabilities
- `tenant-system`: 补齐编码唯一性、状态枚举与生命周期管理、创建时管理员用户生成、到期自动判定

## Impact

- `kbpd-upms-domain`: `SysTenantEntity`（status 类型变更）、`ISysTenantRepository`（新增 queryByCode）、`ISysTenantService`/`SysTenantService`（业务逻辑）、新增 `SysTenantStatus` 枚举到 types 层
- `kbpd-upms-application`: `SysTenantCreateCommand`、`SysTenantUpdateCommand`、`SysTenantAppDetailDTO`、`SysTenantAppListDTO`、`SysTenantAppConverter`、`SysTenantAppService`（编排管理员用户创建）
- `kbpd-upms-infrastructure`: `SysTenantRepository`（实现 queryByCode）、`SysTenantPO`（补齐 startTime/endTime）、`SysTenantConverter`
- `kbpd-upms-adapter`: `SysTenantController`（新增状态变更端点）、`SysTenantAdapterConverter`、RPC 层新增租户状态查询
- `kbpd-upms-types`: 新增 `SysTenantStatus` 枚举、`UpmsBizErrorCodeEnum` 扩展租户错误码
