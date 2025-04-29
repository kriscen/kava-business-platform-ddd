package com.kava.kbpd.upms.domain.basic.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysFileGroupEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysFileGroupId;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysFileGroupListQuery;
import com.kava.kbpd.upms.domain.basic.repository.ISysFileGroupRepository;
import com.kava.kbpd.upms.domain.basic.service.ISysFileGroupService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysFileGroupService implements ISysFileGroupService {
    @Resource
    private ISysFileGroupRepository repository;

    @Override
    public SysFileGroupId create(SysFileGroupEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysFileGroupEntity entity) {
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysFileGroupEntity> queryPage(SysFileGroupListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysFileGroupEntity queryById(SysFileGroupId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysFileGroupId> ids) {
        return repository.removeBatchByIds(ids);
    }
}