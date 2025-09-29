package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysRoleQuery;
import com.kava.kbpd.upms.api.model.request.SysRoleRequest;
import com.kava.kbpd.upms.api.model.response.SysRoleListResponse;
import com.kava.kbpd.upms.api.model.response.SysRoleResponse;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysRole转换器
 */
@Mapper(componentModel = "spring")
public interface SysRoleAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysRoleListQuery convertQueryDTO2QueryVal(SysRoleQuery request);

    @Mapping(source = "id.id", target = "id")
    SysRoleListResponse convertEntity2List(SysRoleEntity entity);

    @Mapping(source = "id", target = "id.id")
    SysRoleEntity convertRequest2Entity(SysRoleRequest req);

    @Mapping(source = "id.id", target = "id")
    SysRoleResponse convertEntity2Detail(SysRoleEntity sysRole);
}