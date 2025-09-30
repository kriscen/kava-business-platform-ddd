package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysFileGroupQuery;
import com.kava.kbpd.upms.api.model.request.SysFileGroupRequest;
import com.kava.kbpd.upms.api.model.response.SysFileGroupDetailResponse;
import com.kava.kbpd.upms.domain.model.entity.SysFileGroupEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysFileGroupListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: 文件组转换器
 */
@Mapper(componentModel = "spring")
public interface SysFileGroupAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysFileGroupListQuery convertQueryDTO2QueryVal(SysFileGroupQuery request);

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "pid", target = "pid.id")
    SysFileGroupEntity convertRequest2Entity(SysFileGroupRequest req);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    SysFileGroupDetailResponse SysFileGroupResponse(SysFileGroupEntity sysFileGroup);
}