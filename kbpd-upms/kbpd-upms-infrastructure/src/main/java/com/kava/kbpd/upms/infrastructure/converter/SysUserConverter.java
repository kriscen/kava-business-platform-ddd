package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysUserPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysUser转换器
 */
@Mapper(componentModel = "spring")
public interface SysUserConverter {

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "tenantId", target = "tenantId.id")
    @Mapping(source = "groupId", target = "groupId.id")
    SysUserEntity convertPO2Entity(SysUserPO sysUserPO);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "tenantId.id", target = "tenantId")
    @Mapping(source = "groupId.id", target = "groupId")
    SysUserPO convertEntity2PO(SysUserEntity entity);
}