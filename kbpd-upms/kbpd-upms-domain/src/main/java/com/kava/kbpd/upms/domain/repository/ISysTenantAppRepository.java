package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.entity.SysTenantAppEntity;
import com.kava.kbpd.common.core.model.valobj.SysAppId;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantAppId;

import java.util.List;

public interface ISysTenantAppRepository {

    SysTenantAppId create(SysTenantAppEntity entity);

    Boolean update(SysTenantAppEntity entity);

    List<SysTenantAppEntity> queryByTenantId(SysTenantId tenantId);

    SysTenantAppEntity queryByTenantIdAndAppId(SysTenantId tenantId, SysAppId appId);

    boolean existsActiveSubscription(SysTenantId tenantId, SysAppId appId);

    List<Long> queryMenuIdsByTenantId(SysTenantId tenantId);

    List<Long> queryMenuIdsByAppIds(List<Long> appIds);
}
