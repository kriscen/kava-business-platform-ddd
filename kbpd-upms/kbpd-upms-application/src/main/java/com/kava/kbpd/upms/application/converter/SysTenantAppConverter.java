package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysTenantCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysTenantUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysTenantAppConverter {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    SysTenantEntity convertCreateCommand2Entity(SysTenantCreateCommand command);

    SysTenantEntity convertUpdateCommand2Entity(SysTenantUpdateCommand command);

    @Mapping(source = "id.id", target = "id")
    SysTenantAppListDTO convertEntityToListQueryDTO(SysTenantEntity entity);

    @Mapping(source = "id.id", target = "id")
    SysTenantAppDetailDTO convertEntityToDetailDTO(SysTenantEntity entity);
}
