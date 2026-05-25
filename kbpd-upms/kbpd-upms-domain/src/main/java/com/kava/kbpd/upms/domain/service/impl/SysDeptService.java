package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.tree.Tree;
import com.kava.kbpd.common.core.model.tree.TreeBuilder;
import com.kava.kbpd.common.core.model.tree.TreeNode;
import com.kava.kbpd.upms.domain.model.entity.SysDeptEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptId;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptListQuery;
import com.kava.kbpd.upms.domain.repository.ISysDeptRepository;
import com.kava.kbpd.upms.domain.service.ISysDeptService;
import com.kava.kbpd.upms.types.exception.UpmsBizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kava.kbpd.upms.types.exception.UpmsBizErrorCodeEnum.*;

@Service
@RequiredArgsConstructor
public class SysDeptService implements ISysDeptService {
    private final ISysDeptRepository repository;

    @Override
    public SysDeptId create(SysDeptEntity entity) {
        validatePid(entity);
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysDeptEntity entity) {
        validatePid(entity);
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysDeptEntity> queryPage(SysDeptListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysDeptEntity queryById(SysDeptId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysDeptId> ids) {
        validateBeforeDelete(ids);
        return repository.removeBatchByIds(ids);
    }

    @Override
    public List<SysDeptEntity> queryAll() {
        return repository.queryAll();
    }

    @Override
    public List<Tree<Long>> queryTree() {
        List<SysDeptEntity> allDepts = repository.queryAll();
        List<TreeNode<Long>> nodes = allDepts.stream()
                .map(d -> new TreeNode<>(
                        d.getId().getId(),
                        d.getPid() != null ? d.getPid().getId() : null,
                        d.getName(),
                        (long) (d.getSortOrder() != null ? d.getSortOrder() : 0)))
                .toList();
        return TreeBuilder.build(nodes, null);
    }

    private void validatePid(SysDeptEntity entity) {
        SysDeptId pid = entity.getPid();
        if (pid == null) {
            return;
        }
        SysDeptId id = entity.getId();
        if (id != null && pid.getId().equals(id.getId())) {
            throw new UpmsBizException(DEPT_PID_SELF_REFERENCE);
        }
        List<SysDeptEntity> allDepts = repository.queryAll();
        Map<Long, Long> childToParent = allDepts.stream()
                .filter(d -> d.getPid() != null)
                .collect(Collectors.toMap(
                        d -> d.getId().getId(),
                        d -> d.getPid().getId()));
        Long current = pid.getId();
        while (current != null) {
            if (id != null && current.equals(id.getId())) {
                throw new UpmsBizException(DEPT_PID_CIRCULAR);
            }
            current = childToParent.get(current);
        }
    }

    private void validateBeforeDelete(List<SysDeptId> ids) {
        for (SysDeptId id : ids) {
            List<SysDeptEntity> children = repository.queryByPid(id);
            if (!children.isEmpty()) {
                throw new UpmsBizException(DEPT_HAS_CHILDREN);
            }
            if (repository.existsUserReference(id)) {
                throw new UpmsBizException(DEPT_REFERENCED_BY_USER);
            }
        }
    }
}
