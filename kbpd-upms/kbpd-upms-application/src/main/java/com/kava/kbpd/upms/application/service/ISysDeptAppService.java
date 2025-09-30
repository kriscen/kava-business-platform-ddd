package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.model.command.SysDeptCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysDeptUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysDeptAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysDeptAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptId;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: dept application service
 */
public interface ISysDeptAppService {
    SysDeptId createDept(SysDeptCreateCommand command);

    void updateDept(SysDeptUpdateCommand command);

    void removeDeptBatchByIds(List<SysDeptId> ids);

    PagingInfo<SysDeptAppListDTO> queryDeptPage(SysDeptListQuery query);

    SysDeptAppDetailDTO queryDeptById(SysDeptId id);

}
