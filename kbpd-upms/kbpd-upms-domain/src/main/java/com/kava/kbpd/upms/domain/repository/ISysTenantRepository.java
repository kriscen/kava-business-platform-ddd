package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantListQuery;

import java.util.List;

public interface ISysTenantRepository extends IBaseSimpleRepository<SysTenantId, SysTenantEntity, SysTenantListQuery> {
    SysTenantEntity queryByCode(String code);

    List<SysTenantEntity> queryAll();

    List<SysTenantEntity> queryByIds(List<SysTenantId> ids);
}