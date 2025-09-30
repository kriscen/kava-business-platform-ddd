package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysFileGroupCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysFileGroupUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysFileGroupAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysFileGroupAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysFileGroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysFileGroup转换器
 */
@Mapper(componentModel = "spring")
public interface SysFileGroupAppConverter {

//    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysFileGroupEntity convertCreateCommand2Entity(SysFileGroupCreateCommand command);

    SysFileGroupEntity convertUpdateCommand2Entity(SysFileGroupUpdateCommand command);

    SysFileGroupAppListDTO convertEntityToListQueryDTO(SysFileGroupEntity entity);

    SysFileGroupAppDetailDTO convertEntityToDetailDTO(SysFileGroupEntity entity);

}
