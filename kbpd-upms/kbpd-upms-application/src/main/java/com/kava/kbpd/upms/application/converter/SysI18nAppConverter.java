package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysI18nCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysI18nUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysI18nAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysI18nAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysI18nEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysI18n转换器
 */
@Mapper(componentModel = "spring")
public interface SysI18nAppConverter {

//    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysI18nEntity convertCreateCommand2Entity(SysI18nCreateCommand command);

    SysI18nEntity convertUpdateCommand2Entity(SysI18nUpdateCommand command);

    SysI18nAppListDTO convertEntityToListQueryDTO(SysI18nEntity entity);

    SysI18nAppDetailDTO convertEntityToDetailDTO(SysI18nEntity entity);

}
