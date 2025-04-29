package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.basic.model.entity.SysRouteConfEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysRouteConfPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysRouteConf转换器
 */
@Mapper(componentModel = "spring")
public interface SysRouteConfConverter {

    @Mapping(source = "id", target = "id.id")
    SysRouteConfEntity convertPO2Entity(SysRouteConfPO sysRouteConfPO);

    @Mapping(source = "id.id", target = "id")
    SysRouteConfPO convertEntity2PO(SysRouteConfEntity entity);
}