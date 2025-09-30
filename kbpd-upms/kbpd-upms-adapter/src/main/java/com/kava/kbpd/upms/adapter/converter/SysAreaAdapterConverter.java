package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysAreaQuery;
import com.kava.kbpd.upms.api.model.request.SysAreaRequest;
import com.kava.kbpd.upms.api.model.response.SysAreaDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysAreaListResponse;
import com.kava.kbpd.upms.application.model.command.SysAreaCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysAreaUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysAreaAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAreaAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysArea转换器
 */
@Mapper(componentModel = "spring")
public interface SysAreaAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysAreaListQuery convertQueryDTO2QueryVal(SysAreaQuery request);

    SysAreaListResponse convertEntity2List(SysAreaAppListDTO request);

    SysAreaDetailResponse convertEntity2Detail(SysAreaAppDetailDTO request);

    @Mapping(source = "pid", target = "pid.id")
    SysAreaCreateCommand convertRequest2CreateCommand(SysAreaRequest request);

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "pid", target = "pid.id")
    SysAreaUpdateCommand convertRequest2UpdateCommand(SysAreaRequest request);
}
