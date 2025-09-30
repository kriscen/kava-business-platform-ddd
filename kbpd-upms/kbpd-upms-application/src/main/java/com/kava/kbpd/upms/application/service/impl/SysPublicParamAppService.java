package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.converter.SysPublicParamAppConverter;
import com.kava.kbpd.upms.application.model.command.SysPublicParamCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysPublicParamUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysPublicParamAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysPublicParamAppListDTO;
import com.kava.kbpd.upms.application.service.ISysPublicParamAppService;
import com.kava.kbpd.upms.domain.model.entity.SysPublicParamEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamId;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamListQuery;
import com.kava.kbpd.upms.domain.repository.ISysPublicParamRepository;
import com.kava.kbpd.upms.domain.service.ISysPublicParamService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: PublicParam application service
 */
@Slf4j
@Service
public class SysPublicParamAppService implements ISysPublicParamAppService {
    @Resource
    private ISysPublicParamRepository sysPublicParamRepository;

    @Resource
    private ISysPublicParamService sysPublicParamService;

    @Resource
    private SysPublicParamAppConverter sysPublicParamAppConverter;

    @Override
    public SysPublicParamId createPublicParam(SysPublicParamCreateCommand command) {
        SysPublicParamEntity sysPublicParamEntity = sysPublicParamAppConverter.convertCreateCommand2Entity(command);
        return sysPublicParamRepository.create(sysPublicParamEntity);
    }

    @Override
    public void updatePublicParam(SysPublicParamUpdateCommand command) {
        SysPublicParamEntity sysPublicParamEntity = sysPublicParamAppConverter.convertUpdateCommand2Entity(command);
        sysPublicParamRepository.update(sysPublicParamEntity);
    }

    @Override
    public void removePublicParamBatchByIds(List<SysPublicParamId> ids) {
        sysPublicParamRepository.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysPublicParamAppListDTO> queryPublicParamPage(SysPublicParamListQuery query) {
        PagingInfo<SysPublicParamEntity> sysPublicParamEntityPagingInfo = sysPublicParamRepository.queryPage(query);
        List<SysPublicParamAppListDTO> collect = sysPublicParamEntityPagingInfo.getList().stream().map(sysPublicParamEntity -> sysPublicParamAppConverter.convertEntityToListQueryDTO(sysPublicParamEntity)).toList();
        return PagingInfo.toResponse(collect, sysPublicParamEntityPagingInfo);
    }

    @Override
    public SysPublicParamAppDetailDTO queryPublicParamById(SysPublicParamId id) {
        SysPublicParamEntity PublicParamEntity = sysPublicParamRepository.queryById(id);
        return sysPublicParamAppConverter.convertEntityToDetailDTO(PublicParamEntity);
    }

}
