package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.basic.model.entity.SysPublicParamEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysPublicParamPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysPublicParam转换器
 */
@Mapper(componentModel = "spring")
public interface SysPublicParamConverter {

    @Mapping(source = "id", target = "id.id")
    SysPublicParamEntity convertPO2Entity(SysPublicParamPO sysPublicParamPO);

    @Mapping(source = "id.id", target = "id")
    SysPublicParamPO convertEntity2PO(SysPublicParamEntity entity);
}