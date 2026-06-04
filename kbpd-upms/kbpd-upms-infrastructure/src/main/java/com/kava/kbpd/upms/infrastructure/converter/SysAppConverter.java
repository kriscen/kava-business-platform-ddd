package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.model.entity.SysAppEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysAppPO;
import com.kava.kbpd.upms.types.enums.SysAppStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysAppConverter {

    @Mapping(source = "id", target = "id.id")
    @Mapping(target = "status", expression = "java(mapStatus(sysAppPO.getStatus()))")
    SysAppEntity convertPO2Entity(SysAppPO sysAppPO);

    @Mapping(source = "id.id", target = "id")
    @Mapping(target = "status", expression = "java(mapCode(entity.getStatus()))")
    SysAppPO convertEntity2PO(SysAppEntity entity);

    default SysAppStatus mapStatus(String code) {
        if (code == null) {
            return null;
        }
        for (SysAppStatus s : SysAppStatus.values()) {
            if (s.getCode().equals(code)) {
                return s;
            }
        }
        return null;
    }

    default String mapCode(SysAppStatus status) {
        return status != null ? status.getCode() : null;
    }
}
