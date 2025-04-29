package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.permission.model.entity.SysRoleEntity;
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
    SysRoleEntity convertPO2Entity(SysRolePO sysRolePO);

    @Mapping(source = "id.id", target = "id")
    SysRolePO convertEntity2PO(SysRoleEntity entity);
}