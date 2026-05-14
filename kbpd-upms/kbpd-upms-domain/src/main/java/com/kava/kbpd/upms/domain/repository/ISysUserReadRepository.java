package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseReadRepository;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;

import java.util.List;

public interface ISysUserReadRepository extends IBaseReadRepository<SysUserId, SysUserEntity> {

    PagingInfo<SysUserEntity> queryPage(SysUserListQuery query);

    /**
     * 根据租户ID和用户名查询用户
     */
    SysUserEntity queryByUsername(Long tenantId, String username);

    /**
     * 根据用户ID查询角色标识列表
     */
    List<String> queryRoleCodesByUserId(Long userId);

    /**
     * 根据用户ID查询权限标识列表
     */
    List<String> queryPermissionsByUserId(Long userId);

    /**
     * 根据用户ID查询数据权限范围（取主角色的 dsType）
     */
    String queryDataScopeByUserId(Long userId);

    /**
     * 根据用户ID查询关联的菜单ID列表（通过 sys_user_role → sys_role_menu）
     */
    List<Long> queryMenuIdsByUserId(Long userId);
}