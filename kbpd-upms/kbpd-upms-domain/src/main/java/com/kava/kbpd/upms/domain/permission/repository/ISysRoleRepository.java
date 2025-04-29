package com.kava.kbpd.upms.domain.permission.repository;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.permission.model.entity.SysRoleEntity;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysRoleListQuery;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysRoleId;

import java.util.List;

public interface ISysRoleRepository {
    SysRoleId create(SysRoleEntity entity);

    Boolean update(SysRoleEntity entity);

    PagingInfo<SysRoleEntity> queryPage(SysRoleListQuery query);

    SysRoleEntity queryById(SysRoleId id);

    Boolean removeBatchByIds(List<SysRoleId> ids);
}