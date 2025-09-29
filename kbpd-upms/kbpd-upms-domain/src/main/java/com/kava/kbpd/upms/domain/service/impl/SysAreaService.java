package com.kava.kbpd.upms.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysAreaEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaId;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaListQuery;
import com.kava.kbpd.upms.domain.repository.ISysAreaRepository;
import com.kava.kbpd.upms.domain.service.ISysAreaService;
import com.kava.kbpd.upms.types.constants.UpmsConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author Kris
 * @date 2025/3/26
 * @description: 行政区划 service实现
 */
@Service
public class SysAreaService implements ISysAreaService {

    @Resource
    private ISysAreaRepository repository;


    @Override
    public List<Tree<Long>> selectAreaTree(SysAreaListQuery query) {
        List<SysAreaEntity> entityList = repository.selectTreeList(query);
        List<TreeNode<Long>> nodeList = CollUtil.newArrayList();
        for (SysAreaEntity sysAreaEntity : entityList) {
            TreeNode<Long> treeNode = new TreeNode<>(sysAreaEntity.getAdcode(), sysAreaEntity.getPid().getId(),
                    sysAreaEntity.getName(), -Optional.ofNullable(sysAreaEntity.getAreaSort())
                    .orElse(UpmsConstants.DEFAULT_AREA_SORT));

            HashMap<String, Object> extraMap = MapUtil.of(SysAreaEntity.Fields.adcode, sysAreaEntity.getAdcode());
            treeNode.setExtra(extraMap);
            nodeList.add(treeNode);
        }
        return TreeUtil.build(nodeList, Optional.ofNullable(query.getPid()).orElse(UpmsConstants.DEFAULT_AREA_PID));
    }

}
