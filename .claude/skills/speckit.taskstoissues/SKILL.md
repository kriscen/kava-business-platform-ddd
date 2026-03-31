---
name: speckit.taskstoissues
description: 将现有任务转换为可执行、按依赖排序的 GitHub issues。当用户要求创建 GitHub issues、导出任务到 issue tracker 或与 GitHub 同步任务时触发。
compatibility: ['github/github-mcp-server/issue_write']
---

## 概要

### 1. 初始化

运行 `.specify/scripts/bash/check-prerequisites.sh --json --require-tasks --include-tasks` 获取 FEATURE_DIR 和 AVAILABLE_DOCS。

### 2. 获取 Git 远程

运行 `git config --get remote.origin.url` 获取远程 URL。

**警告**：仅当远程是 GITHUB URL 时才继续。

### 3. 创建 Issues

对于每个任务，使用 GitHub MCP 服务器在对应仓库中创建新 issue。

**警告**：不在非匹配远程 URL 的仓库中创建 issues。