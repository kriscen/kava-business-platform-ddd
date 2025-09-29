package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysFileGroupEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysFileGroupId;
import com.kava.kbpd.upms.domain.model.valobj.SysFileGroupListQuery;

import java.util.List;

public interface ISysFileGroupService {
    SysFileGroupId create(SysFileGroupEntity entity);

    Boolean update(SysFileGroupEntity entity);

    PagingInfo<SysFileGroupEntity> queryPage(SysFileGroupListQuery query);

    SysFileGroupEntity queryById(SysFileGroupId id);

    Boolean removeBatchByIds(List<SysFileGroupId> ids);
}