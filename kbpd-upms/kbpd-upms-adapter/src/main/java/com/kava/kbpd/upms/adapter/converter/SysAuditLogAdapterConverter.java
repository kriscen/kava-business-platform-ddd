package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysAuditLogAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysAuditLogRequest;
import com.kava.kbpd.upms.api.model.response.SysAuditLogDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysAuditLogListResponse;
import com.kava.kbpd.upms.application.model.command.SysAuditLogCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysAuditLogUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysAuditLogAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAuditLogAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: sysAuditLog转换器
 */
@Mapper(componentModel = "spring")
public interface SysAuditLogAdapterConverter {

    SysAuditLogListQuery convertQueryDTO2QueryVal(SysAuditLogAdapterListQuery request);

    SysAuditLogListResponse convertEntity2List(SysAuditLogAppListDTO request);

    SysAuditLogDetailResponse convertEntity2Detail(SysAuditLogAppDetailDTO request);

    SysAuditLogCreateCommand convertRequest2CreateCommand(SysAuditLogRequest request);

    SysAuditLogUpdateCommand convertRequest2UpdateCommand(SysAuditLogRequest request);
}
