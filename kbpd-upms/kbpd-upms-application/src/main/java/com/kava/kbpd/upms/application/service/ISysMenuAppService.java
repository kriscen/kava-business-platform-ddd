package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.model.command.SysMenuCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysMenuUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysMenuAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysMenuAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: Menu application service
 */
public interface ISysMenuAppService {
    SysMenuId createMenu(SysMenuCreateCommand command);

    void updateMenu(SysMenuUpdateCommand command);

    void removeMenuBatchByIds(List<SysMenuId> ids);

    PagingInfo<SysMenuAppListDTO> queryMenuPage(SysMenuListQuery query);

    SysMenuAppDetailDTO queryMenuById(SysMenuId id);

}
