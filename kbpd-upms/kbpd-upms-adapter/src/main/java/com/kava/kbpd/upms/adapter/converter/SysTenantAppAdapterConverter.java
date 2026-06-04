package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.response.SysTenantAppDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysTenantAppListResponse;
import com.kava.kbpd.upms.application.model.dto.TenantAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.TenantAppListDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysTenantAppAdapterConverter {

    SysTenantAppListResponse convertEntity2List(TenantAppListDTO dto);

    SysTenantAppDetailResponse convertEntity2Detail(TenantAppDetailDTO dto);
}
