package com.kava.kbpd.upms.domain.basic.repository;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysTenantEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysTenantListQuery;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysTenantId;

import java.util.List;

public interface ISysTenantRepository {
    SysTenantId create(SysTenantEntity entity);

    Boolean update(SysTenantEntity entity);

    PagingInfo<SysTenantEntity> queryPage(SysTenantListQuery query);

    SysTenantEntity queryById(SysTenantId id);

    Boolean removeBatchByIds(List<SysTenantId> ids);
}