package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysLogAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysLogRequest;
import com.kava.kbpd.upms.api.model.response.SysLogDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysLogListResponse;
import com.kava.kbpd.upms.application.model.command.SysLogCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysLogUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysLogAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysLogAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysLogListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysLogAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysLogListQuery convertQueryDTO2QueryVal(SysLogAdapterListQuery request);

    SysLogListResponse convertEntity2List(SysLogAppListDTO request);

    SysLogDetailResponse convertEntity2Detail(SysLogAppDetailDTO request);

    SysLogCreateCommand convertRequest2CreateCommand(SysLogRequest request);

    SysLogUpdateCommand convertRequest2UpdateCommand(SysLogRequest request);
}