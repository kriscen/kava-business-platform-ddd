package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysOauthClientEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientId;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientListQuery;
import com.kava.kbpd.upms.domain.repository.ISysOauthClientRepository;
import com.kava.kbpd.upms.domain.service.ISysOauthClientService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysOauthClientService implements ISysOauthClientService {
    @Resource
    private ISysOauthClientRepository repository;

    @Override
    public SysOauthClientId create(SysOauthClientEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysOauthClientEntity entity) {
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysOauthClientEntity> queryPage(SysOauthClientListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysOauthClientEntity queryById(SysOauthClientId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysOauthClientId> ids) {
        return repository.removeBatchByIds(ids);
    }
}