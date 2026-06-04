package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.request.SysAppRequest;
import com.kava.kbpd.upms.api.model.response.SysAppDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysAppDropdownResponse;
import com.kava.kbpd.upms.api.model.response.SysAppListResponse;
import com.kava.kbpd.upms.application.model.command.SysAppCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysAppUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAppListDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysAppAdapterConverter {

    SysAppCreateCommand convertRequest2CreateCommand(SysAppRequest request);

    SysAppUpdateCommand convertRequest2UpdateCommand(SysAppRequest request);

    SysAppListResponse convertEntity2List(SysAppListDTO dto);

    SysAppDetailResponse convertEntity2Detail(SysAppDetailDTO dto);

    SysAppDropdownResponse convertEntity2Dropdown(SysAppListDTO dto);
}
