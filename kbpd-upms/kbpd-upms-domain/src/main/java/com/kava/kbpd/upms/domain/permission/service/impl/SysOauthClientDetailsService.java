package com.kava.kbpd.upms.domain.permission.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.permission.model.entity.SysOauthClientDetailsEntity;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysOauthClientDetailsId;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysOauthClientDetailsListQuery;
import com.kava.kbpd.upms.domain.permission.repository.ISysOauthClientDetailsRepository;
import com.kava.kbpd.upms.domain.permission.service.ISysOauthClientDetailsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysOauthClientDetailsService implements ISysOauthClientDetailsService {
    @Resource
    private ISysOauthClientDetailsRepository repository;

    @Override
    public SysOauthClientDetailsId create(SysOauthClientDetailsEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysOauthClientDetailsEntity entity) {
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysOauthClientDetailsEntity> queryPage(SysOauthClientDetailsListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysOauthClientDetailsEntity queryById(SysOauthClientDetailsId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysOauthClientDetailsId> ids) {
        return repository.removeBatchByIds(ids);
    }
}