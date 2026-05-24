package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.tree.Tree;
import com.kava.kbpd.common.core.model.tree.TreeBuilder;
import com.kava.kbpd.common.core.model.tree.TreeNode;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaId;
import com.kava.kbpd.upms.domain.model.entity.SysAreaEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaListQuery;
import com.kava.kbpd.upms.domain.repository.ISysAreaRepository;
import com.kava.kbpd.upms.domain.service.ISysAreaService;
import com.kava.kbpd.upms.types.constants.UpmsConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kris
 * @date 2025/3/26
 * @description: 行政区划 service实现
 */
@Service
@RequiredArgsConstructor
public class SysAreaService implements ISysAreaService {

    private final ISysAreaRepository repository;

    @Override
    public SysAreaId create(SysAreaEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysAreaEntity entity) {
        return repository.update(entity);
    }

    @Override
    public Boolean removeBatchByIds(List<SysAreaId> ids) {
        return repository.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysAreaEntity> queryPage(SysAreaListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysAreaEntity queryById(SysAreaId id) {
        return repository.queryById(id);
    }

    @Override
    public List<Tree<Long>> selectAreaTree(SysAreaListQuery query) {
        List<SysAreaEntity> entityList = repository.selectTreeList(query);
        List<TreeNode<Long>> nodeList = new ArrayList<>();

        boolean hasAreaTypeFilter = query.getAreaTypes() != null && !query.getAreaTypes().isEmpty();
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

            Map<String, Object> extraMap = Map.of(SysAreaEntity.Fields.adcode, sysAreaEntity.getAdcode());
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
        return TreeBuilder.build(nodeList, rootId);
    }

    @Override
    public List<SysAreaEntity> selectChildren(SysAreaId pid) {
        Long pidValue = pid != null ? pid.getId() : UpmsConstants.DEFAULT_AREA_PID;
        return repository.selectChildren(pidValue);
    }

}
