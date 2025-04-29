package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.user.model.entity.SysDeptEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysDeptPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysDept转换器
 */
@Mapper(componentModel = "spring")
public interface SysDeptConverter {

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "pid", target = "pid.id")
    SysDeptEntity convertPO2Entity(SysDeptPO sysDeptPO);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    SysDeptPO convertEntity2PO(SysDeptEntity entity);
}