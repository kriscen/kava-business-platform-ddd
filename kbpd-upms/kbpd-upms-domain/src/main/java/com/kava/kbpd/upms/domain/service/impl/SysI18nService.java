package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysI18nEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nId;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nListQuery;
import com.kava.kbpd.upms.domain.service.ISysI18nService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysI18nService implements ISysI18nService {

    @Override
    public SysI18nId create(SysI18nEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean update(SysI18nEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public PagingInfo<SysI18nEntity> queryPage(SysI18nListQuery query) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public SysI18nEntity queryById(SysI18nId id) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean removeBatchByIds(List<SysI18nId> ids) {
        throw new UnsupportedOperationException("暂未实现");
    }
}
