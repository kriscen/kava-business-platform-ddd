package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantListQuery;
import com.kava.kbpd.upms.domain.repository.ISysTenantRepository;
import com.kava.kbpd.upms.domain.service.ISysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysTenantService implements ISysTenantService {
    private final ISysTenantRepository repository;

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
