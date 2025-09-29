package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysFileEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysFileId;
import com.kava.kbpd.upms.domain.model.valobj.SysFileListQuery;
import com.kava.kbpd.upms.domain.repository.ISysFileRepository;
import com.kava.kbpd.upms.domain.service.ISysFileService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysFileService implements ISysFileService {
    @Resource
    private ISysFileRepository repository;

    @Override
    public SysFileId create(SysFileEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysFileEntity entity) {
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysFileEntity> queryPage(SysFileListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysFileEntity queryById(SysFileId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysFileId> ids) {
        return repository.removeBatchByIds(ids);
    }
}