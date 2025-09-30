package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysUserAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysUserRequest;
import com.kava.kbpd.upms.api.model.response.SysUserListResponse;
import com.kava.kbpd.upms.api.model.response.SysUserDetailResponse;
import com.kava.kbpd.upms.application.model.command.SysUserCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysUserUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysUserAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysUserAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysUserAdapterConverter {


    SysUserListQuery convertQueryDTO2QueryVal(SysUserAdapterListQuery request);

    SysUserListResponse convertDTO2List(SysUserAppListDTO dto);

    SysUserCreateCommand convertRequest2CreateCommand(SysUserRequest req);

    SysUserUpdateCommand convertRequest2UpdateCommand(SysUserRequest req);

    SysUserDetailResponse convertDetailDTO2DetailResp(SysUserAppDetailDTO dto);
}