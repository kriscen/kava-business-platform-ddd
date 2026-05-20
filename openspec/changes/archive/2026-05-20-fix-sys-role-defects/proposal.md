## Why

SysRole 角色管理功能存在多项缺陷：列表 DTO 为空导致接口无返回数据、请求对象混入审计字段、菜单绑定校验与 Spec 矛盾、缺少租户隔离和 roleCode 唯一性校验、依赖注入不符合 DDD 规范。这些问题影响功能正确性和数据安全性，需在正式使用前修复。

## What Changes

- **补全 SysRoleAppListDTO**：添加列表展示所需的字段（id、roleName、roleCode、roleDesc、dsType、dsScope 等），使分页查询接口返回有效数据
- **清理 API 层审计字段**：SysRoleRequest 去掉 creator/gmtCreate/modifier/gmtModified/delFlag；SysRoleDetailResponse 去掉 creator/modifier/delFlag；SysRoleListResponse 去掉 delFlag
- **修复菜单绑定校验**：允许 menuIds 为空时创建角色（与 Spec 一致），仅保留菜单 scope 校验
- **增加租户隔离**：角色分页查询从 Security Context 获取 tenantId，按租户过滤结果
- **增加 roleCode 唯一性校验**：同一租户内 roleCode 不可重复，创建和更新时均需校验
- **规范依赖注入**：Controller 和 Infrastructure 层从 @Resource 字段注入改为构造器注入

## Capabilities

### New Capabilities

（无新增能力）

### Modified Capabilities

- `role-menu-binding`: 修改菜单绑定的空值校验规则 — 允许 menuIds 为空时创建角色
- `permission-system`: 增加租户隔离过滤和 roleCode 唯一性校验要求

## Impact

- `kbpd-upms/kbpd-upms-api` — SysRoleRequest、SysRoleDetailResponse、SysRoleListResponse、SysRoleAdapterListQuery
- `kbpd-upms/kbpd-upms-application` — SysRoleAppListDTO、SysRoleAppConverter、SysRoleAppService
- `kbpd-upms/kbpd-upms-domain` — SysRoleListQuery、SysRoleService（校验逻辑调整）、ISysRoleReadRepository（增加 tenantId 过滤）
- `kbpd-upms/kbpd-upms-infrastructure` — SysRoleReadRepository（租户过滤实现）、SysRoleWriteRepository、SysRoleConverter
- `kbpd-upms/kbpd-upms-adapter` — SysRoleController（构造器注入）
