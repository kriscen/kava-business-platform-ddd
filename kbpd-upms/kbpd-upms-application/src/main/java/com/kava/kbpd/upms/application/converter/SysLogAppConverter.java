package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysLogCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysLogUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysLogAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysLogAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysLog转换器
 */
@Mapper(componentModel = "spring")
public interface SysLogAppConverter {

//    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysLogEntity convertCreateCommand2Entity(SysLogCreateCommand command);

    SysLogEntity convertUpdateCommand2Entity(SysLogUpdateCommand command);

    SysLogAppListDTO convertEntityToListQueryDTO(SysLogEntity entity);

    SysLogAppDetailDTO convertEntityToDetailDTO(SysLogEntity entity);

}
