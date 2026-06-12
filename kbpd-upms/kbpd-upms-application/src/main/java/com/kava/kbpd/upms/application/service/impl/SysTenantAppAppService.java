package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.application.model.dto.TenantAppListDTO;
import com.kava.kbpd.upms.application.service.ISysTenantAppAppService;
import com.kava.kbpd.upms.domain.model.entity.SysAppEntity;
import com.kava.kbpd.upms.domain.model.entity.SysTenantAppEntity;
import com.kava.kbpd.common.core.model.valobj.SysAppId;
import com.kava.kbpd.upms.domain.repository.ISysAppRepository;
import com.kava.kbpd.upms.domain.service.ISysTenantAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysTenantAppAppService implements ISysTenantAppAppService {
    private final ISysTenantAppService sysTenantAppService;
    private final ISysAppRepository sysAppRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void subscribe(Long tenantId, Long appId) {
        sysTenantAppService.subscribe(
                SysTenantId.builder().id(tenantId).build(),
                SysAppId.builder().id(appId).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unsubscribe(Long tenantId, Long appId) {
        sysTenantAppService.unsubscribe(
                SysTenantId.builder().id(tenantId).build(),
                SysAppId.builder().id(appId).build());
    }

    @Override
    public List<TenantAppListDTO> queryByTenantId(Long tenantId) {
        List<SysTenantAppEntity> entities = sysTenantAppService.queryByTenantId(
                SysTenantId.builder().id(tenantId).build());
        return entities.stream().map(this::convertToListDTO).toList();
    }

    private TenantAppListDTO convertToListDTO(SysTenantAppEntity entity) {
        TenantAppListDTO dto = new TenantAppListDTO();
        dto.setId(entity.getId() != null ? entity.getId().getId() : null);
        dto.setTenantId(entity.getTenantId().getId());
        dto.setAppId(entity.getAppId().getId());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().getCode() : null);
        dto.setGmtCreate(entity.getGmtCreate());

        SysAppEntity app = sysAppRepository.queryById(entity.getAppId());
        if (app != null) {
            dto.setAppCode(app.getCode());
            dto.setAppName(app.getName());
            dto.setAppIcon(app.getIcon());
        }
        return dto;
    }
}
