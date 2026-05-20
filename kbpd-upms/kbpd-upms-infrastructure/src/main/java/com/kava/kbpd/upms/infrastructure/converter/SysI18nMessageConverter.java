package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.model.entity.SysI18nMessage;
import com.kava.kbpd.upms.infrastructure.dao.po.SysI18nMessagePO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysI18nMessage转换器
 */
@Mapper(componentModel = "spring")
public interface SysI18nMessageConverter {

    @Mapping(source = "id", target = "id.id")
    SysI18nMessage convertPO2Entity(SysI18nMessagePO po);

    @Mapping(source = "id.id", target = "id")
    SysI18nMessagePO convertEntity2PO(SysI18nMessage entity);
}
