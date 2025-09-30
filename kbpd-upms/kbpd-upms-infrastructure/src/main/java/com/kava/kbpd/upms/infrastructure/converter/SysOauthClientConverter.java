package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.model.entity.SysOauthClientEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysOauthClientPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysOauthClientDetails转换器
 */
@Mapper(componentModel = "spring")
public interface SysOauthClientConverter {

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "tenantId", target = "tenantId.id")
    SysOauthClientEntity convertPO2Entity(SysOauthClientPO sysOauthClientDetailsPO);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "tenantId.id", target = "tenantId")
    SysOauthClientPO convertEntity2PO(SysOauthClientEntity entity);
}