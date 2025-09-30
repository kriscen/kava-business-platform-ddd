package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.model.command.SysRouteConfCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysRouteConfUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysRouteConfAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysRouteConfAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfId;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: RouteConf application service
 */
public interface ISysRouteConfAppService {
    SysRouteConfId createRouteConf(SysRouteConfCreateCommand command);

    void updateRouteConf(SysRouteConfUpdateCommand command);

    void removeRouteConfBatchByIds(List<SysRouteConfId> ids);

    PagingInfo<SysRouteConfAppListDTO> queryRouteConfPage(SysRouteConfListQuery query);

    SysRouteConfAppDetailDTO queryRouteConfById(SysRouteConfId id);

}
