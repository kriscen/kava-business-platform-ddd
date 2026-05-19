## ADDED Requirements

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
