package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.repository.ISysRoleWriteRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysRoleConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysRoleMapper;
import com.kava.kbpd.upms.infrastructure.dao.SysRoleMenuMapper;
import com.kava.kbpd.upms.infrastructure.dao.SysUserRoleMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysRoleMenuPO;
import com.kava.kbpd.upms.infrastructure.dao.po.SysRolePO;
import com.kava.kbpd.upms.infrastructure.dao.po.SysUserRolePO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysRoleWriteRepository implements ISysRoleWriteRepository {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleConverter sysRoleConverter;
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public SysRoleId create(SysRoleEntity entity) {
        SysRolePO sysRolePO = sysRoleConverter.convertEntity2PO(entity);
        sysRoleMapper.insert(sysRolePO);
        return SysRoleId.builder()
                .id(sysRolePO.getId())
                .build();
    }

    @Override
    public Boolean update(SysRoleEntity entity) {
        SysRolePO sysRolePO = sysRoleConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysRoleMapper.updateById(sysRolePO));
    }

    @Override
    public Boolean removeBatchByIds(List<SysRoleId> ids) {
        List<Long> idList = ids.stream().map(SysRoleId::getId).toList();
        return SqlHelper.retBool(sysRoleMapper.deleteByIds(idList));
    }

    @Override
    public void saveRoleMenus(SysRoleId roleId, List<SysMenuId> menuIds) {
        Long roleIdVal = roleId.getId();
        for (SysMenuId menuId : menuIds) {
            SysRoleMenuPO po = new SysRoleMenuPO();
            po.setRoleId(roleIdVal);
            po.setMenuId(menuId.getId());
            sysRoleMenuMapper.insert(po);
        }
    }

    @Override
    public void removeRoleMenus(SysRoleId roleId) {
        sysRoleMenuMapper.delete(
                Wrappers.lambdaQuery(SysRoleMenuPO.class)
                        .eq(SysRoleMenuPO::getRoleId, roleId.getId()));
    }

    @Override
    public void removeUserRoleByRoleId(SysRoleId roleId) {
        sysUserRoleMapper.delete(
                Wrappers.lambdaQuery(SysUserRolePO.class)
                        .eq(SysUserRolePO::getRoleId, roleId.getId()));
    }
}