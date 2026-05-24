package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantListQuery;
import com.kava.kbpd.upms.types.enums.SysTenantStatus;

import java.util.List;

public interface ISysTenantService {
    SysTenantId create(SysTenantEntity entity);

    Boolean update(SysTenantEntity entity);

    PagingInfo<SysTenantEntity> queryPage(SysTenantListQuery query);

    SysTenantEntity queryById(SysTenantId id);

    Boolean removeBatchByIds(List<SysTenantId> ids);

    void enable(SysTenantId id);

    void disable(SysTenantId id);

    SysTenantStatus queryEffectiveStatus(SysTenantId id);

    List<SysTenantEntity> queryAll();
}