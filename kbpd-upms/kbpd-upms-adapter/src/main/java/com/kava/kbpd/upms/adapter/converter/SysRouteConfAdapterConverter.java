package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysRouteConfAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysRouteConfRequest;
import com.kava.kbpd.upms.api.model.response.SysRouteConfDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysRouteConfListResponse;
import com.kava.kbpd.upms.application.model.command.SysRouteConfCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysRouteConfUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysRouteConfAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysRouteConfAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysRouteConfAdapterConverter {

    SysRouteConfListQuery convertQueryDTO2QueryVal(SysRouteConfAdapterListQuery request);

    SysRouteConfListResponse convertEntity2List(SysRouteConfAppListDTO request);

    SysRouteConfDetailResponse convertEntity2Detail(SysRouteConfAppDetailDTO request);

    SysRouteConfCreateCommand convertRequest2CreateCommand(SysRouteConfRequest request);

    SysRouteConfUpdateCommand convertRequest2UpdateCommand(SysRouteConfRequest request);
}