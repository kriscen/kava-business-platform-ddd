package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.upms.domain.model.entity.SysLogEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysLogId;
import com.kava.kbpd.upms.domain.model.valobj.SysLogListQuery;

public interface ISysLogRepository extends IBaseSimpleRepository<SysLogId, SysLogEntity, SysLogListQuery> {

}