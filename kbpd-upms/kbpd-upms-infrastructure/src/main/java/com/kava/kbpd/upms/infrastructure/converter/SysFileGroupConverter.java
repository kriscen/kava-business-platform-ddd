package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.basic.model.entity.SysFileGroupEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysFileGroupPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysFileGroup转换器
 */
@Mapper(componentModel = "spring")
public interface SysFileGroupConverter {

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "pid", target = "pid.id")
    SysFileGroupEntity convertPO2Entity(SysFileGroupPO sysFileGroupPO);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    SysFileGroupPO convertEntity2PO(SysFileGroupEntity entity);
}