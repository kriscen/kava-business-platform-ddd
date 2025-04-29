package com.kava.kbpd.upms.domain.basic.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysPublicParamEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysPublicParamId;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysPublicParamListQuery;
import com.kava.kbpd.upms.domain.basic.repository.ISysPublicParamRepository;
import com.kava.kbpd.upms.domain.basic.service.ISysPublicParamService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPublicParamService implements ISysPublicParamService {
    @Resource
    private ISysPublicParamRepository repository;

    @Override
    public SysPublicParamId create(SysPublicParamEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysPublicParamEntity entity) {
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysPublicParamEntity> queryPage(SysPublicParamListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysPublicParamEntity queryById(SysPublicParamId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysPublicParamId> ids) {
        return repository.removeBatchByIds(ids);
    }
}