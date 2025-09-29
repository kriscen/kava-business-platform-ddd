package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseWriteRepository;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;

public interface ISysRoleWriteRepository extends IBaseWriteRepository<SysRoleId, SysRoleEntity> {

}