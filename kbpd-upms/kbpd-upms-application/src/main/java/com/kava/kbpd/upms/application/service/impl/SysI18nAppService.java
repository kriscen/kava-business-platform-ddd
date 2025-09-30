package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.converter.SysI18nAppConverter;
import com.kava.kbpd.upms.application.model.command.SysI18nCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysI18nUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysI18nAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysI18nAppListDTO;
import com.kava.kbpd.upms.application.service.ISysI18nAppService;
import com.kava.kbpd.upms.domain.model.entity.SysI18nEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nId;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nListQuery;
import com.kava.kbpd.upms.domain.repository.ISysI18nRepository;
import com.kava.kbpd.upms.domain.service.ISysI18nService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: I18n application service
 */
@Slf4j
@Service
public class SysI18nAppService implements ISysI18nAppService {
    @Resource
    private ISysI18nRepository sysI18nRepository;

    @Resource
    private ISysI18nService sysI18nService;

    @Resource
    private SysI18nAppConverter sysI18nAppConverter;

    @Override
    public SysI18nId createI18n(SysI18nCreateCommand command) {
        SysI18nEntity sysI18nEntity = sysI18nAppConverter.convertCreateCommand2Entity(command);
        return sysI18nRepository.create(sysI18nEntity);
    }

    @Override
    public void updateI18n(SysI18nUpdateCommand command) {
        SysI18nEntity sysI18nEntity = sysI18nAppConverter.convertUpdateCommand2Entity(command);
        sysI18nRepository.update(sysI18nEntity);
    }

    @Override
    public void removeI18nBatchByIds(List<SysI18nId> ids) {
        sysI18nRepository.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysI18nAppListDTO> queryI18nPage(SysI18nListQuery query) {
        PagingInfo<SysI18nEntity> sysI18nEntityPagingInfo = sysI18nRepository.queryPage(query);
        List<SysI18nAppListDTO> collect = sysI18nEntityPagingInfo.getList().stream().map(sysI18nEntity -> sysI18nAppConverter.convertEntityToListQueryDTO(sysI18nEntity)).toList();
        return PagingInfo.toResponse(collect, sysI18nEntityPagingInfo);
    }

    @Override
    public SysI18nAppDetailDTO queryI18nById(SysI18nId id) {
        SysI18nEntity I18nEntity = sysI18nRepository.queryById(id);
        return sysI18nAppConverter.convertEntityToDetailDTO(I18nEntity);
    }

}
