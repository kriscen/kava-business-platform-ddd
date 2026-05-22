package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysI18nMessageEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nListQuery;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nMessageId;

import java.util.List;

public interface ISysI18nMessageService {
    SysI18nMessageId create(SysI18nMessageEntity entity);

    Boolean update(SysI18nMessageEntity entity);

    PagingInfo<SysI18nMessageEntity> queryPage(SysI18nListQuery query);

    SysI18nMessageEntity queryById(SysI18nMessageId id);

    Boolean removeBatchByIds(List<SysI18nMessageId> ids);

    SysI18nMessageEntity queryByCodeAndLanguage(String code, String language);
}
