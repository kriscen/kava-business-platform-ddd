package com.kava.kbpd.upms.domain.basic.repository;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysFileGroupEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysFileGroupListQuery;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysFileGroupId;

import java.util.List;

public interface ISysFileGroupRepository {
    SysFileGroupId create(SysFileGroupEntity entity);

    Boolean update(SysFileGroupEntity entity);

    PagingInfo<SysFileGroupEntity> queryPage(SysFileGroupListQuery query);

    SysFileGroupEntity queryById(SysFileGroupId id);

    Boolean removeBatchByIds(List<SysFileGroupId> ids);
}