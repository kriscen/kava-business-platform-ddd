package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;
import com.kava.kbpd.upms.domain.repository.ISysUserReadRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysUserConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysMenuMapper;
import com.kava.kbpd.upms.infrastructure.dao.SysRoleMapper;
import com.kava.kbpd.upms.infrastructure.dao.SysRoleMenuMapper;
import com.kava.kbpd.upms.infrastructure.dao.SysUserMapper;
import com.kava.kbpd.upms.infrastructure.dao.SysUserRoleMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysMenuPO;
import com.kava.kbpd.upms.infrastructure.dao.po.SysRoleMenuPO;
import com.kava.kbpd.upms.infrastructure.dao.po.SysRolePO;
import com.kava.kbpd.upms.infrastructure.dao.po.SysUserPO;
import com.kava.kbpd.upms.infrastructure.dao.po.SysUserRolePO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class SysUserReadRepository implements ISysUserReadRepository {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserConverter sysUserConverter;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public PagingInfo<SysUserEntity> queryPage(SysUserListQuery query) {
        Page<SysUserPO> sysUserPOPage = sysUserMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysUserPO.class));
        return PagingInfo.toResponse(sysUserPOPage.getRecords().stream()
                        .map(sysUserConverter::convertPO2Entity).toList(),
                sysUserPOPage.getTotal(), sysUserPOPage.getCurrent(), sysUserPOPage.getSize());
    }

    @Override
    public SysUserEntity queryById(SysUserId id) {
        SysUserPO sysUserPO = sysUserMapper.selectById(id.getId());
        return sysUserConverter.convertPO2Entity(sysUserPO);
    }

    @Override
    public SysUserEntity queryByUsername(Long tenantId, String username) {
        SysUserPO sysUserPO = sysUserMapper.selectOne(
                Wrappers.lambdaQuery(SysUserPO.class)
                        .eq(SysUserPO::getTenantId, tenantId)
                        .eq(SysUserPO::getUsername, username));
        if (sysUserPO == null) {
            return null;
        }
        return sysUserConverter.convertPO2Entity(sysUserPO);
    }

    @Override
    public List<String> queryRoleCodesByUserId(Long userId) {
        List<Long> roleIds = sysUserRoleMapper.selectList(
                        Wrappers.lambdaQuery(SysUserRolePO.class)
                                .eq(SysUserRolePO::getUserId, userId))
                .stream().map(SysUserRolePO::getRoleId).toList();

        if (roleIds.isEmpty()) {
            return Collections.emptyList();
        }

        return sysRoleMapper.selectBatchIds(roleIds).stream()
                .map(SysRolePO::getRoleCode)
                .toList();
    }

    @Override
    public List<String> queryPermissionsByUserId(Long userId) {
        List<Long> roleIds = sysUserRoleMapper.selectList(
                        Wrappers.lambdaQuery(SysUserRolePO.class)
                                .eq(SysUserRolePO::getUserId, userId))
                .stream().map(SysUserRolePO::getRoleId).toList();

        if (roleIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> menuIds = sysRoleMenuMapper.selectList(
                        Wrappers.lambdaQuery(SysRoleMenuPO.class)
                                .in(SysRoleMenuPO::getRoleId, roleIds))
                .stream().map(SysRoleMenuPO::getMenuId).distinct().toList();

        if (menuIds.isEmpty()) {
            return Collections.emptyList();
        }

        return sysMenuMapper.selectBatchIds(menuIds).stream()
                .map(SysMenuPO::getPermission)
                .filter(p -> p != null && !p.isEmpty())
                .distinct()
                .toList();
    }

}