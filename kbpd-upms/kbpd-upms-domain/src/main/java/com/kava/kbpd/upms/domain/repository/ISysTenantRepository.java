package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantListQuery;

public interface ISysTenantRepository extends IBaseSimpleRepository<SysTenantId, SysTenantEntity, SysTenantListQuery> {

}