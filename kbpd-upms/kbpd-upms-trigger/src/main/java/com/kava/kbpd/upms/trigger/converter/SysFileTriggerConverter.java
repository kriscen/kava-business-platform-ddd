package com.kava.kbpd.upms.trigger.converter;

import com.kava.kbpd.upms.api.model.query.SysFileQuery;
import com.kava.kbpd.upms.api.model.request.SysFileRequest;
import com.kava.kbpd.upms.api.model.response.SysFileListResponse;
import com.kava.kbpd.upms.api.model.response.SysFileResponse;
import com.kava.kbpd.upms.domain.basic.model.entity.SysFileEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysFileListQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: 文件转换器
 */
@Mapper(componentModel = "spring")
public interface SysFileTriggerConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysFileListQuery convertQueryDTO2QueryVal(SysFileQuery request);

    @Mapping(source = "id.id", target = "id")
    SysFileListResponse convertEntity2List(SysFileEntity entity);

    @Mapping(source = "id", target = "id.id")
    SysFileEntity convertRequest2Entity(SysFileRequest req);

    @Mapping(source = "id.id", target = "id")
    SysFileResponse convertEntity2Detail(SysFileEntity sysFile);
}