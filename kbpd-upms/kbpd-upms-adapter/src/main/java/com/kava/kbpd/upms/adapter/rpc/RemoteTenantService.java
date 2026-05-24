package com.kava.kbpd.upms.adapter.rpc;

import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.api.model.dto.TenantStatusDTO;
import com.kava.kbpd.upms.api.service.IRemoteTenantService;
import com.kava.kbpd.upms.application.model.dto.TenantStatusAppDTO;
import com.kava.kbpd.upms.application.service.ISysTenantAppService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

@Slf4j
@DubboService(version = "1.0")
public class RemoteTenantService implements IRemoteTenantService {
    @Resource
    private ISysTenantAppService sysTenantAppService;

    @Override
    public TenantStatusDTO checkTenantStatus(Long tenantId) {
        TenantStatusAppDTO appDTO = sysTenantAppService.checkTenantStatus(SysTenantId.of(tenantId));
        if (appDTO == null) {
            return null;
        }
        TenantStatusDTO dto = new TenantStatusDTO();
        dto.setStatus(appDTO.getStatus());
        dto.setExpired(appDTO.getExpired());
        return dto;
    }
}
