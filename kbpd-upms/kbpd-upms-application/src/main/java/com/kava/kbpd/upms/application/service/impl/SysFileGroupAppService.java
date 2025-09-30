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
import com.kava.kbpd.upms.domain.repository.ISysFileGroupRepository;
import com.kava.kbpd.upms.domain.service.ISysFileGroupService;
import jakarta.annotation.Resource;
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
public class SysFileGroupAppService implements ISysFileGroupAppService {
    @Resource
    private ISysFileGroupRepository sysFileGroupRepository;

    @Resource
    private ISysFileGroupService sysFileGroupService;

    @Resource
    private SysFileGroupAppConverter sysFileGroupAppConverter;

    @Override
    public SysFileGroupId createFileGroup(SysFileGroupCreateCommand command) {
        SysFileGroupEntity sysFileGroupEntity = sysFileGroupAppConverter.convertCreateCommand2Entity(command);
        return sysFileGroupRepository.create(sysFileGroupEntity);
    }

    @Override
    public void updateFileGroup(SysFileGroupUpdateCommand command) {
        SysFileGroupEntity sysFileGroupEntity = sysFileGroupAppConverter.convertUpdateCommand2Entity(command);
        sysFileGroupRepository.update(sysFileGroupEntity);
    }

    @Override
    public void removeFileGroupBatchByIds(List<SysFileGroupId> ids) {
        sysFileGroupRepository.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysFileGroupAppListDTO> queryFileGroupPage(SysFileGroupListQuery query) {
        PagingInfo<SysFileGroupEntity> sysFileGroupEntityPagingInfo = sysFileGroupRepository.queryPage(query);
        List<SysFileGroupAppListDTO> collect = sysFileGroupEntityPagingInfo.getList().stream().map(sysFileGroupEntity -> sysFileGroupAppConverter.convertEntityToListQueryDTO(sysFileGroupEntity)).toList();
        return PagingInfo.toResponse(collect, sysFileGroupEntityPagingInfo);
    }

    @Override
    public SysFileGroupAppDetailDTO queryFileGroupById(SysFileGroupId id) {
        SysFileGroupEntity FileGroupEntity = sysFileGroupRepository.queryById(id);
        return sysFileGroupAppConverter.convertEntityToDetailDTO(FileGroupEntity);
    }

}
