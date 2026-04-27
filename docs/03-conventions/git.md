# Git 规范

## 4.1 分支管理

### 4.1.1 分支命名

```
{类型}/{迭代}-{描述}

示例：
feature/001-user-login
feature/002-oauth2-integration
bugfix/003-fix-login-timeout
hotfix/004-security-patch
release/v1.0.0
```

| 类型 | 用途 | 命名格式 |
|------|------|---------|
| feature | 新功能开发 | `feature/{迭代号}-{简短描述}` |
| bugfix | 缺陷修复 | `bugfix/{迭代号}-{简短描述}` |
| hotfix | 紧急修复 | `hotfix/{问题描述}` |
| release | 发布分支 | `release/{版本号}` |
| refactor | 重构 | `refactor/{描述}` |

### 4.1.2 主分支

| 分支 | 用途 | 保护策略 |
|------|------|---------|
| `main` | 主分支，稳定可发布 | 必须 PR，不能直接推送 |
| `develop` | 开发主分支 | 汇总 feature 分支 |

### 4.1.3 分支流程

```
                    ┌─ feature/001 ────┐
                    │                  │
    develop ────────┤                  ├─── develop ──── release/v1.0.0 ──── main
                    │                  │
                    └─ feature/002 ────┘
                          ▲
                          │
                    从 develop 创建
```

## 4.2 提交规范

### 4.2.1 提交信息格式

```
{类型}({范围}): {简短描述}

[可选正文]

[可选 footer]
```

### 4.2.2 提交类型

| 类型 | 描述 | 示例 |
|------|------|------|
| `feat` | 新功能 | `feat(upms): 添加用户锁定功能` |
| `fix` | 修复 bug | `fix(auth): 修复 Token 刷新问题` |
| `docs` | 文档更新 | `docs: 更新 API 文档` |
| `style` | 代码格式（不影响功能） | `style: 格式化代码` |
| `refactor` | 重构（非功能修改） | `refactor(user): 简化密码校验逻辑` |
| `perf` | 性能优化 | `perf: 优化用户查询性能` |
| `test` | 测试相关 | `test: 添加用户创建测试用例` |
| `chore` | 构建/工具相关 | `chore: 升级 Spring 版本` |
| `ci` | CI 配置 | `ci: 添加 GitHub Actions` |
| `revert` | 回滚 | `revert: 回滚提交 abc123` |

### 4.2.3 提交示例

```bash
# 简单提交
git commit -m "feat(upms): 添加用户锁定功能"

# 详细提交（多行）
git commit -m "feat(upms): 添加用户锁定功能

- 添加 SysUserAggregate.lock() 方法
- 更新 UserStatus 枚举
- 添加对应的单元测试

Closes #123"

# 带 footer
git commit -m "fix(auth): 修复 Token 过期后刷新失败的问题

问题原因：refresh_token 验证时未检查 token 类型
修复方案：在验证逻辑中增加 token 类型校验

Fix #456
Reviewed-by: @zhangsan"
```

### 4.2.4 提交频率

- **推荐**：每完成一个独立功能点或修复提交一次
- **最小提交**：不鼓励过频繁琐碎提交（如每改一行就提交）
- **最大提交**：单个提交不应超过 500 行变更

## 4.3 Pull Request 规范

### 4.3.1 PR 创建流程

1. **Fork**（如适用）或从 `develop` 创建 feature 分支
2. 开发完成，推送到远程
3. 创建 PR，指定目标分支为 `develop`
4. 至少 1 人 Review 通过后方可合并
5. 合并后删除源分支

### 4.3.2 PR 标题格式

```
{类型}({模块}): {描述} {#issue号}

示例：
feat(upms): 添加用户角色分配功能 {#123}
fix(auth): 修复 OAuth2 回调地址解析错误
```

### 4.3.3 PR 描述模板

