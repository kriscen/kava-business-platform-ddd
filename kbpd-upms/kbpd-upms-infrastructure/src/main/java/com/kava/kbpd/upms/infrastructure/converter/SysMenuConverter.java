package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysMenuPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysMenu转换器
 */
@Mapper(componentModel = "spring")
public interface SysMenuConverter {

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "tenantId", target = "tenantId.id")
    SysMenuEntity convertPO2Entity(SysMenuPO sysMenuPO);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "tenantId.id", target = "tenantId")
    SysMenuPO convertEntity2PO(SysMenuEntity entity);
}