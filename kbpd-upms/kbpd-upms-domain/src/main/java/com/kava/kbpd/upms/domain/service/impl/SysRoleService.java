package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;
import com.kava.kbpd.upms.domain.repository.ISysMenuRepository;
import com.kava.kbpd.upms.domain.repository.ISysRoleReadRepository;
import com.kava.kbpd.upms.domain.repository.ISysRoleWriteRepository;
import com.kava.kbpd.upms.domain.repository.ISysTenantAppRepository;
import com.kava.kbpd.upms.domain.service.ISysRoleService;
import com.kava.kbpd.upms.types.enums.SysMenuLevel;
import com.kava.kbpd.upms.types.enums.SysRoleDataScope;
import com.kava.kbpd.upms.types.exception.UpmsBizErrorCodeEnum;
import com.kava.kbpd.upms.types.exception.UpmsBizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleService implements ISysRoleService {

    private final ISysRoleWriteRepository writeRepository;
    private final ISysRoleReadRepository readRepository;
    private final ISysMenuRepository menuRepository;
    private final ISysTenantAppRepository tenantAppRepository;

    @Override
    public SysRoleId create(SysRoleEntity entity) {
        validateRoleCodeUnique(entity.getRoleCode(), entity.getTenantId(), null);
        validateMenuLevel(entity);
        SysRoleId roleId = writeRepository.create(entity);
        if (!CollectionUtils.isEmpty(entity.getMenuIds())) {
            writeRepository.saveRoleMenus(roleId, entity.getMenuIds());
        }
        return roleId;
    }

    @Override
    public Boolean update(SysRoleEntity entity) {
        validateRoleCodeUnique(entity.getRoleCode(), entity.getTenantId(), entity.getId());
        validateMenuLevel(entity);
        writeRepository.removeRoleMenus(entity.getId());
        if (!CollectionUtils.isEmpty(entity.getMenuIds())) {
            writeRepository.saveRoleMenus(entity.getId(), entity.getMenuIds());
        }
        return writeRepository.update(entity);
    }

    @Override
    public PagingInfo<SysRoleEntity> queryPage(SysRoleListQuery query) {
        return readRepository.queryPage(query);
    }

    @Override
    public SysRoleEntity queryById(SysRoleId id) {
        return readRepository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysRoleId> ids) {
        for (SysRoleId roleId : ids) {
            writeRepository.removeRoleMenus(roleId);
            writeRepository.removeUserRoleByRoleId(roleId);
        }
        return writeRepository.removeBatchByIds(ids);
    }

    @Override
    public SysRoleId initTenantAdminRole(SysTenantId tenantId) {
        List<Long> menuIds = tenantAppRepository.queryMenuIdsByTenantId(tenantId);

        List<SysMenuId> sysMenuIds = menuIds.stream()
                .map(id -> SysMenuId.builder().id(id).build())
                .toList();

        SysRoleEntity adminRole = SysRoleEntity.builder()
                .roleName("租户管理员")
                .roleCode("tenant_admin")
                .roleDesc("租户创建时自动生成的管理员角色")
                .dsType(SysRoleDataScope.ALL.getCode())
                .menuIds(sysMenuIds)
                .tenantId(tenantId)
                .build();

        SysRoleId roleId = writeRepository.create(adminRole);
        if (!CollectionUtils.isEmpty(sysMenuIds)) {
            writeRepository.saveRoleMenus(roleId, sysMenuIds);
        }
        return roleId;
    }

    private void validateRoleCodeUnique(String roleCode, SysTenantId tenantId, SysRoleId excludeId) {
        SysRoleEntity existing = readRepository.queryByRoleCode(roleCode, tenantId);
        if (existing != null && !existing.getId().equals(excludeId)) {
            throw new UpmsBizException(UpmsBizErrorCodeEnum.ROLE_CODE_DUPLICATE);
        }
    }

    private void validateMenuLevel(SysRoleEntity entity) {
        if (CollectionUtils.isEmpty(entity.getMenuIds())) {
            return;
        }

        List<SysMenuEntity> menus = menuRepository.queryByIds(entity.getMenuIds());
        boolean isTenantRole = entity.getTenantId() != null;

        if (isTenantRole) {
            List<Long> allowedMenuIds = tenantAppRepository.queryMenuIdsByTenantId(entity.getTenantId());
            for (SysMenuEntity menu : menus) {
                if (SysMenuLevel.PLATFORM.equals(menu.getLevel())) {
                    throw new UpmsBizException(UpmsBizErrorCodeEnum.MENU_SCOPE_INVALID);
                }
                if (!allowedMenuIds.contains(menu.getId().getId())) {
                    throw new UpmsBizException(UpmsBizErrorCodeEnum.TENANT_APP_MENU_OUT_OF_SCOPE);
                }
            }
        } else {
            for (SysMenuEntity menu : menus) {
                if (SysMenuLevel.TENANT.equals(menu.getLevel())) {
                    throw new UpmsBizException(UpmsBizErrorCodeEnum.MENU_SCOPE_INVALID);
                }
            }
        }
    }
}
