package com.kava.kbpd.upms.domain.basic.repository;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysAuditLogEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAuditLogListQuery;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAuditLogId;

import java.util.List;

public interface ISysAuditLogRepository {
    SysAuditLogId create(SysAuditLogEntity entity);

    Boolean update(SysAuditLogEntity entity);

    PagingInfo<SysAuditLogEntity> queryPage(SysAuditLogListQuery query);

    SysAuditLogEntity queryById(SysAuditLogId id);

    Boolean removeBatchByIds(List<SysAuditLogId> ids);
}
