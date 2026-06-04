package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.model.command.SysGroupCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysGroupUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysGroupAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysGroupAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupId;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: group application service
 */
public interface ISysGroupAppService {
    SysGroupId createGroup(SysGroupCreateCommand command);

    void updateGroup(SysGroupUpdateCommand command);

    void removeGroupBatchByIds(List<SysGroupId> ids);

    PagingInfo<SysGroupAppListDTO> queryGroupPage(SysGroupListQuery query);

    SysGroupAppDetailDTO queryGroupById(SysGroupId id);

    List<SysGroupAppListDTO> queryGroupTree();

}
