package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysOauthClientDetailsQuery;
import com.kava.kbpd.upms.api.model.request.SysOauthClientDetailsRequest;
import com.kava.kbpd.upms.api.model.response.SysOauthClientDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysOauthClientListResponse;
import com.kava.kbpd.upms.domain.model.entity.SysOauthClientEntity;
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
    SysOauthClientListQuery convertQueryDTO2QueryVal(SysOauthClientDetailsQuery request);

    @Mapping(source = "id.id", target = "id")
    SysOauthClientListResponse convertEntity2List(SysOauthClientEntity entity);

    @Mapping(source = "id", target = "id.id")
    SysOauthClientEntity convertRequest2Entity(SysOauthClientDetailsRequest req);

    @Mapping(source = "id.id", target = "id")
    SysOauthClientDetailResponse convertEntity2Detail(SysOauthClientEntity sysOauthClientDetails);
}