package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysTenantPO;
import com.kava.kbpd.upms.types.enums.SysTenantStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysTenantConverter {

    @Mapping(source = "id", target = "id.id")
    @Mapping(target = "status", expression = "java(mapStatus(sysTenantPO.getStatus()))")
    SysTenantEntity convertPO2Entity(SysTenantPO sysTenantPO);

    @Mapping(source = "id.id", target = "id")
    @Mapping(target = "status", expression = "java(mapCode(entity.getStatus()))")
    SysTenantPO convertEntity2PO(SysTenantEntity entity);

    default SysTenantStatus mapStatus(String code) {
        if (code == null) {
            return null;
        }
        for (SysTenantStatus s : SysTenantStatus.values()) {
            if (s.getCode().equals(code)) {
                return s;
            }
        }
        return null;
    }

    default String mapCode(SysTenantStatus status) {
        return status != null ? status.getCode() : null;
    }
}
