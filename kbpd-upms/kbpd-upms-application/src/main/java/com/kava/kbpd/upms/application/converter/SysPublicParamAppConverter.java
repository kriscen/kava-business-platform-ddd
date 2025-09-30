package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysPublicParamCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysPublicParamUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysPublicParamAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysPublicParamAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysPublicParamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysPublicParam转换器
 */
@Mapper(componentModel = "spring")
public interface SysPublicParamAppConverter {

//    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysPublicParamEntity convertCreateCommand2Entity(SysPublicParamCreateCommand command);

    SysPublicParamEntity convertUpdateCommand2Entity(SysPublicParamUpdateCommand command);

    SysPublicParamAppListDTO convertEntityToListQueryDTO(SysPublicParamEntity entity);

    SysPublicParamAppDetailDTO convertEntityToDetailDTO(SysPublicParamEntity entity);

}
