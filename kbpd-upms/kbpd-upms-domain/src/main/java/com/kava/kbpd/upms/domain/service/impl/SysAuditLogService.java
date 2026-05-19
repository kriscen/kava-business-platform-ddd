package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysAuditLogEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogId;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogListQuery;
import com.kava.kbpd.upms.domain.service.ISysAuditLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysAuditLogService implements ISysAuditLogService {

    @Override
    public SysAuditLogId create(SysAuditLogEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean update(SysAuditLogEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public PagingInfo<SysAuditLogEntity> queryPage(SysAuditLogListQuery query) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public SysAuditLogEntity queryById(SysAuditLogId id) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean removeBatchByIds(List<SysAuditLogId> ids) {
        throw new UnsupportedOperationException("暂未实现");
    }
}
