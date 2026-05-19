package com.kava.kbpd.upms.adapter.converter;

import com.kava.kbpd.common.core.enums.Status;
import com.kava.kbpd.upms.api.model.query.SysAreaAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysAreaRequest;
import com.kava.kbpd.upms.api.model.response.SysAreaDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysAreaListResponse;
import com.kava.kbpd.upms.application.model.command.SysAreaCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysAreaUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysAreaAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAreaAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaListQuery;
import com.kava.kbpd.upms.types.enums.SysAreaType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.List;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysArea转换器
 */
@Mapper(componentModel = "spring")
public interface SysAreaAdapterConverter {

    @Mapping(source = "pageNo", target = "queryParam.pageNo")
    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    @Mapping(source = "areaType", target = "areaTypes", qualifiedByName = "splitAreaType")
    SysAreaListQuery convertQueryDTO2QueryVal(SysAreaAdapterListQuery request);

    SysAreaListResponse convertEntity2List(SysAreaAppListDTO request);

    SysAreaDetailResponse convertEntity2Detail(SysAreaAppDetailDTO request);

    @Mapping(source = "pid", target = "pid.id")
    SysAreaCreateCommand convertRequest2CreateCommand(SysAreaRequest request);

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "pid", target = "pid.id")
    SysAreaUpdateCommand convertRequest2UpdateCommand(SysAreaRequest request);

    @Named("splitAreaType")
    default List<String> splitAreaType(String areaType) {
        if (areaType == null || areaType.isBlank()) return null;
        return Arrays.stream(areaType.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
    }

    default SysAreaType stringToSysAreaType(String code) {
        if (code == null) return null;
        for (SysAreaType type : SysAreaType.values()) {
            if (type.getCode().equals(code)) return type;
        }
        return null;
    }

    default String sysAreaTypeToString(SysAreaType type) {
        return type == null ? null : type.getCode();
    }

    default Status stringToStatus(String code) {
        if (code == null) return null;
        for (Status s : Status.values()) {
            if (s.getCode().equals(code)) return s;
        }
        return null;
    }

    default String statusToString(Status status) {
        return status == null ? null : status.getCode();
    }
}
