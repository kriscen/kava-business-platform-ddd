package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.upms.application.model.dto.TenantAppListDTO;

import java.util.List;

public interface ISysTenantAppAppService {
    void subscribe(Long tenantId, Long appId);

    void unsubscribe(Long tenantId, Long appId);

    List<TenantAppListDTO> queryByTenantId(Long tenantId);
}
