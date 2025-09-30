package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysFileGroupAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysFileGroupRequest;
import com.kava.kbpd.upms.api.model.response.SysFileGroupDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysFileGroupListResponse;
import com.kava.kbpd.upms.application.model.command.SysFileGroupCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysFileGroupUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysFileGroupAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysFileGroupAppListDTO;
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
    SysFileGroupListQuery convertQueryDTO2QueryVal(SysFileGroupAdapterListQuery request);

    SysFileGroupListResponse convertEntity2List(SysFileGroupAppListDTO request);

    SysFileGroupDetailResponse convertEntity2Detail(SysFileGroupAppDetailDTO request);

    SysFileGroupCreateCommand convertRequest2CreateCommand(SysFileGroupRequest request);

    SysFileGroupUpdateCommand convertRequest2UpdateCommand(SysFileGroupRequest request);
}