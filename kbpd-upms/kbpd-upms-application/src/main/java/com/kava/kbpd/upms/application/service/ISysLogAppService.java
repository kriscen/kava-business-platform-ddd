package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.model.command.SysLogCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysLogUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysLogAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysLogAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysLogId;
import com.kava.kbpd.upms.domain.model.valobj.SysLogListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: Log application service
 */
public interface ISysLogAppService {
    SysLogId createLog(SysLogCreateCommand command);

    void updateLog(SysLogUpdateCommand command);

    void removeLogBatchByIds(List<SysLogId> ids);

    PagingInfo<SysLogAppListDTO> queryLogPage(SysLogListQuery query);

    SysLogAppDetailDTO queryLogById(SysLogId id);

}
