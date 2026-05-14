package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.upms.domain.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuListQuery;

import java.util.List;

public interface ISysMenuRepository extends IBaseSimpleRepository<SysMenuId, SysMenuEntity, SysMenuListQuery> {

    /**
     * 查询所有菜单
     */
    List<SysMenuEntity> queryAll();

    /**
     * 根据菜单ID列表批量查询
     */
    List<SysMenuEntity> queryByIds(List<SysMenuId> ids);
}