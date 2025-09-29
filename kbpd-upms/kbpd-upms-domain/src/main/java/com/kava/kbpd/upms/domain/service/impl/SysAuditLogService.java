package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysAuditLogEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogId;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogListQuery;
import com.kava.kbpd.upms.domain.repository.ISysAuditLogRepository;
import com.kava.kbpd.upms.domain.service.ISysAuditLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysAuditLogService implements ISysAuditLogService {
    @Resource
    private ISysAuditLogRepository repository;

    @Override
    public SysAuditLogId create(SysAuditLogEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysAuditLogEntity entity) {
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysAuditLogEntity> queryPage(SysAuditLogListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysAuditLogEntity queryById(SysAuditLogId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysAuditLogId> ids) {
        return repository.removeBatchByIds(ids);
    }
}
