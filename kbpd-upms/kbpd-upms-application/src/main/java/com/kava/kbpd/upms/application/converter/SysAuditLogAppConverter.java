package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysAuditLogCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysAuditLogUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysAuditLogAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAuditLogAppListDTO;
import com.kava.kbpd.upms.domain.model.entity.SysAuditLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysAuditLog转换器
 */
@Mapper(componentModel = "spring")
public interface SysAuditLogAppConverter {

//    @Mapping(source = "pageSize", target = "queryParam.pageSize")
    SysAuditLogEntity convertCreateCommand2Entity(SysAuditLogCreateCommand command);

    SysAuditLogEntity convertUpdateCommand2Entity(SysAuditLogUpdateCommand command);

    SysAuditLogAppListDTO convertEntityToListQueryDTO(SysAuditLogEntity entity);

    SysAuditLogAppDetailDTO convertEntityToDetailDTO(SysAuditLogEntity entity);

}
