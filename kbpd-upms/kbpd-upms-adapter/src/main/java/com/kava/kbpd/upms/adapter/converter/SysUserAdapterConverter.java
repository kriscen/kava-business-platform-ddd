package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysUserQuery;
import com.kava.kbpd.upms.api.model.request.SysUserRequest;
import com.kava.kbpd.upms.api.model.response.SysUserListResponse;
import com.kava.kbpd.upms.api.model.response.SysUserResponse;
import com.kava.kbpd.upms.domain.model.entity.SysUserEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysUserAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysUserListQuery convertQueryDTO2QueryVal(SysUserQuery request);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "tenantId.id", target = "tenantId")
    @Mapping(source = "deptId.id", target = "deptId")
    SysUserListResponse convertEntity2List(SysUserEntity entity);

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "deptId", target = "deptId.id")
    @Mapping(source = "tenantId", target = "tenantId.id")
    SysUserEntity convertRequest2Entity(SysUserRequest req);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "deptId.id", target = "deptId")
    @Mapping(source = "tenantId.id", target = "tenantId")
    SysUserResponse convertEntity2Detail(SysUserEntity sysUser);
}