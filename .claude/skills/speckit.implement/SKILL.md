---
name: speckit.implement
description: 通过处理和执行 tasks.md 中定义的所有任务来执行实施计划。当用户要求开始实施、执行任务或开发功能时触发。
---

## 执行前检查

检查 `.specify/extensions.yml` 的 `hooks.before_implement` 钩子。

## 概要

### 1. 初始化

运行 `.specify/scripts/bash/check-prerequisites.sh --json --require-tasks --include-tasks` 获取 FEATURE_DIR 和 AVAILABLE_DOCS。

### 2. 检查检查清单状态

扫描 `FEATURE_DIR/checklists/` 目录，计算每个检查清单的完成状态。如有未完成，询问用户是否继续。

### 3. 加载实施上下文

**必需**：读取 tasks.md、plan.md
**可选**：data-model.md、contracts/、research.md、quickstart.md

### 4. 项目设置验证

基于实际项目设置创建/验证忽略文件（.gitignore、.dockerignore等）。

### 5. 解析 tasks.md 结构

提取任务阶段、依赖、详情和执行流程。

### 6. 按任务计划执行实施

- 逐阶段执行
- 尊重依赖：顺序任务按序运行，并行任务 [P] 可同时运行
- 遵循TDD方法
- 每完成一个任务标记 [X]

### 7. 实施执行规则

**执行顺序**：设置 → 测试 → 核心开发 → 集成工作 → 完善与验证

### 8. 完成验证

验证所有必需任务完成、功能匹配规格、测试通过。

### 9. 检查扩展钩子

完成后检查 `hooks.after_implement` 钩子。