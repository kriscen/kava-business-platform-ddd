package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysGroupCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysGroupUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysGroupAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysGroupAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysGroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysGroup转换器
 */
@Mapper(componentModel = "spring")
public interface SysGroupAppConverter {

    @Mapping(source = "pid", target = "pid.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    SysGroupEntity convertCreateCommand2Entity(SysGroupCreateCommand command);

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "pid", target = "pid.id")
    @Mapping(target = "tenantId", ignore = true)
    SysGroupEntity convertUpdateCommand2Entity(SysGroupUpdateCommand command);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    SysGroupAppListDTO convertEntityToListQueryDTO(SysGroupEntity entity);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    SysGroupAppDetailDTO convertEntityToDetailDTO(SysGroupEntity entity);
}
