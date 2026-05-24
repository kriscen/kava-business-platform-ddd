package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysMenuCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysMenuUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysMenuAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysMenuAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysMenuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysMenu转换器
 */
@Mapper(componentModel = "spring")
public interface SysMenuAppConverter {

    @Mapping(source = "pid", target = "pid.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "scope", ignore = true)
    SysMenuEntity convertCreateCommand2Entity(SysMenuCreateCommand command);

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "pid", target = "pid.id")
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "scope", ignore = true)
    SysMenuEntity convertUpdateCommand2Entity(SysMenuUpdateCommand command);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "parentId")
    @Mapping(source = "tenantId.id", target = "tenantId")
    SysMenuAppListDTO convertEntityToListQueryDTO(SysMenuEntity entity);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    @Mapping(source = "tenantId.id", target = "tenantId")
    SysMenuAppDetailDTO convertEntityToDetailDTO(SysMenuEntity entity);

}
