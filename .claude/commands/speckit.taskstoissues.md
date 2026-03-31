---
description: 将现有任务转换为可执行、按依赖排序的 GitHub issues，基于可用的设计工件。
tools: ['github/github-mcp-server/issue_write']
---

## 用户输入

```text
$ARGUMENTS
```

如果用户输入不为空，你**必须**在继续之前考虑用户输入。

## 概要

1. 从仓库根目录运行 `.specify/scripts/bash/check-prerequisites.sh --json --require-tasks --include-tasks` 并解析 FEATURE_DIR 和 AVAILABLE_DOCS 列表。所有路径必须是绝对路径。对于参数中的单引号，如 "I'm Groot"，使用转义语法：例如 'I'\''m Groot'（或者尽可能使用双引号："I'm Groot"）。
1. 从执行的脚本中提取 **tasks** 的路径。
1. 通过运行以下命令获取Git远程：

```bash
git config --get remote.origin.url
```

> [!警告]
> 仅当远程是GITHUB URL时才继续下一步

1. 对于列表中的每个任务，使用GitHub MCP服务器在Git远程对应的仓库中创建新issue。

> [!警告]
> 在任何情况下都不在非匹配远程URL的仓库中创建issues