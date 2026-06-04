package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.upms.domain.model.entity.SysAppEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAppId;
import com.kava.kbpd.upms.domain.model.valobj.SysAppListQuery;

import java.util.List;

public interface ISysAppRepository extends IBaseSimpleRepository<SysAppId, SysAppEntity, SysAppListQuery> {

    SysAppEntity queryByCode(String code);

    List<SysAppEntity> queryAll();

    boolean existsActiveTenantApp(SysAppId appId);
}
