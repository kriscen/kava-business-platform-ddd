package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysPublicParamEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamId;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamListQuery;

import java.util.List;

public interface ISysPublicParamService {
    SysPublicParamId create(SysPublicParamEntity entity);

    Boolean update(SysPublicParamEntity entity);

    PagingInfo<SysPublicParamEntity> queryPage(SysPublicParamListQuery query);

    SysPublicParamEntity queryById(SysPublicParamId id);

    Boolean removeBatchByIds(List<SysPublicParamId> ids);
}