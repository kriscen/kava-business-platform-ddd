package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.upms.domain.model.entity.SysI18nMessageEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nListQuery;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nMessageId;

public interface ISysI18nMessageRepository extends IBaseSimpleRepository<SysI18nMessageId, SysI18nMessageEntity, SysI18nListQuery> {

    SysI18nMessageEntity queryByCodeAndLanguage(String code, String language);
}
