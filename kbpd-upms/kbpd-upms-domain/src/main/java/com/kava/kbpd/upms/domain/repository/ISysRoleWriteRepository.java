package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseWriteRepository;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;

import java.util.List;

public interface ISysRoleWriteRepository extends IBaseWriteRepository<SysRoleId, SysRoleEntity> {

    /**
     * 批量保存角色-菜单关联
     */
    void saveRoleMenus(SysRoleId roleId, List<SysMenuId> menuIds);

    /**
     * 删除角色的所有菜单关联
     */
    void removeRoleMenus(SysRoleId roleId);

    /**
     * 删除角色关联的用户-角色关系
     */
    void removeUserRoleByRoleId(SysRoleId roleId);
}