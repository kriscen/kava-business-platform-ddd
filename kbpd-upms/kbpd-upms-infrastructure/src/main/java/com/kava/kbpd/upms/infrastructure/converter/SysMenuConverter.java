package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.permission.model.entity.SysMenuEntity;
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
    SysMenuEntity convertPO2Entity(SysMenuPO sysMenuPO);

    @Mapping(source = "id.id", target = "id")
    SysMenuPO convertEntity2PO(SysMenuEntity entity);
}