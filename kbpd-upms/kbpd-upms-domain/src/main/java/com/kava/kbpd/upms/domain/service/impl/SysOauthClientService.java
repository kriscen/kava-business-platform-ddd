package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysOauthClientEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientId;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientListQuery;
import com.kava.kbpd.upms.domain.service.ISysOauthClientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysOauthClientService implements ISysOauthClientService {

    @Override
    public SysOauthClientId create(SysOauthClientEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean update(SysOauthClientEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public PagingInfo<SysOauthClientEntity> queryPage(SysOauthClientListQuery query) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public SysOauthClientEntity queryById(SysOauthClientId id) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean removeBatchByIds(List<SysOauthClientId> ids) {
        throw new UnsupportedOperationException("暂未实现");
    }
}
