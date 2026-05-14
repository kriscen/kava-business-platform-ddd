package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;
import com.kava.kbpd.upms.domain.repository.ISysMenuRepository;
import com.kava.kbpd.upms.domain.repository.ISysRoleReadRepository;
import com.kava.kbpd.upms.domain.repository.ISysRoleWriteRepository;
import com.kava.kbpd.upms.domain.service.ISysRoleService;
import com.kava.kbpd.upms.types.enums.SysMenuScope;
import com.kava.kbpd.upms.types.exception.UpmsBizErrorCodeEnum;
import com.kava.kbpd.upms.types.exception.UpmsBizException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SysRoleService implements ISysRoleService {
    @Resource
    private ISysRoleWriteRepository writeRepository;

    @Resource
    private ISysRoleReadRepository readRepository;

    @Resource
    private ISysMenuRepository menuRepository;

    @Override
    public SysRoleId create(SysRoleEntity entity) {
        validateMenuBinding(entity);
        SysRoleId roleId = writeRepository.create(entity);
        if (!CollectionUtils.isEmpty(entity.getMenuIds())) {
            writeRepository.saveRoleMenus(roleId, entity.getMenuIds());
        }
        return roleId;
    }

    @Override
    public Boolean update(SysRoleEntity entity) {
        validateMenuBinding(entity);
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

    /**
     * 校验菜单绑定：menuIds 不能为空，且菜单 scope 必须在角色可见范围内
     */
    private void validateMenuBinding(SysRoleEntity entity) {
        if (CollectionUtils.isEmpty(entity.getMenuIds())) {
            throw new UpmsBizException(UpmsBizErrorCodeEnum.ROLE_MENU_EMPTY);
        }

        List<SysMenuEntity> menus = menuRepository.queryByIds(entity.getMenuIds());
        boolean isTenantRole = entity.getTenantId() != null;

        for (SysMenuEntity menu : menus) {
            String scope = menu.getScope();
            if (isTenantRole && SysMenuScope.SYSTEM.getCode().equals(scope)) {
                throw new UpmsBizException(UpmsBizErrorCodeEnum.MENU_SCOPE_INVALID);
            }
            if (!isTenantRole && SysMenuScope.TENANT.getCode().equals(scope)) {
                throw new UpmsBizException(UpmsBizErrorCodeEnum.MENU_SCOPE_INVALID);
            }
        }
    }
}