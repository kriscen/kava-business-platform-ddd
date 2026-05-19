## Why

UPMS 的 Area（行政区划）模块作为通用基础数据子域，当前存在多个功能缺陷：分页查询的 ListDTO 为空导致接口返回无数据、查询参数被忽略未生效、缺少按父节点查子节点的懒加载接口。需要修复这些问题并补充常用查询能力，使其成为可靠的基础服务。

## What Changes

- 修复 `SysAreaAppListDTO` 为空的问题，补齐所有列表展示字段
- 修复 `queryPage()` 未应用查询条件过滤的问题
- 移除 Request/Response 中多余的 `hot` 字段（DB 和 Entity 中均不存在）
- 复用 `kbpd-common` 中的 `Status` 通用枚举替代 `areaStatus` 的字符串硬编码
- `SysAreaType` 枚举在 domain entity 中实际使用，替代 String areaType
- 新增 `GET /children` 接口：按父节点 ID 查询直接子节点，支持级联选择器懒加载场景
- 增强 `GET /tree` 接口：支持按 `areaType` 筛选，用于"只加载省级"或"只加载到市级"等场景

## Capabilities

### New Capabilities
- `area-children-query`: 按父节点查询直接子节点列表，用于级联选择器懒加载模式
- `area-tree-filter`: 增强树查询支持按 areaType 筛选，控制树的层级深度

### Modified Capabilities

（无已有 spec 需要修改）

## Impact

**kbpd-upms 模块内变更，不涉及跨模块：**

- `kbpd-upms-types`: `SysAreaType` 枚举保留不变
- `kbpd-upms-domain`: `SysAreaEntity` 的 areaType 字段类型改为 `SysAreaType` 枚举
- `kbpd-upms-application`: `SysAreaAppListDTO` 补齐字段、新增 children 查询方法
- `kbpd-upms-adapter`: Controller 新增 `/children` 端点，`/tree` 增加 areaType 参数
- `kbpd-upms-infrastructure`: Repository 新增按 pid 查询子节点实现，queryPage 加查询条件
- `kbpd-upms-api`: Request/Response 移除 hot 字段，AdapterListQuery 增加 areaType 参数