```markdown
## Summary
简要描述本次 PR 的变更内容和目的

## Changes
- 变更点 1
- 变更点 2

## Test Plan
- [ ] 单元测试通过
- [ ] 集成测试通过
- [ ] 手动验证（描述验证步骤）

## Screenshots（可选）
如有 UI 变更，附上截图

## Related Issues
Closes #123
```

### 4.3.4 Code Review 注意事项

- **Reviewer**：
  - 检查代码逻辑是否正确
  - 检查是否遵循本规范的代码风格
  - 检查是否有潜在安全风险
  - 检查测试是否充分

- **Author**：
  - 及时响应 Review 意见
  - 不要在 PR 中混合无关变更
  - 保持提交历史整洁

## 4.4 合并策略

### 4.4.1 Merge vs Rebase

| 场景 | 策略 | 说明 |
|------|------|------|
| feature → develop | Merge | 保留完整提交历史 |
| hotfix → main | Merge | 保留修复记录 |
| develop → main | Merge | 发布版本保留历史 |
| 本地同步远程 | Rebase | 保持提交历史线性 |

```bash
# 推荐：保持线性历史
git fetch origin
git rebase origin/develop

# 需要合并历史时使用 merge
git checkout develop
git merge feature/001-user-login
```

### 4.4.2 冲突处理

```bash
# 1. 更新最新代码
git fetch origin

# 2. 变基（rebase 方式）
git rebase origin/develop

# 3. 解决冲突后
git add .
git rebase --continue

# 4. 强制推送（谨慎）
git push --force-with-lease
```

## 4.5 标签（Tag）管理

### 4.5.1 版本标签

遵循语义化版本 `v{MAJOR}.{MINOR}.{PATCH}`：

```bash
# 打标签
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin v1.0.0

# 删除标签
git tag -d v1.0.0
git push origin --delete v1.0.0
```

### 4.5.2 标签类型

| 标签格式 | 用途 |
|---------|------|
| `v1.0.0` | 正式发布版本 |
| `v1.0.0-rc.1` | 预发布版本 |
| `v1.0.0-beta.1` | 测试版本 |
| `v1.0.0-hotfix.1` | 热修复版本 |

## 4.6 Git Hooks

项目使用 `pre-commit` 和 `commit-msg` hooks 确保规范执行：

```bash
# 安装 hooks（项目根目录执行）
mvn org.apache.maven.plugins:maven-scm-plugin:1.9.5:check-policy

# 或手动链接
ln -sf ../../scripts/pre-commit.sh .git/hooks/pre-commit
ln -sf ../../scripts/commit-msg.sh .git/hooks/commit-msg
```

### 4.6.1 pre-commit Hook

- 运行 `mvn spotless:check` 检查代码格式
- 运行 `mvn test` 执行单元测试（可选，耗时较长可跳过）

### 4.6.2 commit-msg Hook

- 验证提交信息格式是否符合规范
- 自动补充 `Closes #issue` 关联

## 4.7 常用 Git 命令速查

```bash
# 创建并切换分支
git checkout -b feature/001-user-login

# 暂存变更
git add -p  # 交互式暂存（推荐）

# 提交
git commit -m "feat(upms): 添加用户登录功能"

# 拉取最新代码（rebase 方式）
git pull --rebase origin develop

# 查看状态
git status

# 查看提交历史
git log --oneline -10

# 查看差异
git diff --staged  # 暂存区 vs HEAD
git diff          # 工作区 vs 暂存区

# 撤销
git reset HEAD file.txt    # 取消暂存
git checkout -- file.txt   # 撤销工作区修改

# 压缩提交（交互式 rebase）
git rebase -i HEAD~3
```

## 4.8 注意事项

1. **不要强制推送 main/develop 分支**
2. **不要在 PR 中包含不相关的变更**
3. **保持提交信息清晰准确，便于追溯**
4. **删除已合并的远程分支**
5. **敏感信息（密码、密钥）禁止提交到 Git**
