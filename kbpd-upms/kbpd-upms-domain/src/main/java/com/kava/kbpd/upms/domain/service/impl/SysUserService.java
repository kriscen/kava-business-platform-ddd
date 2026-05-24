package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;
import com.kava.kbpd.upms.domain.repository.ISysUserReadRepository;
import com.kava.kbpd.upms.domain.repository.ISysUserWriteRepository;
import com.kava.kbpd.upms.domain.service.ISysUserService;
import com.kava.kbpd.upms.types.exception.UpmsBizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import static com.kava.kbpd.upms.types.exception.UpmsBizErrorCodeEnum.USER_USERNAME_DUPLICATE;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserService implements ISysUserService {
    private final ISysUserWriteRepository writeRepository;
    private final ISysUserReadRepository readRepository;

    @Override
    public SysUserId create(SysUserEntity entity) {
        validateUsernameUnique(entity.getTenantId().getId(), entity.getUsername(), null);
        SysUserId userId = writeRepository.create(entity);
        if (!CollectionUtils.isEmpty(entity.getRoleIds())) {
            writeRepository.saveUserRoles(userId, entity.getRoleIds());
        }
        return userId;
    }

    @Override
    public Boolean update(SysUserEntity entity) {
        SysUserEntity existing = readRepository.queryById(entity.getId());
        if (existing != null && !existing.getUsername().equals(entity.getUsername())) {
            validateUsernameUnique(entity.getTenantId().getId(), entity.getUsername(), entity.getId());
        }
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

    private void validateUsernameUnique(Long tenantId, String username, SysUserId excludeId) {
        SysUserEntity existing = readRepository.queryByUsername(tenantId, username);
        if (existing != null && (excludeId == null || !existing.getId().equals(excludeId))) {
            throw new UpmsBizException(USER_USERNAME_DUPLICATE);
        }
    }
}
