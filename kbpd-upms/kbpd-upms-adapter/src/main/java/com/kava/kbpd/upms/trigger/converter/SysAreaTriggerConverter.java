package com.kava.kbpd.upms.trigger.converter;

import com.kava.kbpd.upms.api.model.query.SysAreaQuery;
import com.kava.kbpd.upms.api.model.request.SysAreaRequest;
import com.kava.kbpd.upms.api.model.response.SysAreaResponse;
import com.kava.kbpd.upms.api.model.response.SysAreaListResponse;
import com.kava.kbpd.upms.domain.basic.model.entity.SysAreaEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAreaListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysArea转换器
 */
@Mapper(componentModel = "spring")
public interface SysAreaTriggerConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysAreaListQuery convertQueryDTO2QueryVal(SysAreaQuery request);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    SysAreaListResponse convertEntity2List(SysAreaEntity request);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    SysAreaResponse convertEntity2Detail(SysAreaEntity request);

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "pid", target = "pid.id")
    SysAreaEntity convertRequest2Entity(SysAreaRequest request);
}
