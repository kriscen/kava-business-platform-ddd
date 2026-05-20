package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysI18nMessage;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nListQuery;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nMessageId;

import java.util.List;

public interface ISysI18nMessageService {
    SysI18nMessageId create(SysI18nMessage entity);

    Boolean update(SysI18nMessage entity);

    PagingInfo<SysI18nMessage> queryPage(SysI18nListQuery query);

    SysI18nMessage queryById(SysI18nMessageId id);

    Boolean removeBatchByIds(List<SysI18nMessageId> ids);
}
