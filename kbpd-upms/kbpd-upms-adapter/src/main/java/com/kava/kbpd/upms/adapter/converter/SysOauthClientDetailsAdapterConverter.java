package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysOauthClientDetailsQuery;
import com.kava.kbpd.upms.api.model.request.SysOauthClientDetailsRequest;
import com.kava.kbpd.upms.api.model.response.SysOauthClientDetailsListResponse;
import com.kava.kbpd.upms.api.model.response.SysOauthClientDetailsResponse;
import com.kava.kbpd.upms.domain.model.entity.SysOauthClientDetailsEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientDetailsListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysOauthClientDetails转换器
 */
@Mapper(componentModel = "spring")
public interface SysOauthClientDetailsAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysOauthClientDetailsListQuery convertQueryDTO2QueryVal(SysOauthClientDetailsQuery request);

    @Mapping(source = "id.id", target = "id")
    SysOauthClientDetailsListResponse convertEntity2List(SysOauthClientDetailsEntity entity);

    @Mapping(source = "id", target = "id.id")
    SysOauthClientDetailsEntity convertRequest2Entity(SysOauthClientDetailsRequest req);

    @Mapping(source = "id.id", target = "id")
    SysOauthClientDetailsResponse convertEntity2Detail(SysOauthClientDetailsEntity sysOauthClientDetails);
}