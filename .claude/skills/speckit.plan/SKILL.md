---
name: speckit.plan
description: 使用计划模板执行实施计划工作流，生成设计工件包括 research.md、data-model.md、contracts/ 和 quickstart.md。当用户要求创建实施计划、生成设计文档或规划技术方案时触发。
---

## 执行前检查

检查 `.specify/extensions.yml` 的 `hooks.before_plan` 钩子。

## 概要

### 1. 设置

运行 `.specify/scripts/bash/setup-plan.sh --json` 获取 FEATURE_SPEC、IMPL_PLAN、SPECS_DIR、BRANCH。

### 2. 加载上下文

读取 FEATURE_SPEC 和 `.specify/memory/constitution.md`，加载 IMPL_PLAN 模板。

### 3. 执行计划工作流

按 IMPL_PLAN 模板结构：
- 填充技术上下文（将未知标记为"需要澄清"）
- 从章程填充章程检查章节
- 评估门控
- 阶段0：生成 research.md
- 阶段1：生成 data-model.md、contracts/、quickstart.md
- 更新代理上下文

### 4. 停止并报告

命令在阶段2计划后结束。报告分支、IMPL_PLAN 路径和生成的工件。

### 5. 检查扩展钩子

完成后检查 `hooks.after_plan` 钩子。

## 阶段

### 阶段0：概要与研究

从技术上下文提取未知项，在 `research.md` 中整合发现。

### 阶段1：设计与合约

从功能规格提取实体 → `data-model.md`
定义接口合约 → `/contracts/`
运行代理上下文更新脚本

## 关键规则

- 使用绝对路径
- 门控失败或未解决澄清时ERROR