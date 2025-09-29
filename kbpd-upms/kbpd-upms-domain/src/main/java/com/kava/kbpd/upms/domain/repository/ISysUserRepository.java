package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.model.entity.SysUserEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;

public interface ISysUserRepository extends IBaseSimpleRepository<SysUserId, SysUserEntity, SysUserListQuery> {

}