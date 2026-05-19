## 1. 修复现有 Bug

- [x] 1.1 补齐 `SysAreaAppListDTO` 字段：id、pid、name、letter、adcode、location、areaSort、areaStatus、areaType、cityCode
- [x] 1.2 修复 `SysAreaRepository.queryPage()` 添加查询条件过滤（pid、name、areaType、areaStatus）
- [x] 1.3 移除 `SysAreaRequest`、`SysAreaDetailResponse`、`SysAreaListResponse`、`SysAreaAdapterListQuery` 中多余的 `hot` 字段

## 2. 枚举类型落地

- [x] 2.1 `SysAreaEntity` 的 `areaType` 字段类型从 String 改为 `SysAreaType` 枚举
- [x] 2.2 `SysAreaEntity` 的 `areaStatus` 字段类型从 String 改为 `Status` 枚举（kbpd-common）
- [x] 2.3 更新 `SysAreaConverter`（Infrastructure）添加 String ↔ Enum 的 MapStruct 映射
- [x] 2.4 更新 `SysAreaAppConverter`（Application）和 `SysAreaAdapterConverter`（Adapter）适配枚举变化
- [x] 2.5 同步更新 `SysAreaCreateCommand`、`SysAreaUpdateCommand`、`SysAreaAppDetailDTO`、`SysAreaAppListDTO` 的字段类型

## 3. 新增 children 查询接口

- [x] 3.1 `ISysAreaRepository` 新增 `selectChildren(Long pid)` 方法声明
- [x] 3.2 `SysAreaRepository` 实现按 pid 查询直接子节点（areaStatus=1，按 areaSort 降序）
- [x] 3.3 `ISysAreaService` 新增 `selectChildren(SysAreaId pid)` 方法
- [x] 3.4 `SysAreaService` 实现子节点查询逻辑
- [x] 3.5 `ISysAreaAppService` 和 `SysAreaAppService` 新增 `queryAreaChildren` 方法
- [x] 3.6 `SysAreaController` 新增 `GET /children` 端点，pid 默认值 100000

## 4. 增强 tree 接口支持 areaType 筛选

- [x] 4.1 `SysAreaListQuery` 新增 `areaTypes` 字段（支持多值）
- [x] 4.2 `SysAreaAdapterListQuery` 新增 `areaType` 字段
- [x] 4.3 更新 Adapter Converter 映射 areaType → areaTypes
- [x] 4.4 `SysAreaRepository.selectTreeList()` 添加 areaType 过滤条件
- [x] 4.5 `SysAreaService.selectAreaTree()` 处理 areaType 筛选时的根节点计算逻辑

## 5. 验证

- [x] 5.1 编译验证：`mvn clean install -pl kbpd-upms -am -DskipTests`
- [x] 5.2 验证各接口功能：GET /page 过滤生效、GET /tree 支持 areaType、GET /children 返回正确子节点
