package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.types.exception.UpmsBizException;
import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantListQuery;
import com.kava.kbpd.upms.domain.repository.ISysTenantRepository;
import com.kava.kbpd.upms.domain.service.ISysTenantService;
import com.kava.kbpd.upms.types.enums.SysTenantStatus;
import com.kava.kbpd.upms.types.exception.UpmsBizErrorCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysTenantService implements ISysTenantService {
    private final ISysTenantRepository repository;

    @Override
    public SysTenantId create(SysTenantEntity entity) {
        validateCodeUnique(entity.getCode(), null);
        entity.setStatus(SysTenantStatus.NORMAL);
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysTenantEntity entity) {
        validateCodeUnique(entity.getCode(), entity.getId());
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

    @Override
    public void enable(SysTenantId id) {
        SysTenantEntity entity = getOrThrow(id);
        if (entity.getStatus() == SysTenantStatus.NORMAL) {
            throw new UpmsBizException(UpmsBizErrorCodeEnum.TENANT_STATUS_INVALID_TRANSITION);
        }
        entity.setStatus(SysTenantStatus.NORMAL);
        repository.update(entity);
    }

    @Override
    public void disable(SysTenantId id) {
        SysTenantEntity entity = getOrThrow(id);
        if (entity.getStatus() == SysTenantStatus.DISABLED) {
            throw new UpmsBizException(UpmsBizErrorCodeEnum.TENANT_STATUS_INVALID_TRANSITION);
        }
        entity.setStatus(SysTenantStatus.DISABLED);
        repository.update(entity);
    }

    @Override
    public SysTenantStatus queryEffectiveStatus(SysTenantId id) {
        SysTenantEntity entity = getOrThrow(id);
        return entity.isExpired() ? SysTenantStatus.DISABLED : entity.getStatus();
    }

    private void validateCodeUnique(String code, SysTenantId excludeId) {
        SysTenantEntity existing = repository.queryByCode(code);
        if (existing != null && (excludeId == null || !existing.getId().equals(excludeId))) {
            throw new UpmsBizException(UpmsBizErrorCodeEnum.TENANT_CODE_DUPLICATE);
        }
    }

    private SysTenantEntity getOrThrow(SysTenantId id) {
        SysTenantEntity entity = repository.queryById(id);
        if (entity == null) {
            throw new UpmsBizException(UpmsBizErrorCodeEnum.TENANT_NOT_FOUND);
        }
        return entity;
    }
}
