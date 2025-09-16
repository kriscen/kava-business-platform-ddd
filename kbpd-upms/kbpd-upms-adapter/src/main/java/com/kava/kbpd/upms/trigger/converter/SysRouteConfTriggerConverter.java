package com.kava.kbpd.upms.trigger.converter;

import com.kava.kbpd.upms.api.model.query.SysRouteConfQuery;
import com.kava.kbpd.upms.api.model.request.SysRouteConfRequest;
import com.kava.kbpd.upms.api.model.response.SysRouteConfListResponse;
import com.kava.kbpd.upms.api.model.response.SysRouteConfResponse;
import com.kava.kbpd.upms.domain.basic.model.entity.SysRouteConfEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysRouteConfListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysRouteConfTriggerConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysRouteConfListQuery convertQueryDTO2QueryVal(SysRouteConfQuery request);

    @Mapping(source = "id.id", target = "id")
    SysRouteConfListResponse convertEntity2List(SysRouteConfEntity entity);

    @Mapping(source = "id", target = "id.id")
    SysRouteConfEntity convertRequest2Entity(SysRouteConfRequest req);

    @Mapping(source = "id.id", target = "id")
    SysRouteConfResponse convertEntity2Detail(SysRouteConfEntity sysRouteConf);
}