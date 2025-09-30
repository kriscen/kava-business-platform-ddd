package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.model.command.SysAuditLogCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysAuditLogUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysAuditLogAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAuditLogAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogId;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: audit log application service
 */
public interface ISysAuditLogAppService {
    SysAuditLogId createAuditLog(SysAuditLogCreateCommand command);

    void updateAuditLog(SysAuditLogUpdateCommand command);

    void removeAuditLogBatchByIds(List<SysAuditLogId> ids);

    PagingInfo<SysAuditLogAppListDTO> queryAuditLogPage(SysAuditLogListQuery query);

    SysAuditLogAppDetailDTO queryAuditLogById(SysAuditLogId id);

}
