package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysI18nEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nId;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nListQuery;
import com.kava.kbpd.upms.domain.repository.ISysI18nRepository;
import com.kava.kbpd.upms.domain.service.ISysI18nService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysI18nService implements ISysI18nService {
    @Resource
    private ISysI18nRepository repository;

    @Override
    public SysI18nId create(SysI18nEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysI18nEntity entity) {
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysI18nEntity> queryPage(SysI18nListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysI18nEntity queryById(SysI18nId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysI18nId> ids) {
        return repository.removeBatchByIds(ids);
    }
}