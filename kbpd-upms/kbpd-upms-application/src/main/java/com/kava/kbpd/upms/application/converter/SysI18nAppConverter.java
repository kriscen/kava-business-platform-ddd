package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysI18nCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysI18nUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysI18nAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysI18nAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysI18nMessageEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nMessageId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysI18nMessage转换器
 */
@Mapper(componentModel = "spring")
public interface SysI18nAppConverter {

    @Mapping(target = "id", ignore = true)
    SysI18nMessageEntity convertCreateCommand2Entity(SysI18nCreateCommand command);

    @Mapping(source = "id", target = "id")
    SysI18nMessageEntity convertUpdateCommand2Entity(SysI18nUpdateCommand command);

    @Mapping(source = "id.id", target = "id")
    SysI18nAppListDTO convertEntityToListQueryDTO(SysI18nMessageEntity entity);

    @Mapping(source = "id.id", target = "id")
    SysI18nAppDetailDTO convertEntityToDetailDTO(SysI18nMessageEntity entity);

    default SysI18nMessageId map(Long id) {
        return SysI18nMessageId.of(id);
    }
}
