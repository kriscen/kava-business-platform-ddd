package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kava.kbpd.common.core.base.PagingInfo;
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
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysRoleReadRepository implements ISysRoleReadRepository {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleConverter sysRoleConverter;
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public PagingInfo<SysRoleEntity> queryPage(SysRoleListQuery query) {
        Page<SysRolePO> sysRolePOPage = sysRoleMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysRolePO.class));
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
        // 查询关联的菜单ID列表
        List<SysMenuId> menuIds = sysRoleMenuMapper.selectList(
                        Wrappers.lambdaQuery(SysRoleMenuPO.class)
                                .eq(SysRoleMenuPO::getRoleId, id.getId()))
                .stream()
                .map(po -> SysMenuId.builder().id(po.getMenuId()).build())
                .toList();
        entity.setMenuIds(menuIds);
        return entity;
    }

}