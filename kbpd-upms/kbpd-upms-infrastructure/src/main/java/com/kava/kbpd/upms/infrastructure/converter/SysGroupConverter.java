package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.model.entity.SysGroupEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysGroupPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysGroup转换器
 */
@Mapper(componentModel = "spring")
public interface SysGroupConverter {

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "pid", target = "pid.id")
    @Mapping(source = "tenantId", target = "tenantId.id")
    SysGroupEntity convertPO2Entity(SysGroupPO sysGroupPO);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    @Mapping(source = "tenantId.id", target = "tenantId")
    SysGroupPO convertEntity2PO(SysGroupEntity entity);
}
