package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysMenuPO;
import com.kava.kbpd.upms.types.enums.SysMenuLevel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysMenuConverter {

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "tenantId", target = "tenantId.id")
    @Mapping(target = "level", expression = "java(mapLevel(sysMenuPO.getLevel()))")
    SysMenuEntity convertPO2Entity(SysMenuPO sysMenuPO);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "tenantId.id", target = "tenantId")
    @Mapping(target = "level", expression = "java(mapLevelCode(entity.getLevel()))")
    SysMenuPO convertEntity2PO(SysMenuEntity entity);

    default SysMenuLevel mapLevel(String code) {
        if (code == null) {
            return null;
        }
        for (SysMenuLevel l : SysMenuLevel.values()) {
            if (l.getCode().equals(code)) {
                return l;
            }
        }
        return null;
    }

    default String mapLevelCode(SysMenuLevel level) {
        return level != null ? level.getCode() : null;
    }
}
