package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysUserQuery;
import com.kava.kbpd.upms.api.model.request.SysUserRequest;
import com.kava.kbpd.upms.api.model.response.SysUserListResponse;
import com.kava.kbpd.upms.api.model.response.SysUserResponse;
import com.kava.kbpd.upms.application.model.command.SysUserCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysUserUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysUserAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysUserListQueryDTO;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysUserAdapterConverter {


    SysUserListQuery convertQueryDTO2QueryVal(SysUserQuery request);

    SysUserListResponse convertDTO2List(SysUserListQueryDTO dto);

    SysUserCreateCommand convertRequest2CreateCommand(SysUserRequest req);

    SysUserUpdateCommand convertRequest2UpdateCommand(SysUserRequest req);

    SysUserResponse convertDetailDTO2DetailResp(SysUserAppDetailDTO dto);
}