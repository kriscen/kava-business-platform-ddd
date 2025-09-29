package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuListQuery;

import java.util.List;

public interface ISysMenuService {
    SysMenuId create(SysMenuEntity entity);

    Boolean update(SysMenuEntity entity);

    PagingInfo<SysMenuEntity> queryPage(SysMenuListQuery query);

    SysMenuEntity queryById(SysMenuId id);

    Boolean removeBatchByIds(List<SysMenuId> ids);
}