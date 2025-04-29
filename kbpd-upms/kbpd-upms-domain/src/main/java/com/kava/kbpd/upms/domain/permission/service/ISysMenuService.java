package com.kava.kbpd.upms.domain.permission.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.permission.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysMenuListQuery;

import java.util.List;

public interface ISysMenuService {
    SysMenuId create(SysMenuEntity entity);

    Boolean update(SysMenuEntity entity);

    PagingInfo<SysMenuEntity> queryPage(SysMenuListQuery query);

    SysMenuEntity queryById(SysMenuId id);

    Boolean removeBatchByIds(List<SysMenuId> ids);
}