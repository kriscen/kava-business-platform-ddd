package com.kava.kbpd.upms.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaId;
import com.kava.kbpd.upms.domain.model.entity.SysAreaEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaListQuery;
import com.kava.kbpd.upms.domain.repository.ISysAreaRepository;
import com.kava.kbpd.upms.domain.service.ISysAreaService;
import com.kava.kbpd.upms.types.constants.UpmsConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

        boolean hasAreaTypeFilter = CollUtil.isNotEmpty(query.getAreaTypes());
        Set<Long> adcodeSet = null;
        if (hasAreaTypeFilter) {
            adcodeSet = entityList.stream().map(SysAreaEntity::getAdcode).collect(Collectors.toSet());
        }

        for (SysAreaEntity sysAreaEntity : entityList) {
            Long parentId = sysAreaEntity.getPid().getId();
            if (hasAreaTypeFilter && !adcodeSet.contains(parentId)) {
                parentId = 0L;
            }
            TreeNode<Long> treeNode = new TreeNode<>(sysAreaEntity.getAdcode(), parentId,
                    sysAreaEntity.getName(), -Optional.ofNullable(sysAreaEntity.getAreaSort())
                    .orElse(UpmsConstants.DEFAULT_AREA_SORT));

            HashMap<String, Object> extraMap = MapUtil.of(SysAreaEntity.Fields.adcode, sysAreaEntity.getAdcode());
            treeNode.setExtra(extraMap);
            nodeList.add(treeNode);
        }

        Long rootId;
        if (query.getPid() != null) {
            rootId = query.getPid();
        } else if (hasAreaTypeFilter) {
            rootId = 0L;
        } else {
            rootId = UpmsConstants.DEFAULT_AREA_PID;
        }
        return TreeUtil.build(nodeList, rootId);
    }

    @Override
    public List<SysAreaEntity> selectChildren(SysAreaId pid) {
        Long pidValue = pid != null ? pid.getId() : UpmsConstants.DEFAULT_AREA_PID;
        return repository.selectChildren(pidValue);
    }

}
