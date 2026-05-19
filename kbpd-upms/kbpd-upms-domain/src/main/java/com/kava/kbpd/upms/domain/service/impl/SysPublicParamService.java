package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysPublicParamEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamId;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamListQuery;
import com.kava.kbpd.upms.domain.service.ISysPublicParamService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPublicParamService implements ISysPublicParamService {

    @Override
    public SysPublicParamId create(SysPublicParamEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean update(SysPublicParamEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public PagingInfo<SysPublicParamEntity> queryPage(SysPublicParamListQuery query) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public SysPublicParamEntity queryById(SysPublicParamId id) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean removeBatchByIds(List<SysPublicParamId> ids) {
        throw new UnsupportedOperationException("暂未实现");
    }
}
