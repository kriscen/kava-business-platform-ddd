package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.tree.Tree;
import com.kava.kbpd.upms.application.converter.SysGroupAppConverter;
import com.kava.kbpd.upms.application.model.command.SysGroupCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysGroupUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysGroupAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysGroupAppListDTO;
import com.kava.kbpd.upms.application.service.ISysGroupAppService;
import com.kava.kbpd.upms.domain.model.entity.SysGroupEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupId;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupListQuery;
import com.kava.kbpd.upms.domain.service.ISysGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: group application service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysGroupAppService implements ISysGroupAppService {
    private final ISysGroupService sysGroupService;
    private final SysGroupAppConverter sysGroupAppConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysGroupId createGroup(SysGroupCreateCommand command) {
        SysGroupEntity sysGroupEntity = sysGroupAppConverter.convertCreateCommand2Entity(command);
        return sysGroupService.create(sysGroupEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGroup(SysGroupUpdateCommand command) {
        SysGroupEntity sysGroupEntity = sysGroupAppConverter.convertUpdateCommand2Entity(command);
        sysGroupService.update(sysGroupEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeGroupBatchByIds(List<SysGroupId> ids) {
        sysGroupService.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysGroupAppListDTO> queryGroupPage(SysGroupListQuery query) {
        PagingInfo<SysGroupEntity> sysGroupEntityPagingInfo = sysGroupService.queryPage(query);
        List<SysGroupAppListDTO> collect = sysGroupEntityPagingInfo.getList().stream().map(sysGroupAppConverter::convertEntityToListQueryDTO).toList();

        List<Long> pids = collect.stream().map(SysGroupAppListDTO::getPid).filter(Objects::nonNull).distinct().toList();
        Map<Long, String> parentNameMap = pids.isEmpty() ? Collections.emptyMap() :
                sysGroupService.queryAll().stream()
                        .filter(d -> d.getId() != null)
                        .collect(Collectors.toMap(d -> d.getId().getId(), SysGroupEntity::getName, (a, b) -> a));
        collect.forEach(dto -> dto.setParentName(parentNameMap.get(dto.getPid())));

        return PagingInfo.toResponse(collect, sysGroupEntityPagingInfo);
    }

    @Override
    public SysGroupAppDetailDTO queryGroupById(SysGroupId id) {
        SysGroupEntity groupEntity = sysGroupService.queryById(id);
        SysGroupAppDetailDTO dto = sysGroupAppConverter.convertEntityToDetailDTO(groupEntity);
        if (dto != null && groupEntity.getPid() != null) {
            SysGroupEntity parent = sysGroupService.queryById(groupEntity.getPid());
            dto.setParentName(parent != null ? parent.getName() : null);
        }
        return dto;
    }

    @Override
    public List<SysGroupAppListDTO> queryGroupTree() {
        List<Tree<Long>> trees = sysGroupService.queryTree();
        Map<Long, String> nameMap = buildNameMap(trees);
        return convertGroupTreeList(trees, nameMap);
    }

    private Map<Long, String> buildNameMap(List<Tree<Long>> trees) {
        Map<Long, String> map = new HashMap<>();
        Deque<List<Tree<Long>>> stack = new ArrayDeque<>();
        stack.push(trees);
        while (!stack.isEmpty()) {
            List<Tree<Long>> level = stack.pop();
            for (Tree<Long> tree : level) {
                map.put(tree.getId(), tree.getName());
                if (tree.getChildren() != null && !tree.getChildren().isEmpty()) {
                    stack.push(tree.getChildren());
                }
            }
        }
        return map;
    }

    private List<SysGroupAppListDTO> convertGroupTreeList(List<Tree<Long>> trees, Map<Long, String> nameMap) {
        if (trees == null || trees.isEmpty()) return List.of();
        return trees.stream().map(tree -> convertGroupTree(tree, nameMap)).toList();
    }

    private SysGroupAppListDTO convertGroupTree(Tree<Long> tree, Map<Long, String> nameMap) {
        SysGroupAppListDTO dto = new SysGroupAppListDTO();
        dto.setId(tree.getId());
        dto.setName(tree.getName());
        dto.setPid(tree.getParentId());
        dto.setSortOrder(tree.getWeight() != null ? tree.getWeight().intValue() : null);
        dto.setParentName(tree.getParentId() != null ? nameMap.get(tree.getParentId()) : null);
        dto.setChildren(convertGroupTreeList(tree.getChildren(), nameMap));
        return dto;
    }

}
