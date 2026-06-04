package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysMenuCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysMenuUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysMenuAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysMenuAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.types.enums.SysMenuLevel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysMenuAppConverter {

    @Mapping(source = "pid", target = "pid.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "level", expression = "java(mapLevel(command.getLevel()))")
    SysMenuEntity convertCreateCommand2Entity(SysMenuCreateCommand command);

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "pid", target = "pid.id")
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "level", expression = "java(mapLevel(command.getLevel()))")
    SysMenuEntity convertUpdateCommand2Entity(SysMenuUpdateCommand command);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "parentId")
    @Mapping(source = "tenantId.id", target = "tenantId")
    @Mapping(target = "level", expression = "java(mapLevelCode(entity.getLevel()))")
    SysMenuAppListDTO convertEntityToListQueryDTO(SysMenuEntity entity);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    @Mapping(source = "tenantId.id", target = "tenantId")
    @Mapping(target = "level", expression = "java(mapLevelCode(entity.getLevel()))")
    SysMenuAppDetailDTO convertEntityToDetailDTO(SysMenuEntity entity);

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
