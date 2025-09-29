package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysDeptEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptId;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptListQuery;

import java.util.List;

public interface ISysDeptService {
    SysDeptId create(SysDeptEntity entity);

    Boolean update(SysDeptEntity entity);

    PagingInfo<SysDeptEntity> queryPage(SysDeptListQuery query);

    SysDeptEntity queryById(SysDeptId id);

    Boolean removeBatchByIds(List<SysDeptId> ids);
}