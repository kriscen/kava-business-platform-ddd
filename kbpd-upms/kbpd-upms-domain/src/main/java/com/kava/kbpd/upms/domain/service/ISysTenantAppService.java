package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.entity.SysTenantAppEntity;
import com.kava.kbpd.common.core.model.valobj.SysAppId;

import java.util.List;

public interface ISysTenantAppService {
    SysTenantAppEntity subscribe(SysTenantId tenantId, SysAppId appId);

    Boolean unsubscribe(SysTenantId tenantId, SysAppId appId);

    List<SysTenantAppEntity> queryByTenantId(SysTenantId tenantId);

    List<Long> queryMenuIdsByTenantId(SysTenantId tenantId);
}
