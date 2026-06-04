package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.entity.SysAppEntity;
import com.kava.kbpd.upms.domain.model.entity.SysTenantAppEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAppId;
import com.kava.kbpd.upms.domain.repository.ISysAppRepository;
import com.kava.kbpd.upms.domain.repository.ISysTenantAppRepository;
import com.kava.kbpd.upms.domain.service.ISysTenantAppService;
import com.kava.kbpd.upms.types.enums.SysAppStatus;
import com.kava.kbpd.upms.types.enums.SysTenantAppStatus;
import com.kava.kbpd.upms.types.exception.UpmsBizErrorCodeEnum;
import com.kava.kbpd.upms.types.exception.UpmsBizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysTenantAppService implements ISysTenantAppService {
    private static final String KAVA_BASE_CODE = "kava-base";

    private final ISysTenantAppRepository tenantAppRepository;
    private final ISysAppRepository appRepository;

    @Override
    public SysTenantAppEntity subscribe(SysTenantId tenantId, SysAppId appId) {
        SysAppEntity app = appRepository.queryById(appId);
        if (app == null) {
            throw new UpmsBizException(UpmsBizErrorCodeEnum.APP_NOT_FOUND);
        }
        if (app.getStatus() == SysAppStatus.DISABLED) {
            throw new UpmsBizException(UpmsBizErrorCodeEnum.APP_DISABLED);
        }
        if (tenantAppRepository.existsActiveSubscription(tenantId, appId)) {
            throw new UpmsBizException(UpmsBizErrorCodeEnum.TENANT_APP_ALREADY_SUBSCRIBED);
        }

        SysTenantAppEntity entity = SysTenantAppEntity.builder()
                .tenantId(tenantId)
                .appId(appId)
                .status(SysTenantAppStatus.ACTIVE)
                .build();
        tenantAppRepository.create(entity);
        return entity;
    }

    @Override
    public Boolean unsubscribe(SysTenantId tenantId, SysAppId appId) {
        SysAppEntity app = appRepository.queryById(appId);
        if (app != null && KAVA_BASE_CODE.equals(app.getCode())) {
            throw new UpmsBizException(UpmsBizErrorCodeEnum.TENANT_APP_NOT_UNSUBSCRIBABLE);
        }

        SysTenantAppEntity entity = tenantAppRepository.queryByTenantIdAndAppId(tenantId, appId);
        if (entity == null) {
            throw new UpmsBizException(UpmsBizErrorCodeEnum.APP_NOT_FOUND);
        }
        entity.setStatus(SysTenantAppStatus.EXPIRED);
        return tenantAppRepository.update(entity);
    }

    @Override
    public List<SysTenantAppEntity> queryByTenantId(SysTenantId tenantId) {
        return tenantAppRepository.queryByTenantId(tenantId);
    }

    @Override
    public List<Long> queryMenuIdsByTenantId(SysTenantId tenantId) {
        return tenantAppRepository.queryMenuIdsByTenantId(tenantId);
    }
}
