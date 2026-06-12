package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.model.valobj.SysAppId;

import java.util.List;

public interface ISysAppMenuRepository {

    void replaceAppMenus(SysAppId appId, List<Long> menuIds);

    List<Long> queryMenuIdsByAppId(SysAppId appId);

    List<Long> queryMenuIdsByAppIds(List<Long> appIds);
}
