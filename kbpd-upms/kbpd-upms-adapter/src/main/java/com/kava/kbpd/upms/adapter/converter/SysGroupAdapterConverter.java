package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysGroupAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysGroupRequest;
import com.kava.kbpd.upms.api.model.response.SysGroupDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysGroupListResponse;
import com.kava.kbpd.upms.application.model.command.SysGroupCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysGroupUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysGroupAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysGroupAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: 分组转换器
 */
@Mapper(componentModel = "spring")
public interface SysGroupAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    @Mapping(source = "name", target = "groupName")
    SysGroupListQuery convertQueryDTO2QueryVal(SysGroupAdapterListQuery request);

    SysGroupListResponse convertEntity2List(SysGroupAppListDTO request);

    SysGroupDetailResponse convertEntity2Detail(SysGroupAppDetailDTO request);

    SysGroupCreateCommand convertRequest2CreateCommand(SysGroupRequest request);

    SysGroupUpdateCommand convertRequest2UpdateCommand(SysGroupRequest request);
}
