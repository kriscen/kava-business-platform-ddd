package com.kava.kbpd.upms.domain.user.repository;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.user.model.entity.SysDeptEntity;
import com.kava.kbpd.upms.domain.user.model.valobj.SysDeptListQuery;
import com.kava.kbpd.upms.domain.user.model.valobj.SysDeptId;

import java.util.List;

public interface ISysDeptRepository {
    SysDeptId create(SysDeptEntity entity);

    Boolean update(SysDeptEntity entity);

    PagingInfo<SysDeptEntity> queryPage(SysDeptListQuery query);

    SysDeptEntity queryById(SysDeptId id);

    Boolean removeBatchByIds(List<SysDeptId> ids);
}