package com.kava.kbpd.upms.trigger.converter;

import com.kava.kbpd.upms.api.model.query.SysUserQuery;
import com.kava.kbpd.upms.api.model.request.SysUserRequest;
import com.kava.kbpd.upms.api.model.response.SysUserListResponse;
import com.kava.kbpd.upms.api.model.response.SysUserResponse;
import com.kava.kbpd.upms.domain.user.model.entity.SysUserEntity;
import com.kava.kbpd.upms.domain.user.model.valobj.SysUserListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysUserTriggerConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysUserListQuery convertQueryDTO2QueryVal(SysUserQuery request);

    @Mapping(source = "id.id", target = "id")
    SysUserListResponse convertEntity2List(SysUserEntity entity);

    @Mapping(source = "id", target = "id.id")
    SysUserEntity convertRequest2Entity(SysUserRequest req);

    @Mapping(source = "id.id", target = "id")
    SysUserResponse convertEntity2Detail(SysUserEntity sysUser);
}