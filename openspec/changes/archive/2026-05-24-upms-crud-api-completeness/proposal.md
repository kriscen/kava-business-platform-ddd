## Why

UPMS 14 个子域的 CRUD 接口骨架已全部搭好，但经审查发现多处缺陷导致前端无法正常集成：Menu 的创建/更新接口是空壳（Command 无字段）、Menu tree 返回时树结构被丢弃、Dept 缺少树端点、所有表单所需的下拉选择器端点缺失、响应对象只返回关联 ID 不返回名称。需要补齐这些缺口，前端管理面板才能实际联调使用。

## What Changes

- **P0 — 修复 Menu Create/Update Command 空壳**：`SysMenuCreateCommand` 和 `SysMenuUpdateCommand` 没有任何字段，导致前端 POST/PUT 菜单无效，需要补全字段映射链路
- **P0 — 修复 Menu tree 响应结构断裂**：`SysMenuAppListDTO` 有 `children` 字段，但 `SysMenuListResponse` 缺少该字段，adapter converter 映射时树结构被丢弃
- **P1 — 新增 Dept tree 端点**：部门是层级结构（有 `pid`），但只有分页列表，缺少 `GET /dept/tree`，需复用已有的树构建逻辑
- **P1 — 新增下拉选择器端点**：用户/角色等表单需要关联实体的非分页列表（角色下拉、部门树、租户下拉），当前只有分页端点不适合前端下拉选择器
- **P2 — 响应补充关联名称**：User 列表/详情缺 `deptName`、`roleNames`、`tenantName`；Role 详情缺 `menuNames`；Dept/Menu 缺 `parentName`；前端无法直接展示关联信息

## Capabilities

### New Capabilities

- `dropdown-selector-api`: 前端表单下拉选择器端点（Role/Dept/Tenant 非分页列表）
- `response-enrichment`: 响应对象补充关联实体名称（跨表关联查询或批量解析）

### Modified Capabilities

- `menu-domain-service`: 修复 Menu Create/Update Command 字段缺失（P0 空壳问题）
- `dept-domain-service`: 新增 Dept tree 端点，复用树构建逻辑
- `dept-tree-validation`: Dept tree 端点依赖此能力的树结构构建
- `area-query`: Area 已有树端点可作为 Dept tree 的参考实现
- `role-menu-binding`: Role 详情响应需补充 menuNames
- `user-role-binding`: User 列表/详情响应需补充 roleNames、deptName、tenantName

## Impact

主要涉及 `kbpd-upms` 模块（单模块 change）：

- `kbpd-upms-api`：Response 对象新增字段、新增下拉查询 DTO、Menu Command 补字段
- `kbpd-upms-application`：新增下拉查询方法、Command 补字段、Converter 补映射
- `kbpd-upms-adapter`：新增下拉端点、Dept tree 端点、Menu tree Response 补 children、adapter converter 补映射
- `kbpd-upms-infrastructure`：repository 新增批量名称查询方法（用于关联名称解析）
- `kbpd-upms-domain`：repository 接口新增查询方法定义
