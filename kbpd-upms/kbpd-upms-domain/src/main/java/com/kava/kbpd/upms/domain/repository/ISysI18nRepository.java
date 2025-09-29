package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.upms.domain.model.entity.SysI18nEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nId;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nListQuery;

public interface ISysI18nRepository extends IBaseSimpleRepository<SysI18nId, SysI18nEntity, SysI18nListQuery> {

}