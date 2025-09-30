package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysFileCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysFileUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysFileAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysFileAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysFileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysFile转换器
 */
@Mapper(componentModel = "spring")
public interface SysFileAppConverter {

//    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysFileEntity convertCreateCommand2Entity(SysFileCreateCommand command);

    SysFileEntity convertUpdateCommand2Entity(SysFileUpdateCommand command);

    SysFileAppListDTO convertEntityToListQueryDTO(SysFileEntity entity);

    SysFileAppDetailDTO convertEntityToDetailDTO(SysFileEntity entity);

}
