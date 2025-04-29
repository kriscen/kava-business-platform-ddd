package com.kava.kbpd.upms.domain.basic.repository;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysRouteConfEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysRouteConfListQuery;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysRouteConfId;

import java.util.List;

public interface ISysRouteConfRepository {
    SysRouteConfId create(SysRouteConfEntity entity);

    Boolean update(SysRouteConfEntity entity);

    PagingInfo<SysRouteConfEntity> queryPage(SysRouteConfListQuery query);

    SysRouteConfEntity queryById(SysRouteConfId id);

    Boolean removeBatchByIds(List<SysRouteConfId> ids);
}