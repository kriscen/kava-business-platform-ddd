package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysUserCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysUserUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysUserAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysUserAppListDTO;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import org.mapstruct.Mapper;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: user转换器
 */
@Mapper(componentModel = "spring")
public interface SysUserAppConverter {

    SysUserEntity convertCreateCommand2Entity(SysUserCreateCommand command);

    SysUserEntity convertUpdateCommand2Entity(SysUserUpdateCommand command);

    SysUserAppListDTO convertEntity2DTO(SysUserEntity sysUserEntity);

    SysUserAppDetailDTO convertEntity2Detail(SysUserEntity sysUserEntity);
}
