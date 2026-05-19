package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysRouteConfEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfId;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfListQuery;
import com.kava.kbpd.upms.domain.service.ISysRouteConfService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRouteConfService implements ISysRouteConfService {

    @Override
    public SysRouteConfId create(SysRouteConfEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean update(SysRouteConfEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public PagingInfo<SysRouteConfEntity> queryPage(SysRouteConfListQuery query) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public SysRouteConfEntity queryById(SysRouteConfId id) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean removeBatchByIds(List<SysRouteConfId> ids) {
        throw new UnsupportedOperationException("暂未实现");
    }
}
