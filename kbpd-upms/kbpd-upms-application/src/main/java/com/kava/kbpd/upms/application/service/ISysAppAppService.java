package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.model.command.SysAppCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysAppUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAppListDTO;

import java.util.List;

public interface ISysAppAppService {
    Long createApp(SysAppCreateCommand command);

    void updateApp(SysAppUpdateCommand command);

    void removeAppBatchByIds(List<Long> ids);

    PagingInfo<SysAppListDTO> queryAppPage(String appName, Integer pageNo, Integer pageSize);

    SysAppDetailDTO queryAppById(Long id);

    List<SysAppListDTO> queryAppDropdown();

    void updateAppMenus(Long appId, List<Long> menuIds);
}
