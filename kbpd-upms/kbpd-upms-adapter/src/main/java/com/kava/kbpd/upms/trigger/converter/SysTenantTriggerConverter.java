package com.kava.kbpd.upms.trigger.converter;

import com.kava.kbpd.upms.api.model.query.SysTenantQuery;
import com.kava.kbpd.upms.api.model.request.SysTenantRequest;
import com.kava.kbpd.upms.api.model.response.SysTenantListResponse;
import com.kava.kbpd.upms.api.model.response.SysTenantResponse;
import com.kava.kbpd.upms.domain.basic.model.entity.SysTenantEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysTenantListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysTenantTriggerConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysTenantListQuery convertQueryDTO2QueryVal(SysTenantQuery request);

    @Mapping(source = "id.id", target = "id")
    SysTenantListResponse convertEntity2List(SysTenantEntity entity);

    @Mapping(source = "id", target = "id.id")
    SysTenantEntity convertRequest2Entity(SysTenantRequest req);

    @Mapping(source = "id.id", target = "id")
    SysTenantResponse convertEntity2Detail(SysTenantEntity sysTenant);
}