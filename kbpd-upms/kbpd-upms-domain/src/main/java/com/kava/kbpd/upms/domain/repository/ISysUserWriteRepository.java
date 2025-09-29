package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseWriteRepository;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;

public interface ISysUserWriteRepository extends IBaseWriteRepository<SysUserId, SysUserEntity> {

}