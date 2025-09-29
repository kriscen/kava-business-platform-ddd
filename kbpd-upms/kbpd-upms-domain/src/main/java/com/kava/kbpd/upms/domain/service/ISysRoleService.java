package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;

import java.util.List;

public interface ISysRoleService {
    SysRoleId create(SysRoleEntity entity);

    Boolean update(SysRoleEntity entity);

    PagingInfo<SysRoleEntity> queryPage(SysRoleListQuery query);

    SysRoleEntity queryById(SysRoleId id);

    Boolean removeBatchByIds(List<SysRoleId> ids);
}