package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.model.entity.SysAuditLogEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysAuditLogPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysAuditLog转换器
 */
@Mapper(componentModel = "spring")
public interface SysAuditLogConverter {

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "tenantId", target = "tenantId.id")
    SysAuditLogEntity convertPO2Entity(SysAuditLogPO sysAuditLogPO);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "tenantId.id", target = "tenantId")
    SysAuditLogPO convertEntity2PO(SysAuditLogEntity entity);
}
