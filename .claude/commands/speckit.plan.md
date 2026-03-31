---
description: 使用计划模板执行实施计划工作流，生成设计工件。
handoffs:
  - label: 创建任务
    agent: speckit.tasks
    prompt: 将计划分解为任务
    send: true
  - label: 创建检查清单
    agent: speckit.checklist
    prompt: 为以下领域创建检查清单...
---

## 用户输入

```text
$ARGUMENTS
```

如果用户输入不为空，你**必须**在继续之前考虑用户输入。

## 执行前检查

**检查扩展钩子（计划前）**：
- 检查项目根目录是否存在 `.specify/extensions.yml`。
- 如存在，读取并查找 `hooks.before_plan` 键下的条目
- 如YAML无法解析或无效，静默跳过钩子检查并正常继续
- 过滤掉 `enabled` 显式为 `false` 的钩子。将无 `enabled` 字段的钩子视为默认启用。
- 对于每个剩余钩子，**不要**尝试解释或评估钩子 `condition` 表达式：
  - 如钩子无 `condition` 字段，或为null/空，视为钩子可执行
  - 如钩子定义非空 `condition`，跳过钩子并将条件评估留给HookExecutor实施
- 对于每个可执行钩子，根据其 `optional` 标志输出以下内容：
  - **可选钩子**（`optional: true`）：
    ```
    ## 扩展钩子

    **可选前置钩子**：{extension}
    命令：`/{command}`
    描述：{description}

    提示：{prompt}
    执行：`/{command}`
    ```
  - **强制钩子**（`optional: false`）：
    ```
    ## 扩展钩子

    **自动前置钩子**：{extension}
    执行：`/{command}`
    EXECUTE_COMMAND: {command}

    等待钩子命令结果后再继续概要。
    ```
- 如无注册钩子或 `.specify/extensions.yml` 不存在，静默跳过

## 概要

1. **设置**：从仓库根目录运行 `.specify/scripts/bash/setup-plan.sh --json` 并解析 JSON 获取 FEATURE_SPEC、IMPL_PLAN、SPECS_DIR、BRANCH。对于参数中的单引号，如 "I'm Groot"，使用转义语法：例如 'I'\''m Groot'（或者尽可能使用双引号："I'm Groot"）。

2. **加载上下文**：读取 FEATURE_SPEC 和 `.specify/memory/constitution.md`。加载 IMPL_PLAN 模板（已复制）。

3. **执行计划工作流**：按 IMPL_PLAN 模板结构：
   - 填充技术上下文（将未知标记为"需要澄清"）
   - 从章程填充章程检查章节
   - 评估门控（如违规无正当理由则ERROR）
   - 阶段0：生成 research.md（解决所有"需要澄清"）
   - 阶段1：生成 data-model.md、contracts/、quickstart.md
   - 阶段1：通过运行代理脚本更新代理上下文
   - 设计后重新评估章程检查

4. **停止并报告**：命令在阶段2计划后结束。报告分支、IMPL_PLAN 路径和生成的工件。

5. **检查扩展钩子**：报告后，检查项目根目录是否存在 `.specify/extensions.yml`。
   - 如存在，读取并查找 `hooks.after_plan` 键下的条目
   - 如YAML无法解析或无效，静默跳过钩子检查并正常继续
   - 过滤掉 `enabled` 显式为 `false` 的钩子。将无 `enabled` 字段的钩子视为默认启用。
   - 对于每个剩余钩子，**不要**尝试解释或评估钩子 `condition` 表达式：
     - 如钩子无 `condition` 字段，或为null/空，视为钩子可执行
     - 如钩子定义非空 `condition`，跳过钩子并将条件评估留给HookExecutor实施
   - 对于每个可执行钩子，根据其 `optional` 标志输出以下内容：
     - **可选钩子**（`optional: true`）：
       ```
       ## 扩展钩子

       **可选钩子**：{extension}
       命令：`/{command}`
       描述：{description}

       提示：{prompt}
       执行：`/{command}`
       ```
     - **强制钩子**（`optional: false`）：
       ```
       ## 扩展钩子

       **自动钩子**：{extension}
       执行：`/{command}`
       EXECUTE_COMMAND: {command}
       ```
   - 如无注册钩子或 `.specify/extensions.yml` 不存在，静默跳过

## 阶段

### 阶段0：概要与研究

1. **从技术上下文提取未知项**：
   - 每个"需要澄清" → 研究任务
   - 每个依赖 → 最佳实践任务
   - 每个集成 → 模式任务

2. **生成并派遣研究代理**：

   ```text
   对于技术上下文中的每个未知：
     任务："为{功能上下文}研究{未知}"
   对于每个技术选择：
     任务："为{领域}中的{技术}查找最佳实践"
   ```

3. **在 `research.md` 中整合发现**，使用格式：
   - 决策：[选择了什么]
   - 理由：[为何选择]
   - 考虑的替代方案：[还评估了什么]

**输出**：research.md，所有"需要澄清"已解决

### 阶段1：设计与合约

**前置条件**：`research.md` 完成

1. **从功能规格提取实体** → `data-model.md`：
   - 实体名称、字段、关系
   - 需求中的验证规则
   - 状态转换（如适用）

2. **定义接口合约**（如项目有外部接口）→ `/contracts/`：
   - 识别项目向用户或其他系统暴露的接口
   - 文档适合项目类型的合约格式
   - 示例：库的公共API、CLI工具的命令schema、Web服务的端点、解析器的语法、应用的UI合约
   - 如项目纯内部（构建脚本、一次性工具等）则跳过

3. **代理上下文更新**：
   - 运行 `.specify/scripts/bash/update-agent-context.sh claude`
   - 这些脚本检测正在使用的AI代理
   - 更新相应的特定代理上下文文件
   - 仅从当前计划添加新技术
   - 在标记间保留手动添加

**输出**：data-model.md、/contracts/*、quickstart.md、特定代理文件

## 关键规则

- 使用绝对路径
- 门控失败或未解决澄清时ERROR