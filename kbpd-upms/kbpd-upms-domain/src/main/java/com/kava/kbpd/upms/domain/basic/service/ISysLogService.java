package com.kava.kbpd.upms.domain.basic.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysLogEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysLogId;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysLogListQuery;

import java.util.List;

public interface ISysLogService {
    SysLogId create(SysLogEntity entity);

    Boolean update(SysLogEntity entity);

    PagingInfo<SysLogEntity> queryPage(SysLogListQuery query);

    SysLogEntity queryById(SysLogId id);

    Boolean removeBatchByIds(List<SysLogId> ids);
}