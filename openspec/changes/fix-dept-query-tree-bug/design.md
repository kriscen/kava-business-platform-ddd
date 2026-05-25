## Context

`SysDeptService.queryTree()` 当前实现为纯委托 `repository.queryAll()`，返回平铺列表。对照 `dept-domain-service` spec，该方法应"将所有部门按 pid 组装为树形结构返回"。同一模块中 `SysAreaService.selectAreaTree()` 已有正确的树构建实现，使用 `TreeBuilder` 工具类。

## Goals / Non-Goals

**Goals:**
- 修复 `SysDeptService.queryTree()` 使其正确构建树形结构
- 复用 `TreeBuilder`，与 Area 子域保持一致

**Non-Goals:**
- 不修改 `TreeBuilder` 本身
- 不修改 AppService 层或 Controller 层
- 不涉及租户隔离逻辑变更

## Decisions

**在 DomainService 层构建树** — Because spec 要求 DomainService 的 `queryTree()` 返回树形结构，且 `SysAreaService` 已在 DomainService 层构建树（而非 AppService 层），保持一致。

## Risks / Trade-offs

无明显风险。修改范围仅限一个方法，且参照现有 Area 实现。
