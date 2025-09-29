package com.kava.kbpd.upms.application.service;

import cn.hutool.core.lang.tree.Tree;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.model.command.SysAreaCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysAreaUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysAreaAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAreaListQueryDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaId;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: area application service
 */
public interface ISysAreaAppService {
    SysAreaId createArea(SysAreaCreateCommand command);

    void updateArea(SysAreaUpdateCommand command);

    void removeAreaBatchByIds(List<SysAreaId> ids);

    PagingInfo<SysAreaListQueryDTO> queryAreaPage(SysAreaListQuery query);

    SysAreaAppDetailDTO queryAreaById(SysAreaId id);

    List<Tree<Long>> selectAreaTree(SysAreaListQuery query);
}
