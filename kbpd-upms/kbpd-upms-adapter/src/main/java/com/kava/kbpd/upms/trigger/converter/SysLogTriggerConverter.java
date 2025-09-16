package com.kava.kbpd.upms.trigger.converter;

import com.kava.kbpd.upms.api.model.query.SysLogQuery;
import com.kava.kbpd.upms.api.model.request.SysLogRequest;
import com.kava.kbpd.upms.api.model.response.SysLogListResponse;
import com.kava.kbpd.upms.api.model.response.SysLogResponse;
import com.kava.kbpd.upms.domain.basic.model.entity.SysLogEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysLogListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysLogTriggerConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysLogListQuery convertQueryDTO2QueryVal(SysLogQuery query);

    @Mapping(source = "id", target = "id.id")
    SysLogEntity convertRequest2Entity(SysLogRequest request);

    @Mapping(source = "id.id", target = "id")
    SysLogListResponse convertEntity2List(SysLogEntity entity);

    @Mapping(source = "id.id", target = "id")
    SysLogResponse convertEntity2Detail(SysLogEntity entity);
}