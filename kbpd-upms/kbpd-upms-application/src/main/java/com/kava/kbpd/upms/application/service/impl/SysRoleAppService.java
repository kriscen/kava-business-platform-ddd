package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.application.converter.SysRoleAppConverter;
import com.kava.kbpd.upms.application.model.command.SysRoleCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysRoleUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppListDTO;
import com.kava.kbpd.upms.application.service.ISysRoleAppService;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;
import com.kava.kbpd.upms.domain.repository.ISysMenuRepository;
import com.kava.kbpd.upms.domain.repository.ISysRoleReadRepository;
import com.kava.kbpd.upms.domain.repository.ISysRoleWriteRepository;
import com.kava.kbpd.upms.domain.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * @description: role application service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleAppService implements ISysRoleAppService {
    private final ISysRoleReadRepository readRepository;
    private final ISysRoleWriteRepository writeRepository;
    private final ISysRoleService sysRoleService;
    private final SysRoleAppConverter appConverter;
    private final ISysMenuRepository sysMenuRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysRoleId createRole(SysRoleCreateCommand command) {
        return sysRoleService.create(appConverter.convertCreateCommand2Entity(command));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRoleUpdateCommand command) {
        sysRoleService.update(appConverter.convertUpdateCommand2Entity(command));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRoleBatchByIds(List<SysRoleId> ids) {
        sysRoleService.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysRoleAppListDTO> queryRolePage(SysRoleListQuery query) {
        PagingInfo<SysRoleEntity> pagingInfo = readRepository.queryPage(query);
        List<SysRoleAppListDTO> convertList = pagingInfo.getList().stream().map(appConverter::convertEntity2DTO).toList();
        return PagingInfo.toResponse(convertList, pagingInfo);
    }

    @Override
    public SysRoleAppDetailDTO queryRoleById(SysRoleId id) {
        SysRoleEntity sysRoleEntity = readRepository.queryById(id);
        SysRoleAppDetailDTO dto = appConverter.convertEntity2Detail(sysRoleEntity);
        if (dto != null && dto.getMenuIds() != null && !dto.getMenuIds().isEmpty()) {
            List<SysMenuId> menuIds = dto.getMenuIds().stream().map(SysMenuId::of).toList();
            Map<Long, String> menuNameMap = sysMenuRepository.queryByIds(menuIds).stream()
                    .collect(Collectors.toMap(m -> m.getId().getId(), m -> m.getName(), (a, b) -> a));
            dto.setMenuNames(dto.getMenuIds().stream().map(menuNameMap::get).filter(Objects::nonNull).toList());
        }
        if (dto != null && dto.getMenuNames() == null) {
            dto.setMenuNames(Collections.emptyList());
        }
        return dto;
    }

    @Override
    public List<SysRoleAppListDTO> queryRoleDropdown(SysTenantId tenantId) {
        SysRoleListQuery query = SysRoleListQuery.builder()
                .tenantId(tenantId)
                .build();
        List<SysRoleEntity> roles = readRepository.queryList(query);
        return roles.stream().map(appConverter::convertEntity2DTO).toList();
    }
}
