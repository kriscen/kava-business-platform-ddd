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

    @Mapping(source = "pid", target = "pid.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    SysDeptEntity convertCreateCommand2Entity(SysDeptCreateCommand command);

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "pid", target = "pid.id")
    @Mapping(target = "tenantId", ignore = true)
    SysDeptEntity convertUpdateCommand2Entity(SysDeptUpdateCommand command);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    SysDeptAppListDTO convertEntityToListQueryDTO(SysDeptEntity entity);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    SysDeptAppDetailDTO convertEntityToDetailDTO(SysDeptEntity entity);
}
