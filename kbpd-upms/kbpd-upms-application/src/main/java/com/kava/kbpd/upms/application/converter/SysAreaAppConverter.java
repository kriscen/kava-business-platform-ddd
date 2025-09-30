package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysAreaCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysAreaUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysAreaAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAreaAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysAreaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysArea转换器
 */
@Mapper(componentModel = "spring")
public interface SysAreaAppConverter {

//    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysAreaEntity convertCreateCommand2Entity(SysAreaCreateCommand command);

    SysAreaEntity convertUpdateCommand2Entity(SysAreaUpdateCommand command);

    SysAreaAppListDTO convertEntityToListQueryDTO(SysAreaEntity entity);

    @Mapping(source = "pid.id", target = "pid")
    SysAreaAppDetailDTO convertEntityToDetailDTO(SysAreaEntity entity);

}
