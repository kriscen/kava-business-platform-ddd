package com.kava.kbpd.common.core.model.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从扁平列表构建树结构
 */
public final class TreeBuilder {

    private TreeBuilder() {
    }

    /**
     * 将扁平的 TreeNode 列表构建为树
     *
     * @param nodes  扁平节点列表
     * @param rootId 根节点的 parentId
     * @return 树的根节点列表
     */
    public static <T> List<Tree<T>> build(List<TreeNode<T>> nodes, T rootId) {
        Map<T, List<TreeNode<T>>> parentGroup = new HashMap<>();
        for (TreeNode<T> node : nodes) {
            parentGroup.computeIfAbsent(node.getParentId(), k -> new ArrayList<>()).add(node);
        }

        List<TreeNode<T>> roots = parentGroup.getOrDefault(rootId, List.of());
        List<Tree<T>> result = new ArrayList<>(roots.size());
        for (TreeNode<T> root : roots) {
            result.add(buildTree(root, parentGroup));
        }
        return result;
    }

    private static <T> Tree<T> buildTree(TreeNode<T> node, Map<T, List<TreeNode<T>>> parentGroup) {
        Tree<T> tree = new Tree<>(node.getId(), node.getParentId(), node.getName(), node.getWeight());
        tree.setExtra(node.getExtra());

        List<TreeNode<T>> children = parentGroup.getOrDefault(node.getId(), List.of());
        for (TreeNode<T> child : children) {
            tree.getChildren().add(buildTree(child, parentGroup));
        }
        return tree;
    }
}
