package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysFileEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysFileId;
import com.kava.kbpd.upms.domain.model.valobj.SysFileListQuery;
import com.kava.kbpd.upms.domain.service.ISysFileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysFileService implements ISysFileService {

    @Override
    public SysFileId create(SysFileEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean update(SysFileEntity entity) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public PagingInfo<SysFileEntity> queryPage(SysFileListQuery query) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public SysFileEntity queryById(SysFileId id) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public Boolean removeBatchByIds(List<SysFileId> ids) {
        throw new UnsupportedOperationException("暂未实现");
    }
}
