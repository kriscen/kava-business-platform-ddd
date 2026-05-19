package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysLogEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysLogId;
import com.kava.kbpd.upms.domain.model.valobj.SysLogListQuery;
import com.kava.kbpd.upms.domain.service.ISysLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLogService implements ISysLogService {

    @Override
    public SysLogId create(SysLogEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean update(SysLogEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public PagingInfo<SysLogEntity> queryPage(SysLogListQuery query) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public SysLogEntity queryById(SysLogId id) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean removeBatchByIds(List<SysLogId> ids) {
        throw new UnsupportedOperationException("暂未实现");
    }
}
