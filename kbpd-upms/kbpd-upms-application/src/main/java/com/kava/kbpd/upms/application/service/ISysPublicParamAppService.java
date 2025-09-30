package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.model.command.SysPublicParamCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysPublicParamUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysPublicParamAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysPublicParamAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamId;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: PublicParam application service
 */
public interface ISysPublicParamAppService {
    SysPublicParamId createPublicParam(SysPublicParamCreateCommand command);

    void updatePublicParam(SysPublicParamUpdateCommand command);

    void removePublicParamBatchByIds(List<SysPublicParamId> ids);

    PagingInfo<SysPublicParamAppListDTO> queryPublicParamPage(SysPublicParamListQuery query);

    SysPublicParamAppDetailDTO queryPublicParamById(SysPublicParamId id);

}
