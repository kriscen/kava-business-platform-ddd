package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysMenuQuery;
import com.kava.kbpd.upms.api.model.request.SysMenuRequest;
import com.kava.kbpd.upms.api.model.response.SysMenuListResponse;
import com.kava.kbpd.upms.api.model.response.SysMenuDetailResponse;
import com.kava.kbpd.upms.domain.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysMenu转换器
 */
@Mapper(componentModel = "spring")
public interface SysMenuAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysMenuListQuery convertQueryDTO2QueryVal(SysMenuQuery request);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    SysMenuListResponse convertEntity2List(SysMenuEntity entity);

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "pid", target = "pid.id")
    SysMenuEntity convertRequest2Entity(SysMenuRequest req);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    SysMenuDetailResponse convertEntity2Detail(SysMenuEntity sysMenu);
}