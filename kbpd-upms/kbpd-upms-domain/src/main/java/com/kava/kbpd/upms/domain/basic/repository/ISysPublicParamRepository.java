package com.kava.kbpd.upms.domain.basic.repository;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysPublicParamEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysPublicParamListQuery;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysPublicParamId;

import java.util.List;

public interface ISysPublicParamRepository {
    SysPublicParamId create(SysPublicParamEntity entity);

    Boolean update(SysPublicParamEntity entity);

    PagingInfo<SysPublicParamEntity> queryPage(SysPublicParamListQuery query);

    SysPublicParamEntity queryById(SysPublicParamId id);

    Boolean removeBatchByIds(List<SysPublicParamId> ids);
}