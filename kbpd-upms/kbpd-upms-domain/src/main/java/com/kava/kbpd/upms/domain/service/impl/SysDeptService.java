package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysDeptEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptId;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptListQuery;
import com.kava.kbpd.upms.domain.service.ISysDeptService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDeptService implements ISysDeptService {

    @Override
    public SysDeptId create(SysDeptEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean update(SysDeptEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public PagingInfo<SysDeptEntity> queryPage(SysDeptListQuery query) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public SysDeptEntity queryById(SysDeptId id) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean removeBatchByIds(List<SysDeptId> ids) {
        throw new UnsupportedOperationException("暂未实现");
    }
}
