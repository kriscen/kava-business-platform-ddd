package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;
import com.kava.kbpd.upms.domain.repository.ISysRoleReadRepository;
import com.kava.kbpd.upms.domain.repository.ISysRoleWriteRepository;
import com.kava.kbpd.upms.domain.service.ISysRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleService implements ISysRoleService {
    @Resource
    private ISysRoleWriteRepository writeRepository;

    @Resource
    private ISysRoleReadRepository readRepository;

    @Override
    public SysRoleId create(SysRoleEntity entity) {
        return writeRepository.create(entity);
    }

    @Override
    public Boolean update(SysRoleEntity entity) {
        return writeRepository.update(entity);
    }

    @Override
    public PagingInfo<SysRoleEntity> queryPage(SysRoleListQuery query) {
        return null;
    }

    @Override
    public SysRoleEntity queryById(SysRoleId id) {
        return readRepository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysRoleId> ids) {
        return writeRepository.removeBatchByIds(ids);
    }
}