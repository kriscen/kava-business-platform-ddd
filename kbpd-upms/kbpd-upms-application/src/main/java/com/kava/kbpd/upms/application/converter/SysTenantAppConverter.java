package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysTenantCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysTenantUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysTenant转换器
 */
@Mapper(componentModel = "spring")
public interface SysTenantAppConverter {

//    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysTenantEntity convertCreateCommand2Entity(SysTenantCreateCommand command);

    SysTenantEntity convertUpdateCommand2Entity(SysTenantUpdateCommand command);

    SysTenantAppListDTO convertEntityToListQueryDTO(SysTenantEntity entity);

    SysTenantAppDetailDTO convertEntityToDetailDTO(SysTenantEntity entity);

}
