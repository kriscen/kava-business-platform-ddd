package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.dto.SysOauthClientDTO;
import com.kava.kbpd.upms.api.model.query.SysOauthClientAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysOauthClientRequest;
import com.kava.kbpd.upms.api.model.response.SysOauthClientDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysOauthClientListResponse;
import com.kava.kbpd.upms.application.model.command.SysOauthClientCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysOauthClientUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysOauthClientAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysOauthClientAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysOauthClientDetails转换器
 */
@Mapper(componentModel = "spring")
public interface SysOauthClientAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysOauthClientListQuery convertQueryDTO2QueryVal(SysOauthClientAdapterListQuery request);

    SysOauthClientListResponse convertEntity2List(SysOauthClientAppListDTO request);

    SysOauthClientDetailResponse convertEntity2Detail(SysOauthClientAppDetailDTO request);

    SysOauthClientCreateCommand convertRequest2CreateCommand(SysOauthClientRequest request);

    SysOauthClientUpdateCommand convertRequest2UpdateCommand(SysOauthClientRequest request);

    SysOauthClientDTO convertAppDetail2RemoteDTO(SysOauthClientAppDetailDTO appDetailDTO);
}