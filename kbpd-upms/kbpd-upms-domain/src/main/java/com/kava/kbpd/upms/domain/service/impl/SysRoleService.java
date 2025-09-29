package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;
import com.kava.kbpd.upms.domain.repository.ISysRoleRepository;
import com.kava.kbpd.upms.domain.service.ISysRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleService implements ISysRoleService {
    @Resource
    private ISysRoleRepository repository;

    @Override
    public SysRoleId create(SysRoleEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysRoleEntity entity) {
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysRoleEntity> queryPage(SysRoleListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysRoleEntity queryById(SysRoleId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysRoleId> ids) {
        return repository.removeBatchByIds(ids);
    }
}