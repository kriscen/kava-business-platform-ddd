package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.upms.domain.model.entity.SysAuditLogEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogId;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogListQuery;

public interface ISysAuditLogRepository extends IBaseSimpleRepository<SysAuditLogId, SysAuditLogEntity, SysAuditLogListQuery> {

}
