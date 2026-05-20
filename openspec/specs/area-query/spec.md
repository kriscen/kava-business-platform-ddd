# Capability: area-query

## Purpose

定义地区（SysArea）查询能力：支持按父节点查询直接子节点（扁平列表）和按类型过滤的树查询，覆盖地区选择器逐级加载和层级筛选场景。

## Requirements

### Requirement: 按父节点查询直接子节点

系统 SHALL 提供按父节点 ID 查询其直接子节点列表的接口，返回扁平列表，不递归构建子树。

#### Scenario: 正常查询子节点
- **WHEN** 调用 `GET /api/v1/sys/area/children?pid=100000`
- **THEN** 返回 pid=100000 下的所有直接子节点列表
- **AND** 列表中每项包含 id、pid、name、letter、adcode、areaType 字段
- **AND** 仅返回 areaStatus=1（生效）的记录

#### Scenario: 传入不存在的 pid
- **WHEN** 调用 `GET /api/v1/sys/area/children?pid=999999`
- **THEN** 返回空列表

#### Scenario: 未传 pid 参数
- **WHEN** 调用 `GET /api/v1/sys/area/children`（不传 pid）
- **THEN** 使用默认根节点 pid=100000 查询

#### Scenario: 结果按 areaSort 降序排列
- **WHEN** 查询返回多个子节点
- **THEN** 结果按 areaSort 降序排列

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
