package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.common.core.enums.Status;
import com.kava.kbpd.upms.domain.model.entity.SysAreaEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysAreaPO;
import com.kava.kbpd.upms.types.enums.SysAreaType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysArea转换器
 */
@Mapper(componentModel = "spring")
public interface SysAreaConverter {

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "pid.id", target = "pid")
    SysAreaPO convertEntity2PO(SysAreaEntity request);

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "pid", target = "pid.id")
    SysAreaEntity convertPO2Entity(SysAreaPO request);

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
