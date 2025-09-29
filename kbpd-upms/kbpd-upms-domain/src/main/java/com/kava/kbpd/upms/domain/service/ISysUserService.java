package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysUserEntity;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;

import java.util.List;

public interface ISysUserService {
    SysUserId create(SysUserEntity entity);

    Boolean update(SysUserEntity entity);

    PagingInfo<SysUserEntity> queryPage(SysUserListQuery query);

    SysUserEntity queryById(SysUserId id);

    Boolean removeBatchByIds(List<SysUserId> ids);
}