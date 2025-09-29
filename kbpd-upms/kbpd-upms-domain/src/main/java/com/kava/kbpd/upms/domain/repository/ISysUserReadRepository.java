package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseReadRepository;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;

public interface ISysUserReadRepository extends IBaseReadRepository<SysUserId, SysUserEntity> {

    PagingInfo<SysUserEntity> queryPage(SysUserListQuery query);
}