package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.tree.Tree;
import com.kava.kbpd.common.core.model.tree.TreeBuilder;
import com.kava.kbpd.common.core.model.tree.TreeNode;
import com.kava.kbpd.upms.domain.model.entity.SysGroupEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupId;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupListQuery;
import com.kava.kbpd.upms.domain.repository.ISysGroupRepository;
import com.kava.kbpd.upms.domain.service.ISysGroupService;
import com.kava.kbpd.upms.types.exception.UpmsBizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kava.kbpd.upms.types.exception.UpmsBizErrorCodeEnum.*;

@Service
@RequiredArgsConstructor
public class SysGroupService implements ISysGroupService {
    private final ISysGroupRepository repository;

    @Override
    public SysGroupId create(SysGroupEntity entity) {
        validatePid(entity);
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysGroupEntity entity) {
        validatePid(entity);
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysGroupEntity> queryPage(SysGroupListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysGroupEntity queryById(SysGroupId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysGroupId> ids) {
        validateBeforeDelete(ids);
        return repository.removeBatchByIds(ids);
    }

    @Override
    public List<SysGroupEntity> queryAll() {
        return repository.queryAll();
    }

    @Override
    public List<Tree<Long>> queryTree() {
        List<SysGroupEntity> allGroups = repository.queryAll();
        List<TreeNode<Long>> nodes = allGroups.stream()
                .map(d -> new TreeNode<>(
                        d.getId().getId(),
                        d.getPid() != null ? d.getPid().getId() : null,
                        d.getName(),
                        (long) (d.getSortOrder() != null ? d.getSortOrder() : 0)))
                .toList();
        return TreeBuilder.build(nodes, null);
    }

    private void validatePid(SysGroupEntity entity) {
        SysGroupId pid = entity.getPid();
        if (pid == null) {
            return;
        }
        SysGroupId id = entity.getId();
        if (id != null && pid.getId().equals(id.getId())) {
            throw new UpmsBizException(GROUP_PID_SELF_REFERENCE);
        }
        List<SysGroupEntity> allGroups = repository.queryAll();
        Map<Long, Long> childToParent = allGroups.stream()
                .filter(d -> d.getPid() != null)
                .collect(Collectors.toMap(
                        d -> d.getId().getId(),
                        d -> d.getPid().getId()));
        Long current = pid.getId();
        while (current != null) {
            if (id != null && current.equals(id.getId())) {
                throw new UpmsBizException(GROUP_PID_CIRCULAR);
            }
            current = childToParent.get(current);
        }
    }

    private void validateBeforeDelete(List<SysGroupId> ids) {
        for (SysGroupId id : ids) {
            List<SysGroupEntity> children = repository.queryByPid(id);
            if (!children.isEmpty()) {
                throw new UpmsBizException(GROUP_HAS_CHILDREN);
            }
            if (repository.existsUserReference(id)) {
                throw new UpmsBizException(GROUP_REFERENCED_BY_USER);
            }
        }
    }
}
