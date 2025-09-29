package com.kava.kbpd.upms.domain.service;

import cn.hutool.core.lang.tree.Tree;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/3/26
 * @description: 行政区划 service
 */
public interface ISysAreaService {

    List<Tree<Long>> selectAreaTree(SysAreaListQuery query);

}
