## ADDED Requirements

### Requirement: 树查询支持按 areaType 筛选
系统 SHALL 在树查询接口中支持按 areaType 参数筛选地区类型，控制返回的树层级范围。

#### Scenario: 不传 areaType 返回全量树
- **WHEN** 调用 `GET /api/v1/sys/area/tree`（不传 areaType）
- **THEN** 返回所有 areaStatus=1 的地区数据构建的完整树

#### Scenario: 按 areaType 筛选省级
- **WHEN** 调用 `GET /api/v1/sys/area/tree?areaType=1`
- **THEN** 仅返回 areaType=1（省级）的节点构建的树
- **AND** 树的根节点为 pid 指向默认根 100000 的省份节点

#### Scenario: 按多级类型筛选
- **WHEN** 调用 `GET /api/v1/sys/area/tree?areaType=1,2`
- **THEN** 返回 areaType 为 1（省）或 2（市）的节点构建的树
- **AND** 市级节点的父关系指向省级节点

#### Scenario: 指定 pid 参数控制树的根节点
- **WHEN** 调用 `GET /api/v1/sys/area/tree?pid=110000`
- **THEN** 以 id=110000 为根节点构建子树
