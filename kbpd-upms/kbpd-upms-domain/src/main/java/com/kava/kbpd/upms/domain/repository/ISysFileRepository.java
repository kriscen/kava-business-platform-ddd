package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.upms.domain.model.entity.SysFileEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysFileId;
import com.kava.kbpd.upms.domain.model.valobj.SysFileListQuery;

public interface ISysFileRepository extends IBaseSimpleRepository<SysFileId, SysFileEntity, SysFileListQuery> {

}