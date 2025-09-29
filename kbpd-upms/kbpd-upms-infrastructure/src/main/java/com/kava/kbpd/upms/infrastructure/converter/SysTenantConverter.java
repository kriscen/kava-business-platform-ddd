package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysTenantPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysTenant转换器
 */
@Mapper(componentModel = "spring")
public interface SysTenantConverter {

    @Mapping(source = "id", target = "id.id")
    SysTenantEntity convertPO2Entity(SysTenantPO sysTenantPO);

    @Mapping(source = "id.id", target = "id")
    SysTenantPO convertEntity2PO(SysTenantEntity entity);
}