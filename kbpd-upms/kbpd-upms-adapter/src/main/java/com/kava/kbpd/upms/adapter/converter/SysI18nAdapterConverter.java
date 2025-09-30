package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysI18nQuery;
import com.kava.kbpd.upms.api.model.request.SysI18nRequest;
import com.kava.kbpd.upms.api.model.response.SysI18nDetailResponse;
import com.kava.kbpd.upms.domain.model.entity.SysI18nEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysI18nAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysI18nListQuery convertQueryDTO2QueryVal(SysI18nQuery query);

    @Mapping(source = "id", target = "id.id")
    SysI18nEntity convertRequest2Entity(SysI18nRequest request);

    @Mapping(source = "id.id", target = "id")
    SysI18nDetailResponse convertEntity2Resp(SysI18nEntity entity);

}