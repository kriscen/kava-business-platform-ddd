package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.model.command.SysFileCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysFileUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysFileAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysFileAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysFileId;
import com.kava.kbpd.upms.domain.model.valobj.SysFileListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: file application service
 */
public interface ISysFileAppService {
    SysFileId createFile(SysFileCreateCommand command);

    void updateFile(SysFileUpdateCommand command);

    void removeFileBatchByIds(List<SysFileId> ids);

    PagingInfo<SysFileAppListDTO> queryFilePage(SysFileListQuery query);

    SysFileAppDetailDTO queryFileById(SysFileId id);

}
