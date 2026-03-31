# 实施计划：[功能名称]

**分支**：`[###-feature-name]` | **日期**：[DATE] | **规格**：[link]
**输入**：来自 `/specs/[###-feature-name]/spec.md` 的功能规格

**注意**：此模板由 `/speckit.plan` 命令填充。执行工作流请参阅 `.specify/templates/plan-template.md`。

## 概要

[从功能规格中提取：主要需求 + 研究得出的技术方案]

## 技术上下文

<!--
  需要操作：将本节内容替换为项目的技术细节。
  此处结构仅供参考，用于指导迭代过程。
-->

**语言/版本**：[如：Python 3.11, Swift 5.9, Rust 1.75 或 需要澄清]
**主要依赖**：[如：FastAPI, UIKit, LLVM 或 需要澄清]
**存储**：[如适用，如：PostgreSQL, CoreData, 文件 或 不适用]
**测试**：[如：pytest, XCTest, cargo test 或 需要澄清]
**目标平台**：[如：Linux 服务器, iOS 15+, WASM 或 需要澄清]
**项目类型**：[如：库/CLI/Web服务/移动应用/编译器/桌面应用 或 需要澄清]
**性能目标**：[领域特定，如：1000 请求/秒, 1万行/秒, 60 fps 或 需要澄清]
**约束条件**：[领域特定，如：<200ms p95, <100MB 内存, 离线可用 或 需要澄清]
**规模/范围**：[领域特定，如：1万用户, 100万行代码, 50个页面 或 需要澄清]

## 宪章检查

*门禁：必须在阶段 0 研究前通过。在阶段 1 设计后重新检查。*

[根据宪章文件确定的门禁项]

## 项目结构

### 文档（本功能）

```text
specs/[###-feature]/
├── plan.md              # 本文件（/speckit.plan 命令输出）
├── research.md          # 阶段 0 输出（/speckit.plan 命令）
├── data-model.md        # 阶段 1 输出（/speckit.plan 命令）
├── quickstart.md        # 阶段 1 输出（/speckit.plan 命令）
├── contracts/           # 阶段 1 输出（/speckit.plan 命令）
└── tasks.md             # 阶段 2 输出（/speckit.tasks 命令 - 非 /speckit.plan 创建）
```

### 源代码（仓库根目录）
<!--
  需要操作：将下方占位树替换为此功能的具体布局。
  删除未使用的选项，用真实路径扩展所选结构（如 apps/admin, packages/something）。
  交付的计划不得包含"选项"标签。
-->

```text
# [如未使用则删除] 选项 1：单项目（默认）
src/
├── models/
├── services/
├── cli/
└── lib/

tests/
├── contract/
├── integration/
└── unit/

# [如未使用则删除] 选项 2：Web 应用（当检测到"前端"+"后端"时）
backend/
├── src/
│   ├── models/
│   ├── services/
│   └── api/
└── tests/

frontend/
├── src/
│   ├── components/
│   ├── pages/
│   └── services/
└── tests/

# [如未使用则删除] 选项 3：移动端 + API（当检测到"iOS/Android"时）
api/
└── [同上 backend 结构]

ios/ 或 android/
└── [平台特定结构：功能模块、UI 流程、平台测试]
```

**结构决策**：[记录所选结构并引用上方捕获的实际目录]

## 复杂度追踪

> **仅在宪章检查存在必须说明的违规时填写**

| 违规项 | 需要原因 | 拒绝更简单方案的原因 |
|--------|----------|---------------------|
| [如：第4个项目] | [当前需求] | [为何3个项目不足够] |
| [如：仓储模式] | [具体问题] | [为何直接数据库访问不足够] |