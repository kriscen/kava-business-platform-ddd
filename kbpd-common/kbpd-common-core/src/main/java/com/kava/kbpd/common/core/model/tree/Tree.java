package com.kava.kbpd.common.core.model.tree;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 通用树节点，用于 API 响应
 * JSON 结构：id, parentId, name, weight, children, extra
 */
@Getter
@Setter
public class Tree<T> {

    private T id;

    private T parentId;

    private String name;

    private Long weight;

    private List<Tree<T>> children = new ArrayList<>();

    private Map<String, Object> extra;

    public Tree() {
    }

    public Tree(T id, T parentId, String name, Long weight) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.weight = weight;
    }
}
