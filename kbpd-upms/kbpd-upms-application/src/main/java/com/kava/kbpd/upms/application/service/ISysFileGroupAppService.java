package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.model.command.SysFileGroupCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysFileGroupUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysFileGroupAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysFileGroupAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysFileGroupId;
import com.kava.kbpd.upms.domain.model.valobj.SysFileGroupListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: FileGroup application service
 */
public interface ISysFileGroupAppService {
    SysFileGroupId createFileGroup(SysFileGroupCreateCommand command);

    void updateFileGroup(SysFileGroupUpdateCommand command);

    void removeFileGroupBatchByIds(List<SysFileGroupId> ids);

    PagingInfo<SysFileGroupAppListDTO> queryFileGroupPage(SysFileGroupListQuery query);

    SysFileGroupAppDetailDTO queryFileGroupById(SysFileGroupId id);

}
