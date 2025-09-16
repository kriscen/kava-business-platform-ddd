package com.kava.kbpd.upms.trigger.converter;

import com.kava.kbpd.upms.api.model.query.SysPublicParamQuery;
import com.kava.kbpd.upms.api.model.request.SysPublicParamRequest;
import com.kava.kbpd.upms.api.model.response.SysPublicParamListResponse;
import com.kava.kbpd.upms.api.model.response.SysPublicParamResponse;
import com.kava.kbpd.upms.domain.basic.model.entity.SysPublicParamEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysPublicParamListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysPublicParam转换器
 */
@Mapper(componentModel = "spring")
public interface SysPublicParamTriggerConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysPublicParamListQuery convertQueryDTO2QueryVal(SysPublicParamQuery request);

    @Mapping(source = "id.id", target = "id")
    SysPublicParamListResponse convertEntity2List(SysPublicParamEntity entity);

    @Mapping(source = "id", target = "id.id")
    SysPublicParamEntity convertRequest2Entity(SysPublicParamRequest req);

    @Mapping(source = "id.id", target = "id")
    SysPublicParamResponse convertEntity2Detail(SysPublicParamEntity sysPublicParam);
}