package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.infrastructure.dao.po.SysAppMenuPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysAppMenuConverter {

    @Mapping(source = "appId", target = "appId")
    @Mapping(source = "menuId", target = "menuId")
    SysAppMenuPO convert(Long appId, Long menuId);
}
