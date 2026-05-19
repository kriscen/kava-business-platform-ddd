package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuListQuery;
import com.kava.kbpd.upms.domain.service.ISysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuService implements ISysMenuService {

    @Override
    public SysMenuId create(SysMenuEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean update(SysMenuEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public PagingInfo<SysMenuEntity> queryPage(SysMenuListQuery query) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public SysMenuEntity queryById(SysMenuId id) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean removeBatchByIds(List<SysMenuId> ids) {
        throw new UnsupportedOperationException("暂未实现");
    }
}
