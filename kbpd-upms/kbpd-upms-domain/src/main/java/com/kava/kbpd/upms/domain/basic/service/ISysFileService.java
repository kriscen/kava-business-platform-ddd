package com.kava.kbpd.upms.domain.basic.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysFileEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysFileId;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysFileListQuery;

import java.util.List;

public interface ISysFileService {
    SysFileId create(SysFileEntity entity);

    Boolean update(SysFileEntity entity);

    PagingInfo<SysFileEntity> queryPage(SysFileListQuery query);

    SysFileEntity queryById(SysFileId id);

    Boolean removeBatchByIds(List<SysFileId> ids);
}