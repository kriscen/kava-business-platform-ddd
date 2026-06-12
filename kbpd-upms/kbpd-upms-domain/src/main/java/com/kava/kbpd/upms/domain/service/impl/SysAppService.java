package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysAppEntity;
import com.kava.kbpd.common.core.model.valobj.SysAppId;
import com.kava.kbpd.upms.domain.model.valobj.SysAppListQuery;
import com.kava.kbpd.upms.domain.repository.ISysAppRepository;
import com.kava.kbpd.upms.domain.service.ISysAppService;
import com.kava.kbpd.upms.types.enums.SysAppStatus;
import com.kava.kbpd.upms.types.exception.UpmsBizErrorCodeEnum;
import com.kava.kbpd.upms.types.exception.UpmsBizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysAppService implements ISysAppService {
    private static final String KAVA_BASE_CODE = "kava-base";

    private final ISysAppRepository repository;

    @Override
    public SysAppId create(SysAppEntity entity) {
        validateCodeUnique(entity.getCode(), null);
        entity.setStatus(SysAppStatus.ACTIVE);
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysAppEntity entity) {
        validateCodeUnique(entity.getCode(), entity.getId());
        validateKavaBaseProtection(entity);
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysAppEntity> queryPage(SysAppListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysAppEntity queryById(SysAppId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysAppId> ids) {
        for (SysAppId id : ids) {
            SysAppEntity entity = repository.queryById(id);
            if (entity != null) {
                if (KAVA_BASE_CODE.equals(entity.getCode())) {
                    throw new UpmsBizException(UpmsBizErrorCodeEnum.APP_NOT_DELETABLE);
                }
                if (repository.existsActiveTenantApp(id)) {
                    throw new UpmsBizException(UpmsBizErrorCodeEnum.APP_IN_USE);
                }
            }
        }
        return repository.removeBatchByIds(ids);
    }

    @Override
    public List<SysAppEntity> queryAll() {
        return repository.queryAll();
    }

    private void validateCodeUnique(String code, SysAppId excludeId) {
        SysAppEntity existing = repository.queryByCode(code);
        if (existing != null && (excludeId == null || !existing.getId().equals(excludeId))) {
            throw new UpmsBizException(UpmsBizErrorCodeEnum.APP_CODE_DUPLICATE);
        }
    }

    private void validateKavaBaseProtection(SysAppEntity entity) {
        if (KAVA_BASE_CODE.equals(entity.getCode()) && entity.getStatus() == SysAppStatus.DISABLED) {
            throw new UpmsBizException(UpmsBizErrorCodeEnum.APP_NOT_DISABLEABLE);
        }
    }
}
