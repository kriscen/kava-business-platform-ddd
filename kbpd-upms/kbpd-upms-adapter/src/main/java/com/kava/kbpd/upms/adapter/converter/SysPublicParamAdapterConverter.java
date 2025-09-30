package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysPublicParamAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysPublicParamRequest;
import com.kava.kbpd.upms.api.model.response.SysPublicParamDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysPublicParamListResponse;
import com.kava.kbpd.upms.application.model.command.SysPublicParamCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysPublicParamUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysPublicParamAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysPublicParamAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysPublicParam转换器
 */
@Mapper(componentModel = "spring")
public interface SysPublicParamAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysPublicParamListQuery convertQueryDTO2QueryVal(SysPublicParamAdapterListQuery request);

    SysPublicParamListResponse convertEntity2List(SysPublicParamAppListDTO request);

    SysPublicParamDetailResponse convertEntity2Detail(SysPublicParamAppDetailDTO request);

    SysPublicParamCreateCommand convertRequest2CreateCommand(SysPublicParamRequest request);

    SysPublicParamUpdateCommand convertRequest2UpdateCommand(SysPublicParamRequest request);
}