package com.kava.kbpd.upms.trigger.converter;

import com.kava.kbpd.upms.api.model.query.SysI18nQuery;
import com.kava.kbpd.upms.api.model.request.SysI18nRequest;
import com.kava.kbpd.upms.api.model.response.SysI18nResponse;
import com.kava.kbpd.upms.domain.basic.model.entity.SysI18nEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysI18nListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysI18nTriggerConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysI18nListQuery convertQueryDTO2QueryVal(SysI18nQuery query);

    @Mapping(source = "id", target = "id.id")
    SysI18nEntity convertRequest2Entity(SysI18nRequest request);

    @Mapping(source = "id.id", target = "id")
    SysI18nResponse convertEntity2Resp(SysI18nEntity entity);

}