package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.basic.model.entity.SysAreaEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysAreaPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysArea转换器
 */
@Mapper(componentModel = "spring")
public interface SysAreaConverter {

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    SysAreaPO convertEntity2PO(SysAreaEntity request);

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "pid", target = "pid.id")
    SysAreaEntity convertPO2Entity(SysAreaPO request);
}
