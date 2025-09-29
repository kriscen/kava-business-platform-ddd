package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysRouteConfEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfId;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfListQuery;
import com.kava.kbpd.upms.domain.repository.ISysRouteConfRepository;
import com.kava.kbpd.upms.domain.service.ISysRouteConfService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRouteConfService implements ISysRouteConfService {
    @Resource
    private ISysRouteConfRepository repository;

    @Override
    public SysRouteConfId create(SysRouteConfEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysRouteConfEntity entity) {
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysRouteConfEntity> queryPage(SysRouteConfListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysRouteConfEntity queryById(SysRouteConfId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysRouteConfId> ids) {
        return repository.removeBatchByIds(ids);
    }
}