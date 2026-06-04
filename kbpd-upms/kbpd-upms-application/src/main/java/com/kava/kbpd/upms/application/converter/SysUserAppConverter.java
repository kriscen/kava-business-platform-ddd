package com.kava.kbpd.upms.application.converter;

import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.application.model.command.SysUserCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysUserUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysUserAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysUserAppListDTO;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: user转换器
 */
@Mapper(componentModel = "spring")
public interface SysUserAppConverter {

    @Mapping(target = "roleIds", expression = "java(convertRoleIds(command.getRoleIds()))")
    @Mapping(target = "groupId", expression = "java(convertGroupId(command.getGroupId()))")
    @Mapping(target = "tenantId", expression = "java(convertTenantId(command.getTenantId()))")
    SysUserEntity convertCreateCommand2Entity(SysUserCreateCommand command);

    @Mapping(target = "roleIds", expression = "java(convertRoleIds(command.getRoleIds()))")
    @Mapping(target = "id", expression = "java(com.kava.kbpd.common.core.model.valobj.SysUserId.builder().id(command.getId()).build())")
    @Mapping(target = "groupId", expression = "java(convertGroupId(command.getGroupId()))")
    @Mapping(target = "tenantId", expression = "java(convertTenantId(command.getTenantId()))")
    SysUserEntity convertUpdateCommand2Entity(SysUserUpdateCommand command);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "groupId.id", target = "groupId")
    @Mapping(source = "tenantId.id", target = "tenantId")
    @Mapping(target = "roleIds", expression = "java(convertRoleIdsToLong(sysUserEntity.getRoleIds()))")
    @Mapping(target = "groupName", ignore = true)
    @Mapping(target = "tenantName", ignore = true)
    SysUserAppListDTO convertEntity2DTO(SysUserEntity sysUserEntity);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "tenantId.id", target = "tenantId")
    @Mapping(source = "groupId.id", target = "groupId")
    @Mapping(target = "roleIds", expression = "java(convertRoleIdsToLong(entity.getRoleIds()))")
    SysUserAppDetailDTO convertEntity2Detail(SysUserEntity entity);

    default List<SysRoleId> convertRoleIds(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> SysRoleId.builder().id(id).build()).toList();
    }

    default List<Long> convertRoleIdsToLong(List<SysRoleId> roleIds) {
        if (roleIds == null) return null;
        return roleIds.stream().map(SysRoleId::getId).toList();
    }

    default SysGroupId convertGroupId(Long id) {
        return id == null ? null : SysGroupId.builder().id(id).build();
    }

    default SysTenantId convertTenantId(Long id) {
        return id == null ? null : SysTenantId.builder().id(id).build();
    }
}
