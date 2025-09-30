package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysTenantAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysTenantRequest;
import com.kava.kbpd.upms.api.model.response.SysTenantDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysTenantListResponse;
import com.kava.kbpd.upms.application.model.command.SysTenantCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysTenantUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysTenantAdapterConverter {

    SysTenantListQuery convertQueryDTO2QueryVal(SysTenantAdapterListQuery request);

    SysTenantListResponse convertEntity2List(SysTenantAppListDTO request);

    SysTenantDetailResponse convertEntity2Detail(SysTenantAppDetailDTO request);

    SysTenantCreateCommand convertRequest2CreateCommand(SysTenantRequest request);

    SysTenantUpdateCommand convertRequest2UpdateCommand(SysTenantRequest request);
}