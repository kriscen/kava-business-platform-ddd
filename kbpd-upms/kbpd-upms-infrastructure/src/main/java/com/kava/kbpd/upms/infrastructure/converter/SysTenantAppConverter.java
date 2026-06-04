package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.model.entity.SysTenantAppEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysTenantAppPO;
import com.kava.kbpd.upms.types.enums.SysTenantAppStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysTenantAppConverter {

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "tenantId", target = "tenantId.id")
    @Mapping(source = "appId", target = "appId.id")
    @Mapping(target = "status", expression = "java(mapStatus(sysTenantAppPO.getStatus()))")
    SysTenantAppEntity convertPO2Entity(SysTenantAppPO sysTenantAppPO);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "tenantId.id", target = "tenantId")
    @Mapping(source = "appId.id", target = "appId")
    @Mapping(target = "status", expression = "java(mapCode(entity.getStatus()))")
    SysTenantAppPO convertEntity2PO(SysTenantAppEntity entity);

    default SysTenantAppStatus mapStatus(String code) {
        if (code == null) {
            return null;
        }
        for (SysTenantAppStatus s : SysTenantAppStatus.values()) {
            if (s.getCode().equals(code)) {
                return s;
            }
        }
        return null;
    }

    default String mapCode(SysTenantAppStatus status) {
        return status != null ? status.getCode() : null;
    }
}
