package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.model.entity.SysOauthClientDetailsEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysOauthClientDetailsPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysOauthClientDetails转换器
 */
@Mapper(componentModel = "spring")
public interface SysOauthClientDetailsConverter {

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "tenantId", target = "tenantId.id")
    SysOauthClientDetailsEntity convertPO2Entity(SysOauthClientDetailsPO sysOauthClientDetailsPO);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "tenantId.id", target = "tenantId")
    SysOauthClientDetailsPO convertEntity2PO(SysOauthClientDetailsEntity entity);
}