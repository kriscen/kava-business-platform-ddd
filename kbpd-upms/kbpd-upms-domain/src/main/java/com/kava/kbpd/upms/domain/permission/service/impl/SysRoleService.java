package com.kava.kbpd.upms.domain.permission.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.permission.model.entity.SysRoleEntity;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysRoleListQuery;
import com.kava.kbpd.upms.domain.permission.repository.ISysRoleRepository;
import com.kava.kbpd.upms.domain.permission.service.ISysRoleService;
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