## 1. Bug 修复

- [x] 1.1 修改 `SysDeptService.queryTree()`，参照 `SysAreaService.selectAreaTree()` 使用 `TreeBuilder` 将平铺部门列表按 pid 组装为树形结构
- [x] 1.2 确认 `SysDeptEntity` 具备 `children` 字段或可被 TreeBuilder 识别的树节点接口

## 2. 验证

- [x] 2.1 编译通过：`mvn clean install -pl kbpd-upms -am -DskipTests`
- [x] 2.2 对照 `dept-domain-service` spec 中的 queryTree 场景，验证修复后行为符合 spec
