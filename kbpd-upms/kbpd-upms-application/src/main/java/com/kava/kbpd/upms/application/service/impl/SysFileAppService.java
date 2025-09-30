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
import com.kava.kbpd.upms.domain.repository.ISysFileRepository;
import com.kava.kbpd.upms.domain.service.ISysFileService;
import jakarta.annotation.Resource;
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
public class SysFileAppService implements ISysFileAppService {
    @Resource
    private ISysFileRepository sysFileRepository;

    @Resource
    private ISysFileService sysFileService;

    @Resource
    private SysFileAppConverter sysFileAppConverter;

    @Override
    public SysFileId createFile(SysFileCreateCommand command) {
        SysFileEntity sysFileEntity = sysFileAppConverter.convertCreateCommand2Entity(command);
        return sysFileRepository.create(sysFileEntity);
    }

    @Override
    public void updateFile(SysFileUpdateCommand command) {
        SysFileEntity sysFileEntity = sysFileAppConverter.convertUpdateCommand2Entity(command);
        sysFileRepository.update(sysFileEntity);
    }

    @Override
    public void removeFileBatchByIds(List<SysFileId> ids) {
        sysFileRepository.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysFileAppListDTO> queryFilePage(SysFileListQuery query) {
        PagingInfo<SysFileEntity> sysFileEntityPagingInfo = sysFileRepository.queryPage(query);
        List<SysFileAppListDTO> collect = sysFileEntityPagingInfo.getList().stream().map(sysFileEntity -> sysFileAppConverter.convertEntityToListQueryDTO(sysFileEntity)).toList();
        return PagingInfo.toResponse(collect, sysFileEntityPagingInfo);
    }

    @Override
    public SysFileAppDetailDTO queryFileById(SysFileId id) {
        SysFileEntity FileEntity = sysFileRepository.queryById(id);
        return sysFileAppConverter.convertEntityToDetailDTO(FileEntity);
    }

}
