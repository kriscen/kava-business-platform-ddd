package com.kava.kbpd.upms.domain.basic.repository;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysI18nEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysI18nListQuery;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysI18nId;

import java.util.List;

public interface ISysI18nRepository {
    SysI18nId create(SysI18nEntity entity);

    Boolean update(SysI18nEntity entity);

    PagingInfo<SysI18nEntity> queryPage(SysI18nListQuery query);

    SysI18nEntity queryById(SysI18nId id);

    Boolean removeBatchByIds(List<SysI18nId> ids);
}