---
name: speckit.constitution
description: 从交互式或提供的原则输入创建或更新项目章程，确保所有相关模板保持同步。当用户要求设置项目原则、创建章程或更新治理规则时触发。
---

## 概要

更新 `.specify/memory/constitution.md` 的项目章程。收集/推导具体值，精确填充模板，传播修改到相关制品。

## 执行步骤

### 1. 加载现有章程

识别每个 `[ALL_CAPS_IDENTIFIER]` 形式的占位符token。

### 2. 收集/推导值

- 如果用户输入提供值，使用它
- 否则从现有仓库上下文推断
- 治理日期：RATIFICATION_DATE 是原始采用日期，LAST_AMENDED_DATE 如有更改则为今天
- CONSTITUTION_VERSION 按语义版本规则递增

### 3. 起草更新章程

用具体文本替换占位符，保持标题层次。

### 4. 一致性传播检查

读取并验证模板对齐：
- `.specify/templates/plan-template.md`
- `.specify/templates/spec-template.md`
- `.specify/templates/tasks-template.md`

### 5. 生成同步影响报告

作为HTML注释置于章程文件顶部，包含版本变更、修改原则列表、添加/移除章节、模板更新状态。

### 6. 验证

- 无残留未解释的方括号token
- 版本行匹配报告
- 日期ISO格式YYYY-MM-DD
- 原则是声明性的、可测试的

### 7. 写回章程

将完成章程写回 `.specify/memory/constitution.md`。

### 8. 报告

输出新版本、提升理由、标记手动跟进的文件、建议提交消息。