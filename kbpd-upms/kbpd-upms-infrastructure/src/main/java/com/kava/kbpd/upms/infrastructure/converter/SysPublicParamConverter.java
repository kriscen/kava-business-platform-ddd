package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.model.entity.SysPublicParamEntity;
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
    @Mapping(source = "tenantId", target = "tenantId.id")
    SysPublicParamEntity convertPO2Entity(SysPublicParamPO sysPublicParamPO);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "tenantId.id", target = "tenantId")
    SysPublicParamPO convertEntity2PO(SysPublicParamEntity entity);
}