package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuListQuery;
import com.kava.kbpd.upms.domain.repository.ISysMenuRepository;
import com.kava.kbpd.upms.domain.service.ISysMenuService;
import com.kava.kbpd.upms.types.exception.UpmsBizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kava.kbpd.upms.types.exception.UpmsBizErrorCodeEnum.*;

@Service
@RequiredArgsConstructor
public class SysMenuService implements ISysMenuService {
    private final ISysMenuRepository repository;

    @Override
    public SysMenuId create(SysMenuEntity entity) {
        if (entity.getSortOrder() == null) {
            entity.setSortOrder(0);
        }
        validatePid(entity);
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysMenuEntity entity) {
        validatePid(entity);
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysMenuEntity> queryPage(SysMenuListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysMenuEntity queryById(SysMenuId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysMenuId> ids) {
        validateBeforeDelete(ids);
        return repository.removeBatchByIds(ids);
    }

    @Override
    public List<SysMenuEntity> queryAll() {
        return repository.queryAll();
    }

    @Override
    public List<SysMenuEntity> queryByIds(List<SysMenuId> ids) {
        return repository.queryByIds(ids);
    }

    private void validatePid(SysMenuEntity entity) {
        SysMenuId pid = entity.getPid();
        if (pid == null) {
            return;
        }
        SysMenuId id = entity.getId();
        if (id != null && pid.getId().equals(id.getId())) {
            throw new UpmsBizException(MENU_PID_SELF_REFERENCE);
        }
        List<SysMenuEntity> allMenus = repository.queryAll();
        Map<Long, Long> childToParent = allMenus.stream()
                .filter(m -> m.getPid() != null)
                .collect(Collectors.toMap(
                        m -> m.getId().getId(),
                        m -> m.getPid().getId()));
        Long current = pid.getId();
        while (current != null) {
            if (id != null && current.equals(id.getId())) {
                throw new UpmsBizException(MENU_PID_CIRCULAR);
            }
            current = childToParent.get(current);
        }
    }

    private void validateBeforeDelete(List<SysMenuId> ids) {
        for (SysMenuId id : ids) {
            List<SysMenuEntity> children = repository.queryByPid(id);
            if (!children.isEmpty()) {
                throw new UpmsBizException(MENU_HAS_CHILDREN);
            }
            if (repository.existsRoleReference(id)) {
                throw new UpmsBizException(MENU_REFERENCED_BY_ROLE);
            }
        }
    }
}
