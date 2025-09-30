package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysRoleAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysRoleRequest;
import com.kava.kbpd.upms.api.model.response.SysRoleDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysRoleListResponse;
import com.kava.kbpd.upms.application.model.command.SysRoleCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysRoleUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;
import org.mapstruct.Mapper;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysRole转换器
 */
@Mapper(componentModel = "spring")
public interface SysRoleAdapterConverter {

    SysRoleListQuery convertQueryDTO2QueryVal(SysRoleAdapterListQuery request);

    SysRoleListResponse convertDTO2List(SysRoleAppListDTO dto);

    SysRoleCreateCommand convertRequest2CreateCommand(SysRoleRequest req);

    SysRoleUpdateCommand convertRequest2UpdateCommand(SysRoleRequest req);

    SysRoleDetailResponse convertDetailDTO2DetailResp(SysRoleAppDetailDTO dto);
}