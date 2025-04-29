package com.kava.kbpd.upms.domain.permission.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.permission.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysMenuListQuery;
import com.kava.kbpd.upms.domain.permission.repository.ISysMenuRepository;
import com.kava.kbpd.upms.domain.permission.service.ISysMenuService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuService implements ISysMenuService {
    @Resource
    private ISysMenuRepository repository;

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
}