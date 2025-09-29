package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.model.entity.SysI18nEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysI18nPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysI18n转换器
 */
@Mapper(componentModel = "spring")
public interface SysI18nConverter {

    @Mapping(source = "id", target = "id.id")
    SysI18nEntity convertPO2Entity(SysI18nPO sysI18nPO);

    @Mapping(source = "id.id", target = "id")
    SysI18nPO convertEntity2PO(SysI18nEntity entity);
}