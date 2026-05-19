package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysFileGroupEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysFileGroupId;
import com.kava.kbpd.upms.domain.model.valobj.SysFileGroupListQuery;
import com.kava.kbpd.upms.domain.service.ISysFileGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysFileGroupService implements ISysFileGroupService {

    @Override
    public SysFileGroupId create(SysFileGroupEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean update(SysFileGroupEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public PagingInfo<SysFileGroupEntity> queryPage(SysFileGroupListQuery query) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public SysFileGroupEntity queryById(SysFileGroupId id) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean removeBatchByIds(List<SysFileGroupId> ids) {
        throw new UnsupportedOperationException("暂未实现");
    }
}
