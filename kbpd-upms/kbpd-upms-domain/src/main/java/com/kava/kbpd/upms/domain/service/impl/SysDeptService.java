package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysDeptEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptId;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptListQuery;
import com.kava.kbpd.upms.domain.repository.ISysDeptRepository;
import com.kava.kbpd.upms.domain.service.ISysDeptService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDeptService implements ISysDeptService {
    @Resource
    private ISysDeptRepository repository;

    @Override
    public SysDeptId create(SysDeptEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysDeptEntity entity) {
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysDeptEntity> queryPage(SysDeptListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysDeptEntity queryById(SysDeptId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysDeptId> ids) {
        return repository.removeBatchByIds(ids);
    }
}