package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysUserEntity;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;
import com.kava.kbpd.upms.domain.repository.ISysUserRepository;
import com.kava.kbpd.upms.domain.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserService implements ISysUserService {
    @Resource
    private ISysUserRepository repository;

    @Override
    public SysUserId create(SysUserEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysUserEntity entity) {
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysUserEntity> queryPage(SysUserListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysUserEntity queryById(SysUserId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysUserId> ids) {
        return repository.removeBatchByIds(ids);
    }
}