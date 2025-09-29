package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysAuditLogEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogId;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogListQuery;

import java.util.List;

public interface ISysAuditLogService {
    SysAuditLogId create(SysAuditLogEntity entity);

    Boolean update(SysAuditLogEntity entity);

    PagingInfo<SysAuditLogEntity> queryPage(SysAuditLogListQuery query);

    SysAuditLogEntity queryById(SysAuditLogId id);

    Boolean removeBatchByIds(List<SysAuditLogId> ids);
}
