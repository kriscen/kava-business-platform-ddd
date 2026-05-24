package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.tree.Tree;
import com.kava.kbpd.common.core.model.tree.TreeBuilder;
import com.kava.kbpd.common.core.model.tree.TreeNode;
import com.kava.kbpd.upms.application.converter.SysDeptAppConverter;
import com.kava.kbpd.upms.application.model.command.SysDeptCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysDeptUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysDeptAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysDeptAppListDTO;
import com.kava.kbpd.upms.application.service.ISysDeptAppService;
import com.kava.kbpd.upms.domain.model.entity.SysDeptEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptId;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptListQuery;
import com.kava.kbpd.upms.domain.service.ISysDeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: dept application service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDeptAppService implements ISysDeptAppService {
    private final ISysDeptService sysDeptService;
    private final SysDeptAppConverter sysDeptAppConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysDeptId createDept(SysDeptCreateCommand command) {
        SysDeptEntity sysDeptEntity = sysDeptAppConverter.convertCreateCommand2Entity(command);
        return sysDeptService.create(sysDeptEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(SysDeptUpdateCommand command) {
        SysDeptEntity sysDeptEntity = sysDeptAppConverter.convertUpdateCommand2Entity(command);
        sysDeptService.update(sysDeptEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeDeptBatchByIds(List<SysDeptId> ids) {
        sysDeptService.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysDeptAppListDTO> queryDeptPage(SysDeptListQuery query) {
        PagingInfo<SysDeptEntity> sysDeptEntityPagingInfo = sysDeptService.queryPage(query);
        List<SysDeptAppListDTO> collect = sysDeptEntityPagingInfo.getList().stream().map(sysDeptAppConverter::convertEntityToListQueryDTO).toList();

        List<Long> pids = collect.stream().map(SysDeptAppListDTO::getPid).filter(Objects::nonNull).distinct().toList();
        Map<Long, String> parentNameMap = pids.isEmpty() ? Collections.emptyMap() :
                sysDeptService.queryAll().stream()
                        .filter(d -> d.getId() != null)
                        .collect(Collectors.toMap(d -> d.getId().getId(), SysDeptEntity::getName, (a, b) -> a));
        collect.forEach(dto -> dto.setParentName(parentNameMap.get(dto.getPid())));

        return PagingInfo.toResponse(collect, sysDeptEntityPagingInfo);
    }

    @Override
    public SysDeptAppDetailDTO queryDeptById(SysDeptId id) {
        SysDeptEntity deptEntity = sysDeptService.queryById(id);
        SysDeptAppDetailDTO dto = sysDeptAppConverter.convertEntityToDetailDTO(deptEntity);
        if (dto != null && deptEntity.getPid() != null) {
            SysDeptEntity parent = sysDeptService.queryById(deptEntity.getPid());
            dto.setParentName(parent != null ? parent.getName() : null);
        }
        return dto;
    }

    @Override
    public List<SysDeptAppListDTO> queryDeptTree() {
        List<SysDeptEntity> allDepts = sysDeptService.queryTree();

        Map<Long, String> nameMap = allDepts.stream()
                .filter(d -> d.getId() != null)
                .collect(Collectors.toMap(d -> d.getId().getId(), SysDeptEntity::getName, (a, b) -> a));

        List<TreeNode<Long>> nodes = allDepts.stream()
                .map(d -> new TreeNode<>(
                        d.getId().getId(),
                        d.getPid() != null ? d.getPid().getId() : null,
                        d.getName(),
                        (long) (d.getSortOrder() != null ? d.getSortOrder() : 0)))
                .toList();

        List<Tree<Long>> trees = TreeBuilder.build(nodes, null);
        return convertDeptTreeList(trees, nameMap);
    }

    private List<SysDeptAppListDTO> convertDeptTreeList(List<Tree<Long>> trees, Map<Long, String> nameMap) {
        if (trees == null || trees.isEmpty()) return List.of();
        return trees.stream().map(tree -> convertDeptTree(tree, nameMap)).toList();
    }

    private SysDeptAppListDTO convertDeptTree(Tree<Long> tree, Map<Long, String> nameMap) {
        SysDeptAppListDTO dto = new SysDeptAppListDTO();
        dto.setId(tree.getId());
        dto.setName(tree.getName());
        dto.setPid(tree.getParentId());
        dto.setSortOrder(tree.getWeight() != null ? tree.getWeight().intValue() : null);
        dto.setParentName(tree.getParentId() != null ? nameMap.get(tree.getParentId()) : null);
        dto.setChildren(convertDeptTreeList(tree.getChildren(), nameMap));
        return dto;
    }

}
