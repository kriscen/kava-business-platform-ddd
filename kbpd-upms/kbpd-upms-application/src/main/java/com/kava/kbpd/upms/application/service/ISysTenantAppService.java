package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.application.model.command.SysTenantCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysTenantUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: Tenant application service
 */
public interface ISysTenantAppService {
    SysTenantId createTenant(SysTenantCreateCommand command);

    void updateTenant(SysTenantUpdateCommand command);

    void removeTenantBatchByIds(List<SysTenantId> ids);

    PagingInfo<SysTenantAppListDTO> queryTenantPage(SysTenantListQuery query);

    SysTenantAppDetailDTO queryTenantById(SysTenantId id);

}
