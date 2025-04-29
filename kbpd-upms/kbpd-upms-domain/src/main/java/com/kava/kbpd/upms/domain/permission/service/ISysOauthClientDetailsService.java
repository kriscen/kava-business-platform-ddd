package com.kava.kbpd.upms.domain.permission.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.permission.model.entity.SysOauthClientDetailsEntity;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysOauthClientDetailsId;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysOauthClientDetailsListQuery;

import java.util.List;

public interface ISysOauthClientDetailsService {
    SysOauthClientDetailsId create(SysOauthClientDetailsEntity entity);

    Boolean update(SysOauthClientDetailsEntity entity);

    PagingInfo<SysOauthClientDetailsEntity> queryPage(SysOauthClientDetailsListQuery query);

    SysOauthClientDetailsEntity queryById(SysOauthClientDetailsId id);

    Boolean removeBatchByIds(List<SysOauthClientDetailsId> ids);
}