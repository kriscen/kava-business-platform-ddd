package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.converter.SysI18nAppConverter;
import com.kava.kbpd.upms.application.model.command.SysI18nCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysI18nUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysI18nAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysI18nAppListDTO;
import com.kava.kbpd.upms.application.service.ISysI18nAppService;
import com.kava.kbpd.upms.domain.model.entity.SysI18nMessageEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nListQuery;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nMessageId;
import com.kava.kbpd.upms.domain.service.ISysI18nMessageService;
import com.kava.kbpd.upms.types.exception.UpmsBizException;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SysI18nAppService implements ISysI18nAppService {
    private final ISysI18nMessageService sysI18nMessageService;
    private final SysI18nAppConverter sysI18nAppConverter;

    @Override
    public SysI18nMessageId createI18n(SysI18nCreateCommand command) {
        SysI18nMessageEntity existing = sysI18nMessageService
                .queryByCodeAndLanguage(command.getCode(), command.getLanguage());
        if (existing != null) {
            throw new UpmsBizException("I18N_CODE_DUPLICATE", "翻译键已存在: " + command.getCode());
        }
        SysI18nMessageEntity entity = sysI18nAppConverter.convertCreateCommand2Entity(command);
        return sysI18nMessageService.create(entity);
    }

    @Override
    public void updateI18n(SysI18nUpdateCommand command) {
        SysI18nMessageEntity entity = sysI18nAppConverter.convertUpdateCommand2Entity(command);
        sysI18nMessageService.update(entity);
    }

    @Override
    public void removeI18nBatchByIds(List<SysI18nMessageId> ids) {
        sysI18nMessageService.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysI18nAppListDTO> queryI18nPage(SysI18nListQuery query) {
        PagingInfo<SysI18nMessageEntity> entityPage = sysI18nMessageService.queryPage(query);
        List<SysI18nAppListDTO> collect = entityPage.getList().stream()
                .map(sysI18nAppConverter::convertEntityToListQueryDTO).toList();
        return PagingInfo.toResponse(collect, entityPage);
    }

    @Override
    public SysI18nAppDetailDTO queryI18nById(SysI18nMessageId id) {
        SysI18nMessageEntity entity = sysI18nMessageService.queryById(id);
        return sysI18nAppConverter.convertEntityToDetailDTO(entity);
    }
}
