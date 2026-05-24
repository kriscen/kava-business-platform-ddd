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

    /**
     * 查询指定父节点的直接子菜单
     */
    List<SysMenuEntity> queryByPid(SysMenuId pid);

    /**
     * 检查菜单是否被角色引用
     */
    boolean existsRoleReference(SysMenuId menuId);
}