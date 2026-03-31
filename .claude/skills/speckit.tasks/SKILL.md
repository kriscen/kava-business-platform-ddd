---
name: speckit.tasks
description: 基于可用的设计工件为功能生成可执行、按依赖排序的 tasks.md 任务清单。当用户要求创建任务列表、生成实施任务或规划开发步骤时触发。
---

## 执行前检查

检查 `.specify/extensions.yml` 的 `hooks.before_tasks` 钩子。

## 概要

### 1. 设置

运行 `.specify/scripts/bash/check-prerequisites.sh --json` 获取 FEATURE_DIR 和 AVAILABLE_DOCS。

### 2. 加载设计文档

**必需**：plan.md、spec.md
**可选**：data-model.md、contracts/、research.md、quickstart.md

### 3. 执行任务生成工作流

加载文档并提取：
- plan.md：技术栈、库、项目结构
- spec.md：带优先级的用户故事
- data-model.md：实体映射到用户故事
- contracts/：接口合约映射

### 4. 生成 tasks.md

使用 `.specify/templates/tasks-template.md` 作为结构：

- 阶段1：设置任务
- 阶段2：基础任务
- 阶段3+：每个用户故事一个阶段
- 最终阶段：完善与横切关注点

### 5. 报告

输出生成的 tasks.md 路径、任务计数、并行机会、MVP范围建议。

### 6. 检查扩展钩子

完成后检查 `hooks.after_tasks` 钩子。

## 任务生成规则

**关键**：任务必须按用户故事组织以支持独立实施和测试。

**测试可选**：仅在功能规格中显式请求或用户请求TDD时生成测试任务。

### 检查清单格式（必需）

每个任务必须遵循格式：`- [ ] [TaskID] [P?] [Story?] 带文件路径的描述`

**示例**：
- ✅ `- [ ] T001 按实施计划创建项目结构`
- ✅ `- [ ] T012 [P] [US1] 在 src/models/user.py 创建用户模型`

### 阶段结构

- **阶段1**：设置
- **阶段2**：基础
- **阶段3+**：用户故事（P1、P2、P3...）
- **最终阶段**：完善