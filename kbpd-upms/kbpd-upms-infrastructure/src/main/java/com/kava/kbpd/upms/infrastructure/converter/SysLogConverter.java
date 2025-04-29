package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.basic.model.entity.SysLogEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysLogPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysLog转换器
 */
@Mapper(componentModel = "spring")
public interface SysLogConverter {

    @Mapping(source = "id", target = "id.id")
    SysLogEntity convertPO2Entity(SysLogPO sysLogPO);

    @Mapping(source = "id.id", target = "id")
    SysLogPO convertEntity2PO(SysLogEntity entity);
}