package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysAuditLogQuery;
import com.kava.kbpd.upms.api.model.request.SysAuditLogRequest;
import com.kava.kbpd.upms.api.model.response.SysAuditLogListResponse;
import com.kava.kbpd.upms.api.model.response.SysAuditLogResponse;
import com.kava.kbpd.upms.domain.model.entity.SysAuditLogEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: ysAuditLog转换器
 */
@Mapper(componentModel = "spring")
public interface SysAuditLogAdapterConverter {

    SysAuditLogListQuery convertQueryDTO2QueryVal(SysAuditLogQuery request);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "tenantId.id", target = "tenantId")
    SysAuditLogListResponse convertEntity2List(SysAuditLogEntity entity);

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "tenantId", target = "tenantId.id")
    SysAuditLogEntity convertRequest2Entity(SysAuditLogRequest req);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "tenantId.id", target = "tenantId")
    SysAuditLogResponse convertEntity2Detail(SysAuditLogEntity sysAuditLog);
}
