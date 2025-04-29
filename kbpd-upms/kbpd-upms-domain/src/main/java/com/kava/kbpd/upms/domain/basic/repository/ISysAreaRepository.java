package com.kava.kbpd.upms.domain.basic.repository;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysAreaEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAreaId;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAreaListQuery;

import java.util.List;

public interface ISysAreaRepository {

    SysAreaId create(SysAreaEntity sysAreaEntity);

    Boolean update(SysAreaEntity sysAreaEntity);

    PagingInfo<SysAreaEntity> queryPage(SysAreaListQuery query);

    SysAreaEntity queryById(SysAreaId id);

    List<SysAreaEntity> selectTreeList(SysAreaListQuery query);

    Boolean removeBatchByIds(List<SysAreaId> ids);
}
