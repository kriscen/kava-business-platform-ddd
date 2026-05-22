package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.converter.SysFileAppConverter;
import com.kava.kbpd.upms.application.model.command.SysFileCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysFileUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysFileAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysFileAppListDTO;
import com.kava.kbpd.upms.application.service.ISysFileAppService;
import com.kava.kbpd.upms.domain.model.entity.SysFileEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysFileId;
import com.kava.kbpd.upms.domain.model.valobj.SysFileListQuery;
import com.kava.kbpd.upms.domain.service.ISysFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: File application service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysFileAppService implements ISysFileAppService {
    private final ISysFileService sysFileService;
    private final SysFileAppConverter sysFileAppConverter;

    @Override
    public SysFileId createFile(SysFileCreateCommand command) {
        SysFileEntity sysFileEntity = sysFileAppConverter.convertCreateCommand2Entity(command);
        return sysFileService.create(sysFileEntity);
    }

    @Override
    public void updateFile(SysFileUpdateCommand command) {
        SysFileEntity sysFileEntity = sysFileAppConverter.convertUpdateCommand2Entity(command);
        sysFileService.update(sysFileEntity);
    }

    @Override
    public void removeFileBatchByIds(List<SysFileId> ids) {
        sysFileService.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysFileAppListDTO> queryFilePage(SysFileListQuery query) {
        PagingInfo<SysFileEntity> sysFileEntityPagingInfo = sysFileService.queryPage(query);
        List<SysFileAppListDTO> collect = sysFileEntityPagingInfo.getList().stream().map(sysFileEntity -> sysFileAppConverter.convertEntityToListQueryDTO(sysFileEntity)).toList();
        return PagingInfo.toResponse(collect, sysFileEntityPagingInfo);
    }

    @Override
    public SysFileAppDetailDTO queryFileById(SysFileId id) {
        SysFileEntity FileEntity = sysFileService.queryById(id);
        return sysFileAppConverter.convertEntityToDetailDTO(FileEntity);
    }

}
