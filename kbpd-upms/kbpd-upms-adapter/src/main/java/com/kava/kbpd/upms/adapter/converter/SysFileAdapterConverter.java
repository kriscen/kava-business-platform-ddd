package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.upms.api.model.query.SysFileAdapterListQuery;
import com.kava.kbpd.upms.api.model.query.SysFileAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysFileRequest;
import com.kava.kbpd.upms.api.model.request.SysFileRequest;
import com.kava.kbpd.upms.api.model.response.SysFileDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysFileListResponse;
import com.kava.kbpd.upms.api.model.response.SysFileListResponse;
import com.kava.kbpd.upms.api.model.response.SysFileDetailResponse;
import com.kava.kbpd.upms.application.model.command.SysFileCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysFileUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysFileAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysFileAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysFileEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysFileListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: 文件转换器
 */
@Mapper(componentModel = "spring")
public interface SysFileAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysFileListQuery convertQueryDTO2QueryVal(SysFileAdapterListQuery request);

    SysFileListResponse convertEntity2List(SysFileAppListDTO request);

    SysFileDetailResponse convertEntity2Detail(SysFileAppDetailDTO request);

    SysFileCreateCommand convertRequest2CreateCommand(SysFileRequest request);

    SysFileUpdateCommand convertRequest2UpdateCommand(SysFileRequest request);
}