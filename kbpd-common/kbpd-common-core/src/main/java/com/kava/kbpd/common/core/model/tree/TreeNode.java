package com.kava.kbpd.common.core.model.tree;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 树节点构建输入，用于 TreeBuilder.build() 的入参
 */
@Getter
@Setter
public class TreeNode<T> {

    private T id;

    private T parentId;

    private String name;

    private Long weight;

    private Map<String, Object> extra;

    public TreeNode(T id, T parentId, String name, Long weight) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.weight = weight;
    }
}
