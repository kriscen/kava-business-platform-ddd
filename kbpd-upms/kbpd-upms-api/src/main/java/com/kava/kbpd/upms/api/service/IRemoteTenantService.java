package com.kava.kbpd.upms.api.service;

import com.kava.kbpd.upms.api.model.dto.TenantStatusDTO;

public interface IRemoteTenantService {
    TenantStatusDTO checkTenantStatus(Long tenantId);
}
