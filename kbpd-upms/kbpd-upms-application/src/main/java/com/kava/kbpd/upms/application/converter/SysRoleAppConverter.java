package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.upms.application.model.command.SysRoleCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysRoleUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppListDTO;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: role转换器
 */
@Mapper(componentModel = "spring")
public interface SysRoleAppConverter {

    @Mapping(target = "menuIds", expression = "java(convertMenuIds(command.getMenuIds()))")
    SysRoleEntity convertCreateCommand2Entity(SysRoleCreateCommand command);

    @Mapping(target = "menuIds", expression = "java(convertMenuIds(command.getMenuIds()))")
    @Mapping(target = "id", expression = "java(com.kava.kbpd.upms.domain.model.valobj.SysRoleId.builder().id(command.getId()).build())")
    SysRoleEntity convertUpdateCommand2Entity(SysRoleUpdateCommand command);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "tenantId.id", target = "tenantId")
    @Mapping(target = "menuIds", expression = "java(convertMenuIdsToLong(entity.getMenuIds()))")
    SysRoleAppDetailDTO convertEntity2Detail(SysRoleEntity entity);

    SysRoleAppListDTO convertEntity2DTO(SysRoleEntity entity);

    default List<SysMenuId> convertMenuIds(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> SysMenuId.builder().id(id).build()).toList();
    }

    default List<Long> convertMenuIdsToLong(List<SysMenuId> menuIds) {
        if (menuIds == null) return null;
        return menuIds.stream().map(SysMenuId::getId).toList();
    }
}
