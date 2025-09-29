package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysPublicParamQuery;
import com.kava.kbpd.upms.api.model.request.SysPublicParamRequest;
import com.kava.kbpd.upms.api.model.response.SysPublicParamListResponse;
import com.kava.kbpd.upms.api.model.response.SysPublicParamResponse;
import com.kava.kbpd.upms.domain.model.entity.SysPublicParamEntity;
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
    SysPublicParamListQuery convertQueryDTO2QueryVal(SysPublicParamQuery request);

    @Mapping(source = "id.id", target = "id")
    SysPublicParamListResponse convertEntity2List(SysPublicParamEntity entity);

    @Mapping(source = "id", target = "id.id")
    SysPublicParamEntity convertRequest2Entity(SysPublicParamRequest req);

    @Mapping(source = "id.id", target = "id")
    SysPublicParamResponse convertEntity2Detail(SysPublicParamEntity sysPublicParam);
}