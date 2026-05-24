package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.application.converter.SysTenantAppConverter;
import com.kava.kbpd.upms.application.model.command.SysTenantCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysTenantUpdateCommand;
import com.kava.kbpd.upms.application.model.command.SysUserCreateCommand;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppListDTO;
import com.kava.kbpd.upms.application.model.dto.TenantStatusAppDTO;
import com.kava.kbpd.upms.application.service.ISysTenantAppService;
import com.kava.kbpd.upms.application.service.ISysUserAppService;
import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantListQuery;
import com.kava.kbpd.upms.domain.service.ISysRoleService;
import com.kava.kbpd.upms.domain.service.ISysTenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysTenantAppService implements ISysTenantAppService {
    private final ISysTenantService sysTenantService;
    private final ISysRoleService sysRoleService;
    private final ISysUserAppService sysUserAppService;
    private final SysTenantAppConverter sysTenantAppConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysTenantId createTenant(SysTenantCreateCommand command) {
        SysTenantEntity sysTenantEntity = sysTenantAppConverter.convertCreateCommand2Entity(command);
        SysTenantId tenantId = sysTenantService.create(sysTenantEntity);
        SysRoleId adminRoleId = sysRoleService.initTenantAdminRole(tenantId, sysTenantEntity.getMenuId());

        if (StringUtils.hasText(command.getAdminUsername())) {
            SysUserCreateCommand userCommand = SysUserCreateCommand.builder()
                    .username(command.getAdminUsername())
                    .password(command.getAdminPassword())
                    .tenantId(tenantId.getId())
                    .roleIds(Collections.singletonList(adminRoleId.getId()))
                    .build();
            sysUserAppService.createUser(userCommand);
        }

        return tenantId;
    }

    @Override
    public void updateTenant(SysTenantUpdateCommand command) {
        SysTenantEntity sysTenantEntity = sysTenantAppConverter.convertUpdateCommand2Entity(command);
        sysTenantService.update(sysTenantEntity);
    }

    @Override
    public void removeTenantBatchByIds(List<SysTenantId> ids) {
        sysTenantService.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysTenantAppListDTO> queryTenantPage(SysTenantListQuery query) {
        PagingInfo<SysTenantEntity> sysTenantEntityPagingInfo = sysTenantService.queryPage(query);
        List<SysTenantAppListDTO> collect = sysTenantEntityPagingInfo.getList().stream()
                .map(sysTenantAppConverter::convertEntityToListQueryDTO).toList();
        return PagingInfo.toResponse(collect, sysTenantEntityPagingInfo);
    }

    @Override
    public SysTenantAppDetailDTO queryTenantById(SysTenantId id) {
        SysTenantEntity tenantEntity = sysTenantService.queryById(id);
        return sysTenantAppConverter.convertEntityToDetailDTO(tenantEntity);
    }

    @Override
    public void enableTenant(SysTenantId id) {
        sysTenantService.enable(id);
    }

    @Override
    public void disableTenant(SysTenantId id) {
        sysTenantService.disable(id);
    }

    @Override
    public TenantStatusAppDTO checkTenantStatus(SysTenantId id) {
        SysTenantEntity entity = sysTenantService.queryById(id);
        if (entity == null) {
            return null;
        }
        TenantStatusAppDTO dto = new TenantStatusAppDTO();
        dto.setStatus(sysTenantService.queryEffectiveStatus(id).getCode());
        dto.setExpired(entity.isExpired());
        return dto;
    }

    @Override
    public List<SysTenantAppListDTO> queryTenantDropdown() {
        List<SysTenantEntity> tenants = sysTenantService.queryAll();
        return tenants.stream().map(sysTenantAppConverter::convertEntityToListQueryDTO).toList();
    }
}
