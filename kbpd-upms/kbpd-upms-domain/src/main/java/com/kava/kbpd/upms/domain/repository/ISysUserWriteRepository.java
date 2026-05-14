package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseWriteRepository;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;

import java.util.List;

public interface ISysUserWriteRepository extends IBaseWriteRepository<SysUserId, SysUserEntity> {

    /**
     * 批量保存用户-角色关联
     */
    void saveUserRoles(SysUserId userId, List<SysRoleId> roleIds);

    /**
     * 删除用户的所有角色关联
     */
    void removeUserRoles(SysUserId userId);
}