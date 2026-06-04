package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.application.converter.SysUserAppConverter;
import com.kava.kbpd.upms.application.model.command.SysUserCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysUserUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysUserAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysUserAppListDTO;
import com.kava.kbpd.upms.application.service.ISysUserAppService;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import com.kava.kbpd.upms.domain.model.entity.SysGroupEntity;
import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;
import com.kava.kbpd.upms.domain.repository.ISysGroupRepository;
import com.kava.kbpd.upms.domain.repository.ISysRoleReadRepository;
import com.kava.kbpd.upms.domain.repository.ISysTenantRepository;
import com.kava.kbpd.upms.domain.repository.ISysUserReadRepository;
import com.kava.kbpd.upms.domain.repository.ISysUserWriteRepository;
import com.kava.kbpd.upms.domain.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Kris
 * @date 2025/9/29
 * @description: user application service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserAppService implements ISysUserAppService {
    private final ISysUserReadRepository readRepository;
    private final ISysUserWriteRepository writeRepository;
    private final ISysUserService sysUserService;
    private final SysUserAppConverter sysUserAppConverter;
    private final ISysGroupRepository sysGroupRepository;
    private final ISysTenantRepository sysTenantRepository;
    private final ISysRoleReadRepository sysRoleReadRepository;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUserId createUser(SysUserCreateCommand command) {
        command.setPassword(passwordEncoder.encode(command.getPassword()));
        return sysUserService.create(sysUserAppConverter.convertCreateCommand2Entity(command));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUserUpdateCommand command) {
        if (command.getPassword() != null && !command.getPassword().isEmpty()) {
            command.setPassword(passwordEncoder.encode(command.getPassword()));
        }
        sysUserService.update(sysUserAppConverter.convertUpdateCommand2Entity(command));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUserBatchByIds(List<SysUserId> ids) {
        sysUserService.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysUserAppListDTO> queryUserPage(SysUserListQuery query) {
        PagingInfo<SysUserEntity> pagingInfo = readRepository.queryPage(query);
        List<SysUserAppListDTO> convertList = pagingInfo.getList().stream().map(sysUserAppConverter::convertEntity2DTO).toList();

        enrichUserListDTOs(convertList, pagingInfo.getList());

        return PagingInfo.toResponse(convertList, pagingInfo);
    }

    @Override
    public SysUserAppDetailDTO queryUserById(SysUserId id) {
        SysUserEntity sysUserEntity = readRepository.queryById(id);
        SysUserAppDetailDTO dto = sysUserAppConverter.convertEntity2Detail(sysUserEntity);
        if (dto != null) {
            enrichUserDetailDTO(dto, sysUserEntity);
        }
        return dto;
    }

    @Override
    public SysUserAppDetailDTO queryUserByUsername(Long tenantId, String username) {
        SysUserEntity sysUserEntity = readRepository.queryByUsername(tenantId, username);
        if (sysUserEntity == null) {
            return null;
        }
        SysUserAppDetailDTO dto = sysUserAppConverter.convertEntity2Detail(sysUserEntity);
        enrichUserDetailDTO(dto, sysUserEntity);
        return dto;
    }

    @Override
    public List<String> queryRoleCodesByUserId(Long userId) {
        return readRepository.queryRoleCodesByUserId(userId);
    }

    @Override
    public List<String> queryPermissionsByUserId(Long userId) {
        return readRepository.queryPermissionsByUserId(userId);
    }

    @Override
    public String queryDataScopeByUserId(Long userId) {
        return readRepository.queryDataScopeByUserId(userId);
    }

    @Override
    public List<Long> queryMenuIdsByUserId(Long userId) {
        return readRepository.queryMenuIdsByUserId(userId);
    }

    private void enrichUserDetailDTO(SysUserAppDetailDTO dto, SysUserEntity entity) {
        if (dto.getGroupId() != null) {
            SysGroupEntity group = sysGroupRepository.queryById(SysGroupId.of(dto.getGroupId()));
            dto.setGroupName(group != null ? group.getName() : null);
        }
        if (dto.getTenantId() != null) {
            SysTenantEntity tenant = sysTenantRepository.queryById(SysTenantId.of(dto.getTenantId()));
            dto.setTenantName(tenant != null ? tenant.getName() : null);
        }
        if (entity.getRoleIds() != null && !entity.getRoleIds().isEmpty()) {
            List<Long> roleIdLongs = entity.getRoleIds().stream().map(SysRoleId::getId).toList();
            Map<Long, String> roleNameMap = sysRoleReadRepository.queryList(
                    com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery.builder().build()
            ).stream().collect(Collectors.toMap(r -> r.getId().getId(), r -> r.getRoleName(), (a, b) -> a));
            dto.setRoleNames(roleIdLongs.stream().map(roleNameMap::get).filter(Objects::nonNull).toList());
        }
    }

    private void enrichUserListDTOs(List<SysUserAppListDTO> dtos, List<SysUserEntity> entities) {
        List<Long> groupIds = entities.stream().map(e -> e.getGroupId() != null ? e.getGroupId().getId() : null).filter(Objects::nonNull).distinct().toList();
        List<Long> tenantIds = entities.stream().map(e -> e.getTenantId() != null ? e.getTenantId().getId() : null).filter(Objects::nonNull).distinct().toList();

        Map<Long, String> groupNameMap = groupIds.isEmpty() ? Collections.emptyMap() :
                sysGroupRepository.queryByIds(groupIds.stream().map(SysGroupId::of).toList())
                        .stream().collect(Collectors.toMap(d -> d.getId().getId(), d -> d.getName(), (a, b) -> a));
        Map<Long, String> tenantNameMap = tenantIds.isEmpty() ? Collections.emptyMap() :
                sysTenantRepository.queryByIds(tenantIds.stream().map(SysTenantId::of).toList())
                        .stream().collect(Collectors.toMap(t -> t.getId().getId(), t -> t.getName(), (a, b) -> a));

        for (int i = 0; i < dtos.size(); i++) {
            SysUserAppListDTO dto = dtos.get(i);
            SysUserEntity entity = entities.get(i);

            if (entity.getGroupId() != null) {
                dto.setGroupName(groupNameMap.get(entity.getGroupId().getId()));
            }
            if (entity.getTenantId() != null) {
                dto.setTenantName(tenantNameMap.get(entity.getTenantId().getId()));
            }
            if (entity.getRoleIds() != null) {
                dto.setRoleIds(entity.getRoleIds().stream().map(SysRoleId::getId).toList());
            }
        }
    }
}
