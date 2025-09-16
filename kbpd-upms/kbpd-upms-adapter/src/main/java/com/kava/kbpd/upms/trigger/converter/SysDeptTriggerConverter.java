package com.kava.kbpd.upms.trigger.converter;

import com.kava.kbpd.upms.api.model.query.SysDeptQuery;
import com.kava.kbpd.upms.api.model.request.SysDeptRequest;
import com.kava.kbpd.upms.api.model.response.SysDeptListResponse;
import com.kava.kbpd.upms.api.model.response.SysDeptResponse;
import com.kava.kbpd.upms.domain.user.model.entity.SysDeptEntity;
import com.kava.kbpd.upms.domain.user.model.valobj.SysDeptListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: 部门转换器
 */
@Mapper(componentModel = "spring")
public interface SysDeptTriggerConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysDeptListQuery convertQueryDTO2QueryVal(SysDeptQuery request);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    SysDeptListResponse convertEntity2List(SysDeptEntity entity);

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "pid", target = "pid.id")
    SysDeptEntity convertRequest2Entity(SysDeptRequest req);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    SysDeptResponse convertEntity2Detail(SysDeptEntity sysDept);
}