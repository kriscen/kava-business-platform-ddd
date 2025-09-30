package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysTenantQuery;
import com.kava.kbpd.upms.api.model.request.SysTenantRequest;
import com.kava.kbpd.upms.api.model.response.SysTenantListResponse;
import com.kava.kbpd.upms.api.model.response.SysTenantDetailResponse;
import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysTenantAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysTenantListQuery convertQueryDTO2QueryVal(SysTenantQuery request);

    @Mapping(source = "id.id", target = "id")
    SysTenantListResponse convertEntity2List(SysTenantEntity entity);

    @Mapping(source = "id", target = "id.id")
    SysTenantEntity convertRequest2Entity(SysTenantRequest req);

    @Mapping(source = "id.id", target = "id")
    SysTenantDetailResponse convertEntity2Detail(SysTenantEntity sysTenant);
}