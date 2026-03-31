---
name: speckit.analyze
description: 对 spec.md、plan.md 和 tasks.md 进行跨制品一致性和质量分析，识别不一致、重复、歧义和未充分说明的项目。在任务生成后且实施前运行此 skill，确保需求质量。当用户要求分析需求一致性、检查规格完整性或验证章程对齐时触发。
---

在实施之前识别 `spec.md`、`plan.md` 和 `tasks.md` 这三个核心制品之间的不一致、重复、歧义和未充分说明的项目。此 skill **必须**仅在 `/speckit.tasks` 成功生成完整的 `tasks.md` 后运行。

## 操作约束

**严格只读**：**不要**修改任何文件。输出结构化的分析报告。提供可选的修复计划（用户必须在手动调用任何后续编辑命令之前明确批准）。

**章程权威**：项目章程（`.specify/memory/constitution.md`）在此分析范围内是**不可协商的**。章程冲突自动为**关键**级别。

## 执行步骤

### 1. 初始化分析上下文

从仓库根目录运行 `.specify/scripts/bash/check-prerequisites.sh --json --require-tasks --include-tasks` 并解析 JSON 获取 FEATURE_DIR 和 AVAILABLE_DOCS。推导绝对路径：

- SPEC = FEATURE_DIR/spec.md
- PLAN = FEATURE_DIR/plan.md
- TASKS = FEATURE_DIR/tasks.md

如果任何必需文件缺失，则中止并显示错误消息。

### 2. 加载制品（渐进式披露）

从每个制品加载仅必要的最小上下文：

**从 spec.md：** 概述/上下文、功能需求、成功标准、用户故事、边缘情况

**从 plan.md：** 架构/技术栈选择、数据模型引用、阶段、技术约束

**从 tasks.md：** 任务ID、描述、阶段分组、并行标记 [P]、引用的文件路径

**从章程：** 加载 `.specify/memory/constitution.md` 用于原则验证

### 3. 检测轮次（Token高效分析）

总共限制为50个发现。检测：重复、歧义、未充分说明、章程对齐、覆盖缺口、不一致。

### 4. 严重性分配

- **关键**：违反章程 MUST、缺失核心规格制品、零覆盖的需求
- **高**：重复或冲突的需求、模糊的安全/性能属性
- **中**：术语漂移、缺失的非功能性任务覆盖
- **低**：风格/措辞改进

### 5. 生成分析报告

输出 Markdown 报告（不写入文件），包含发现表、覆盖摘要、章程对齐问题、未映射任务和指标。

### 6. 提供下一步行动

建议解决关键问题或继续实施的具体命令。