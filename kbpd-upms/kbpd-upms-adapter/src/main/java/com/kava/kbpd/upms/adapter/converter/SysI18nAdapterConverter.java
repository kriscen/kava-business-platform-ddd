package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysI18nAdapterListQuery;
import com.kava.kbpd.upms.api.model.query.SysI18nAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysI18nRequest;
import com.kava.kbpd.upms.api.model.request.SysI18nRequest;
import com.kava.kbpd.upms.api.model.response.SysI18nDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysI18nListResponse;
import com.kava.kbpd.upms.api.model.response.SysI18nDetailResponse;
import com.kava.kbpd.upms.application.model.command.SysI18nCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysI18nUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysI18nAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysI18nAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysI18nEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nListQuery;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysI18nAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysI18nListQuery convertQueryDTO2QueryVal(SysI18nAdapterListQuery request);

    SysI18nListResponse convertEntity2List(SysI18nAppListDTO request);

    SysI18nDetailResponse convertEntity2Detail(SysI18nAppDetailDTO request);

    SysI18nCreateCommand convertRequest2CreateCommand(SysI18nRequest request);

    SysI18nUpdateCommand convertRequest2UpdateCommand(SysI18nRequest request);
}