package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantListQuery;
import com.kava.kbpd.upms.domain.repository.ISysRoleWriteRepository;
import com.kava.kbpd.upms.domain.repository.ISysTenantRepository;
import com.kava.kbpd.upms.domain.service.ISysTenantService;
import com.kava.kbpd.upms.types.enums.SysRoleDataScope;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class SysTenantService implements ISysTenantService {
    @Resource
    private ISysTenantRepository repository;
    @Resource
    private ISysRoleWriteRepository roleWriteRepository;

    @Override
    public SysTenantId create(SysTenantEntity entity) {
        SysTenantId tenantId = repository.create(entity);
        // 自动创建租户管理员角色
        initTenantAdminRole(tenantId, entity.getMenuId());
        return tenantId;
    }

    @Override
    public Boolean update(SysTenantEntity entity) {
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysTenantEntity> queryPage(SysTenantListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysTenantEntity queryById(SysTenantId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysTenantId> ids) {
        return repository.removeBatchByIds(ids);
    }

    /**
     * 初始化租户管理员角色：创建 tenant_admin 角色，关联所有已分配菜单
     */
    private void initTenantAdminRole(SysTenantId tenantId, String menuIdStr) {
        List<SysMenuId> menuIds = Collections.emptyList();
        if (StringUtils.hasText(menuIdStr)) {
            menuIds = Arrays.stream(menuIdStr.split(","))
                    .map(String::trim)
                    .filter(StringUtils::hasText)
                    .map(Long::parseLong)
                    .map(id -> SysMenuId.builder().id(id).build())
                    .toList();
        }

        SysRoleEntity adminRole = SysRoleEntity.builder()
                .roleName("租户管理员")
                .roleCode("tenant_admin")
                .roleDesc("租户创建时自动生成的管理员角色")
                .dsType(Integer.valueOf(SysRoleDataScope.ALL.getCode()))
                .menuIds(menuIds)
                .tenantId(tenantId)
                .build();

        roleWriteRepository.create(adminRole);
    }
}