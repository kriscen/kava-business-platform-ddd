package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysLogEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysLogId;
import com.kava.kbpd.upms.domain.model.valobj.SysLogListQuery;

import java.util.List;

public interface ISysLogService {
    SysLogId create(SysLogEntity entity);

    Boolean update(SysLogEntity entity);

    PagingInfo<SysLogEntity> queryPage(SysLogListQuery query);

    SysLogEntity queryById(SysLogId id);

    Boolean removeBatchByIds(List<SysLogId> ids);
}