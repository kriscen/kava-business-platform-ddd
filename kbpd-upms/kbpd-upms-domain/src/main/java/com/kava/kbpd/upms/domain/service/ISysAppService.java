package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysAppEntity;
import com.kava.kbpd.common.core.model.valobj.SysAppId;
import com.kava.kbpd.upms.domain.model.valobj.SysAppListQuery;

import java.util.List;

public interface ISysAppService {
    SysAppId create(SysAppEntity entity);

    Boolean update(SysAppEntity entity);

    PagingInfo<SysAppEntity> queryPage(SysAppListQuery query);

    SysAppEntity queryById(SysAppId id);

    Boolean removeBatchByIds(List<SysAppId> ids);

    List<SysAppEntity> queryAll();
}
