package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.upms.domain.model.entity.SysAreaEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaId;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaListQuery;

import java.util.List;

public interface ISysAreaRepository extends IBaseSimpleRepository<SysAreaId, SysAreaEntity,SysAreaListQuery> {
    List<SysAreaEntity> selectTreeList(SysAreaListQuery query);

}
