package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysDeptAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysDeptRequest;
import com.kava.kbpd.upms.api.model.response.SysDeptDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysDeptListResponse;
import com.kava.kbpd.upms.application.model.command.SysDeptCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysDeptUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysDeptAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysDeptAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: 部门转换器
 */
@Mapper(componentModel = "spring")
public interface SysDeptAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysDeptListQuery convertQueryDTO2QueryVal(SysDeptAdapterListQuery request);

    SysDeptListResponse convertEntity2List(SysDeptAppListDTO request);

    SysDeptDetailResponse convertEntity2Detail(SysDeptAppDetailDTO request);

    SysDeptCreateCommand convertRequest2CreateCommand(SysDeptRequest request);

    SysDeptUpdateCommand convertRequest2UpdateCommand(SysDeptRequest request);
}