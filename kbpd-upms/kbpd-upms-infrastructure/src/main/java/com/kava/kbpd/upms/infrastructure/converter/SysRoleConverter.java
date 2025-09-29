package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysRolePO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysRole转换器
 */
@Mapper(componentModel = "spring")
public interface SysRoleConverter {

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "tenantId", target = "tenantId.id")
    SysRoleEntity convertPO2Entity(SysRolePO sysRolePO);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "tenantId.id", target = "tenantId")
    SysRolePO convertEntity2PO(SysRoleEntity entity);
}