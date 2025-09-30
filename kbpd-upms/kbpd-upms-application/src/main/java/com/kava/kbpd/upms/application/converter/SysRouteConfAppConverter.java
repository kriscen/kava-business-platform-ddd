package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysRouteConfCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysRouteConfUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysRouteConfAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysRouteConfAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysRouteConfEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysRouteConf转换器
 */
@Mapper(componentModel = "spring")
public interface SysRouteConfAppConverter {

//    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysRouteConfEntity convertCreateCommand2Entity(SysRouteConfCreateCommand command);

    SysRouteConfEntity convertUpdateCommand2Entity(SysRouteConfUpdateCommand command);

    SysRouteConfAppListDTO convertEntityToListQueryDTO(SysRouteConfEntity entity);

    SysRouteConfAppDetailDTO convertEntityToDetailDTO(SysRouteConfEntity entity);

}
