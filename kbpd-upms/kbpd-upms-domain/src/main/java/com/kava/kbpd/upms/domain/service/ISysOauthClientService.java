package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysOauthClientEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientId;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientListQuery;

import java.util.List;

public interface ISysOauthClientService {
    SysOauthClientId create(SysOauthClientEntity entity);

    Boolean update(SysOauthClientEntity entity);

    PagingInfo<SysOauthClientEntity> queryPage(SysOauthClientListQuery query);

    SysOauthClientEntity queryById(SysOauthClientId id);

    Boolean removeBatchByIds(List<SysOauthClientId> ids);
}