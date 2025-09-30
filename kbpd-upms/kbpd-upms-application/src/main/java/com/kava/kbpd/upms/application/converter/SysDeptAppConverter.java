package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysDeptCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysDeptUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysDeptAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysDeptAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysDeptEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysDept转换器
 */
@Mapper(componentModel = "spring")
public interface SysDeptAppConverter {

//    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysDeptEntity convertCreateCommand2Entity(SysDeptCreateCommand command);

    SysDeptEntity convertUpdateCommand2Entity(SysDeptUpdateCommand command);

    SysDeptAppListDTO convertEntityToListQueryDTO(SysDeptEntity entity);

    SysDeptAppDetailDTO convertEntityToDetailDTO(SysDeptEntity entity);

}
