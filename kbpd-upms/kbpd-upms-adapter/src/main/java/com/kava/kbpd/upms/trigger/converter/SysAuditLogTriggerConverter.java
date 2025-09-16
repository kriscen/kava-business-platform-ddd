package com.kava.kbpd.upms.trigger.converter;

import com.kava.kbpd.upms.api.model.query.SysAuditLogQuery;
import com.kava.kbpd.upms.api.model.request.SysAuditLogRequest;
import com.kava.kbpd.upms.api.model.response.SysAuditLogListResponse;
import com.kava.kbpd.upms.api.model.response.SysAuditLogResponse;
import com.kava.kbpd.upms.domain.basic.model.entity.SysAuditLogEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAuditLogListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: ysAuditLog转换器
 */
@Mapper(componentModel = "spring")
public interface SysAuditLogTriggerConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysAuditLogListQuery convertQueryDTO2QueryVal(SysAuditLogQuery request);

    @Mapping(source = "id.id", target = "id")
    SysAuditLogListResponse convertEntity2List(SysAuditLogEntity entity);

    @Mapping(source = "id", target = "id.id")
    SysAuditLogEntity convertRequest2Entity(SysAuditLogRequest req);

    @Mapping(source = "id.id", target = "id")
    SysAuditLogResponse convertEntity2Detail(SysAuditLogEntity sysAuditLog);
}
