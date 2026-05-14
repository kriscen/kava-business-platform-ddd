package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;
import com.kava.kbpd.upms.domain.repository.ISysUserReadRepository;
import com.kava.kbpd.upms.domain.repository.ISysUserWriteRepository;
import com.kava.kbpd.upms.domain.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SysUserService implements ISysUserService {
    @Resource
    private ISysUserWriteRepository writeRepository;

    @Resource
    private ISysUserReadRepository readRepository;

    @Override
    public SysUserId create(SysUserEntity entity) {
        SysUserId userId = writeRepository.create(entity);
        if (!CollectionUtils.isEmpty(entity.getRoleIds())) {
            writeRepository.saveUserRoles(userId, entity.getRoleIds());
        }
        return userId;
    }

    @Override
    public Boolean update(SysUserEntity entity) {
        writeRepository.removeUserRoles(entity.getId());
        if (!CollectionUtils.isEmpty(entity.getRoleIds())) {
            writeRepository.saveUserRoles(entity.getId(), entity.getRoleIds());
        }
        return writeRepository.update(entity);
    }

    @Override
    public PagingInfo<SysUserEntity> queryPage(SysUserListQuery query) {
        return readRepository.queryPage(query);
    }

    @Override
    public SysUserEntity queryById(SysUserId id) {
        return readRepository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysUserId> ids) {
        for (SysUserId userId : ids) {
            writeRepository.removeUserRoles(userId);
        }
        return writeRepository.removeBatchByIds(ids);
    }
}
