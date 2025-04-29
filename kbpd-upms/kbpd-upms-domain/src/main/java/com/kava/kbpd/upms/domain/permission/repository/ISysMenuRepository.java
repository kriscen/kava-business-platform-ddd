package com.kava.kbpd.upms.domain.permission.repository;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.permission.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysMenuListQuery;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysMenuId;

import java.util.List;

public interface ISysMenuRepository {
    SysMenuId create(SysMenuEntity entity);

    Boolean update(SysMenuEntity entity);

    PagingInfo<SysMenuEntity> queryPage(SysMenuListQuery query);

    SysMenuEntity queryById(SysMenuId id);

    Boolean removeBatchByIds(List<SysMenuId> ids);
}