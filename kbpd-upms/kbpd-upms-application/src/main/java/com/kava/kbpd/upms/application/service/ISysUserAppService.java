package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.application.model.command.SysUserCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysUserUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysUserAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysUserAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: user application service
 */
public interface ISysUserAppService {
    SysUserId createUser(SysUserCreateCommand command);

    void updateUser(SysUserUpdateCommand command);

    void removeUserBatchByIds(List<SysUserId> ids);

    PagingInfo<SysUserAppListDTO> queryUserPage(SysUserListQuery query);

    SysUserAppDetailDTO queryUserById(SysUserId id);

    /**
     * 根据租户ID和用户名查询用户详情
     */
    SysUserAppDetailDTO queryUserByUsername(Long tenantId, String username);

    /**
     * 根据用户ID查询角色标识列表
     */
    List<String> queryRoleCodesByUserId(Long userId);

    /**
     * 根据用户ID查询权限标识列表
     */
    List<String> queryPermissionsByUserId(Long userId);
}
