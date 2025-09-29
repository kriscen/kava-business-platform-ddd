package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.application.model.command.SysAreaCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysAreaUpdateCommand;
import com.kava.kbpd.upms.application.model.command.SysUserCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysUserUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysAreaAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAreaListQueryDTO;
import com.kava.kbpd.upms.application.model.dto.SysUserAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysUserListQueryDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaId;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaListQuery;
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

    PagingInfo<SysUserListQueryDTO> queryUserPage(SysUserListQuery query);

    SysUserAppDetailDTO queryUserById(SysUserId id);
}
