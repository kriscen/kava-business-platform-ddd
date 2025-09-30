package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.model.command.SysI18nCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysI18nUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysI18nAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysI18nAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nId;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: I18n application service
 */
public interface ISysI18nAppService {
    SysI18nId createI18n(SysI18nCreateCommand command);

    void updateI18n(SysI18nUpdateCommand command);

    void removeI18nBatchByIds(List<SysI18nId> ids);

    PagingInfo<SysI18nAppListDTO> queryI18nPage(SysI18nListQuery query);

    SysI18nAppDetailDTO queryI18nById(SysI18nId id);

}
