package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysMenuAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysMenuRequest;
import com.kava.kbpd.upms.api.model.response.SysMenuDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysMenuListResponse;
import com.kava.kbpd.upms.application.model.command.SysMenuCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysMenuUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysMenuAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysMenuAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysMenu转换器
 */
@Mapper(componentModel = "spring")
public interface SysMenuAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysMenuListQuery convertQueryDTO2QueryVal(SysMenuAdapterListQuery request);

    SysMenuListResponse convertEntity2List(SysMenuAppListDTO request);

    SysMenuDetailResponse convertEntity2Detail(SysMenuAppDetailDTO request);

    SysMenuCreateCommand convertRequest2CreateCommand(SysMenuRequest request);

    SysMenuUpdateCommand convertRequest2UpdateCommand(SysMenuRequest request);
}