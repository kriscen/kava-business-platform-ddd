package com.kava.kbpd.upms.domain.basic.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysLogEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysLogId;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysLogListQuery;
import com.kava.kbpd.upms.domain.basic.repository.ISysLogRepository;
import com.kava.kbpd.upms.domain.basic.service.ISysLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLogService implements ISysLogService {
    @Resource
    private ISysLogRepository repository;

    @Override
    public SysLogId create(SysLogEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysLogEntity entity) {
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysLogEntity> queryPage(SysLogListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysLogEntity queryById(SysLogId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysLogId> ids) {
        return repository.removeBatchByIds(ids);
    }
}