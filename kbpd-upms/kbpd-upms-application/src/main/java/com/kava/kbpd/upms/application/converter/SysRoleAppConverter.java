package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysRoleCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysRoleUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppListDTO;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import org.mapstruct.Mapper;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: role转换器
 */
@Mapper(componentModel = "spring")
public interface SysRoleAppConverter {

    SysRoleEntity convertCreateCommand2Entity(SysRoleCreateCommand command);

    SysRoleEntity convertUpdateCommand2Entity(SysRoleUpdateCommand command);

    SysRoleAppListDTO convertEntity2DTO(SysRoleEntity sysUserEntity);

    SysRoleAppDetailDTO convertEntity2Detail(SysRoleEntity sysUserEntity);
}
