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

//    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysMenuEntity convertCreateCommand2Entity(SysMenuCreateCommand command);

    SysMenuEntity convertUpdateCommand2Entity(SysMenuUpdateCommand command);

    SysMenuAppListDTO convertEntityToListQueryDTO(SysMenuEntity entity);

    SysMenuAppDetailDTO convertEntityToDetailDTO(SysMenuEntity entity);

}
