package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseReadRepository;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;

public interface ISysRoleReadRepository extends IBaseReadRepository<SysRoleId, SysRoleEntity, SysRoleListQuery> {

    SysRoleEntity queryByRoleCode(String roleCode, SysTenantId tenantId);
}
