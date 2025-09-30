package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysOauthClientCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysOauthClientUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysOauthClientAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysOauthClientAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysOauthClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysOauthClient转换器
 */
@Mapper(componentModel = "spring")
public interface SysOauthClientAppConverter {

//    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysOauthClientEntity convertCreateCommand2Entity(SysOauthClientCreateCommand command);

    SysOauthClientEntity convertUpdateCommand2Entity(SysOauthClientUpdateCommand command);

    SysOauthClientAppListDTO convertEntityToListQueryDTO(SysOauthClientEntity entity);

    SysOauthClientAppDetailDTO convertEntityToDetailDTO(SysOauthClientEntity entity);

}
