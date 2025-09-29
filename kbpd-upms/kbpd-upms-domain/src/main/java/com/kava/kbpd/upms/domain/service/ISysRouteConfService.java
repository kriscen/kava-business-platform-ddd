package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysRouteConfEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfId;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfListQuery;

import java.util.List;

public interface ISysRouteConfService {
    SysRouteConfId create(SysRouteConfEntity entity);

    Boolean update(SysRouteConfEntity entity);

    PagingInfo<SysRouteConfEntity> queryPage(SysRouteConfListQuery query);

    SysRouteConfEntity queryById(SysRouteConfId id);

    Boolean removeBatchByIds(List<SysRouteConfId> ids);
}