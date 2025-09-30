package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.model.command.SysRoleCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysRoleUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/30
 * @description: role application service
 */
public interface ISysRoleAppService {
    SysRoleId createRole(SysRoleCreateCommand command);

    void updateRole(SysRoleUpdateCommand command);

    void removeRoleBatchByIds(List<SysRoleId> ids);

    PagingInfo<SysRoleAppListDTO> queryRolePage(SysRoleListQuery query);

    SysRoleAppDetailDTO queryRoleById(SysRoleId id);
}
