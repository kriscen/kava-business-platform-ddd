package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.upms.domain.model.entity.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;

public interface ISysRoleRepository extends IBaseSimpleRepository<SysRoleId, SysRoleEntity, SysRoleListQuery> {

}