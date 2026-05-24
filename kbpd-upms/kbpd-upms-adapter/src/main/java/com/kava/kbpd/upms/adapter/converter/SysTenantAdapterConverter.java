package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.api.model.query.SysTenantAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysTenantRequest;
import com.kava.kbpd.upms.api.model.response.SysTenantDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysTenantListResponse;
import com.kava.kbpd.upms.application.model.command.SysTenantCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysTenantUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantListQuery;
import com.kava.kbpd.upms.types.enums.SysTenantStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysTenantAdapterConverter {

    @Mapping(source = "name", target = "tenantName")
    SysTenantListQuery convertQueryDTO2QueryVal(SysTenantAdapterListQuery request);

    @Mapping(target = "status", expression = "java(mapStatus(dto.getStatus()))")
    SysTenantListResponse convertEntity2List(SysTenantAppListDTO dto);

    @Mapping(target = "status", expression = "java(mapStatus(dto.getStatus()))")
    SysTenantDetailResponse convertEntity2Detail(SysTenantAppDetailDTO dto);

    SysTenantCreateCommand convertRequest2CreateCommand(SysTenantRequest request);

    @Mapping(source = "id", target = "id")
    SysTenantUpdateCommand convertRequest2UpdateCommand(SysTenantRequest request);

    default String mapStatus(SysTenantStatus status) {
        return status != null ? status.getCode() : null;
    }

    default SysTenantId map(Long id) {
        return id != null ? SysTenantId.of(id) : null;
    }
}
