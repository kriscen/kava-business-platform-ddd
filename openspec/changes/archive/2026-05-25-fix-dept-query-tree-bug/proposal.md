## Why

`SysDeptService.queryTree()` 当前直接返回 `repository.queryAll()` 的平铺列表，没有构建树形结构。这与 `dept-domain-service` spec 中定义的"按 pid 组装为树形结构"行为不符，导致前端 `GET /api/v1/sys/dept/tree` 端点返回的不是树而是平铺数据。

## What Changes

- 修复 `SysDeptService.queryTree()`，使其使用 `TreeBuilder` 将部门列表按 pid 组装为树形结构，与 `SysAreaService.selectAreaTree()` 保持一致

## Capabilities

### New Capabilities

（无）

### Modified Capabilities

（无 — 现有实现不符合已有 spec，属于 bug 修复，不涉及需求变更）

## Impact

- `kbpd-upms/kbpd-upms-domain/src/main/java/com/kava/kbpd/upms/domain/service/impl/SysDeptService.java` — queryTree 方法实现
