package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;
import com.kava.kbpd.upms.domain.repository.ISysRoleReadRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysRoleConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysRoleMapper;
import com.kava.kbpd.upms.infrastructure.dao.SysRoleMenuMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysRoleMenuPO;
import com.kava.kbpd.upms.infrastructure.dao.po.SysRolePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SysRoleReadRepository implements ISysRoleReadRepository {

    private final SysRoleMapper sysRoleMapper;
    private final SysRoleConverter sysRoleConverter;
    private final SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public PagingInfo<SysRoleEntity> queryPage(SysRoleListQuery query) {
        Page<SysRolePO> sysRolePOPage = sysRoleMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysRolePO.class)
                        .like(query.getRoleName() != null, SysRolePO::getRoleName, query.getRoleName())
                        .like(query.getRoleCode() != null, SysRolePO::getRoleCode, query.getRoleCode())
                        .eq(query.getTenantId() != null, SysRolePO::getTenantId,
                                query.getTenantId() != null ? query.getTenantId().getId() : null));
        return PagingInfo.toResponse(sysRolePOPage.getRecords().stream()
                        .map(sysRoleConverter::convertPO2Entity).toList(),
                sysRolePOPage.getTotal(), sysRolePOPage.getCurrent(), sysRolePOPage.getSize());
    }

    @Override
    public SysRoleEntity queryById(SysRoleId id) {
        SysRolePO sysRolePO = sysRoleMapper.selectById(id.getId());
        if (sysRolePO == null) {
            return null;
        }
        SysRoleEntity entity = sysRoleConverter.convertPO2Entity(sysRolePO);
        List<SysMenuId> menuIds = sysRoleMenuMapper.selectList(
                        Wrappers.lambdaQuery(SysRoleMenuPO.class)
                                .eq(SysRoleMenuPO::getRoleId, id.getId()))
                .stream()
                .map(po -> SysMenuId.builder().id(po.getMenuId()).build())
                .toList();
        entity.setMenuIds(menuIds);
        return entity;
    }

    @Override
    public SysRoleEntity queryByRoleCode(String roleCode, SysTenantId tenantId) {
        SysRolePO po = sysRoleMapper.selectOne(
                Wrappers.lambdaQuery(SysRolePO.class)
                        .eq(SysRolePO::getRoleCode, roleCode)
                        .eq(tenantId != null, SysRolePO::getTenantId,
                                tenantId != null ? tenantId.getId() : null));
        return po == null ? null : sysRoleConverter.convertPO2Entity(po);
    }

}
