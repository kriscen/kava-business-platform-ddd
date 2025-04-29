package com.kava.kbpd.upms.domain.user.repository;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.user.model.entity.SysUserEntity;
import com.kava.kbpd.upms.domain.user.model.valobj.SysUserListQuery;
import com.kava.kbpd.upms.domain.user.model.valobj.SysUserId;

import java.util.List;

public interface ISysUserRepository {
    SysUserId create(SysUserEntity entity);

    Boolean update(SysUserEntity entity);

    PagingInfo<SysUserEntity> queryPage(SysUserListQuery query);

    SysUserEntity queryById(SysUserId id);

    Boolean removeBatchByIds(List<SysUserId> ids);
}