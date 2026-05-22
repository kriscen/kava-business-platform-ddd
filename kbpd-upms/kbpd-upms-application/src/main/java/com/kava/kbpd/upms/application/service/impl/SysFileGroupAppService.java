package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.converter.SysFileGroupAppConverter;
import com.kava.kbpd.upms.application.model.command.SysFileGroupCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysFileGroupUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysFileGroupAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysFileGroupAppListDTO;
import com.kava.kbpd.upms.application.service.ISysFileGroupAppService;
import com.kava.kbpd.upms.domain.model.entity.SysFileGroupEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysFileGroupId;
import com.kava.kbpd.upms.domain.model.valobj.SysFileGroupListQuery;
import com.kava.kbpd.upms.domain.service.ISysFileGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: FileGroup application service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysFileGroupAppService implements ISysFileGroupAppService {
    private final ISysFileGroupService sysFileGroupService;
    private final SysFileGroupAppConverter sysFileGroupAppConverter;

    @Override
    public SysFileGroupId createFileGroup(SysFileGroupCreateCommand command) {
        SysFileGroupEntity sysFileGroupEntity = sysFileGroupAppConverter.convertCreateCommand2Entity(command);
        return sysFileGroupService.create(sysFileGroupEntity);
    }

    @Override
    public void updateFileGroup(SysFileGroupUpdateCommand command) {
        SysFileGroupEntity sysFileGroupEntity = sysFileGroupAppConverter.convertUpdateCommand2Entity(command);
        sysFileGroupService.update(sysFileGroupEntity);
    }

    @Override
    public void removeFileGroupBatchByIds(List<SysFileGroupId> ids) {
        sysFileGroupService.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysFileGroupAppListDTO> queryFileGroupPage(SysFileGroupListQuery query) {
        PagingInfo<SysFileGroupEntity> sysFileGroupEntityPagingInfo = sysFileGroupService.queryPage(query);
        List<SysFileGroupAppListDTO> collect = sysFileGroupEntityPagingInfo.getList().stream().map(sysFileGroupEntity -> sysFileGroupAppConverter.convertEntityToListQueryDTO(sysFileGroupEntity)).toList();
        return PagingInfo.toResponse(collect, sysFileGroupEntityPagingInfo);
    }

    @Override
    public SysFileGroupAppDetailDTO queryFileGroupById(SysFileGroupId id) {
        SysFileGroupEntity FileGroupEntity = sysFileGroupService.queryById(id);
        return sysFileGroupAppConverter.convertEntityToDetailDTO(FileGroupEntity);
    }

}
