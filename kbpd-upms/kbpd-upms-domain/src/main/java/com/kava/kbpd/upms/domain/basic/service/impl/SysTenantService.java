package com.kava.kbpd.upms.domain.basic.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysTenantEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysTenantListQuery;
import com.kava.kbpd.upms.domain.basic.repository.ISysTenantRepository;
import com.kava.kbpd.upms.domain.basic.service.ISysTenantService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysTenantService implements ISysTenantService {
    @Resource
    private ISysTenantRepository repository;

    @Override
    public SysTenantId create(SysTenantEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysTenantEntity entity) {
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysTenantEntity> queryPage(SysTenantListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysTenantEntity queryById(SysTenantId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysTenantId> ids) {
        return repository.removeBatchByIds(ids);
    }
}