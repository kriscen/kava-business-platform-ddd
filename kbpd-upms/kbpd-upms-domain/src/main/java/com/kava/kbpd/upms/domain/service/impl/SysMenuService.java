package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuListQuery;
import com.kava.kbpd.upms.domain.repository.ISysMenuRepository;
import com.kava.kbpd.upms.domain.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysMenuService implements ISysMenuService {
    private final ISysMenuRepository repository;

    @Override
    public SysMenuId create(SysMenuEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysMenuEntity entity) {
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
}
