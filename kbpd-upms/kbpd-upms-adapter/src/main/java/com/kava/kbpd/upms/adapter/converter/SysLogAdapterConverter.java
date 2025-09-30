package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysLogQuery;
import com.kava.kbpd.upms.api.model.request.SysLogRequest;
import com.kava.kbpd.upms.api.model.response.SysLogDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysLogListResponse;
import com.kava.kbpd.upms.domain.model.entity.SysLogEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysLogListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysLogAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysLogListQuery convertQueryDTO2QueryVal(SysLogQuery query);

    @Mapping(source = "id", target = "id.id")
    SysLogEntity convertRequest2Entity(SysLogRequest request);

    @Mapping(source = "id.id", target = "id")
    SysLogListResponse convertEntity2List(SysLogEntity entity);

    @Mapping(source = "id.id", target = "id")
    SysLogDetailResponse convertEntity2Detail(SysLogEntity entity);
}