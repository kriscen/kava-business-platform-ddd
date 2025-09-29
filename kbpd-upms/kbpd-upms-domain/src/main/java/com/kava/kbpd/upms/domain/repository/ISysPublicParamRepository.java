package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.upms.domain.model.entity.SysPublicParamEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamId;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamListQuery;

public interface ISysPublicParamRepository extends IBaseSimpleRepository<SysPublicParamId, SysPublicParamEntity, SysPublicParamListQuery> {

}