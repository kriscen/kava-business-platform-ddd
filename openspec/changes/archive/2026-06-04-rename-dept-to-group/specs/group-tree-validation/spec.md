## RENAMED Requirements

### FROM: group-tree-validation → TO: group-tree-validation

整个 capability 从 `group-tree-validation` 重命名为 `group-tree-validation`。替换规则同 `group-domain-service`，额外包括：

- spec 中所有"分组"措辞替换为"分组"
- 错误码描述文本更新
- 场景描述中的"子分组"→"子分组"

树形验证逻辑（自引用检查、循环引用检查、子节点检查、用户引用检查）完全不变。
