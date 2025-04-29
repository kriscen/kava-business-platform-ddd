package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.user.model.entity.SysUserEntity;
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
    SysUserEntity convertPO2Entity(SysUserPO sysUserPO);

    @Mapping(source = "id.id", target = "id")
    SysUserPO convertEntity2PO(SysUserEntity entity);
}