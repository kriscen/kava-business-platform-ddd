package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.repository.ISysUserWriteRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysUserConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysUserMapper;
import com.kava.kbpd.upms.infrastructure.dao.SysUserRoleMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysUserPO;
import com.kava.kbpd.upms.infrastructure.dao.po.SysUserRolePO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysUserWriteRepository implements ISysUserWriteRepository {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserConverter sysUserConverter;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public SysUserId create(SysUserEntity entity) {
        SysUserPO sysUserPO = sysUserConverter.convertEntity2PO(entity);
        sysUserMapper.insert(sysUserPO);
        return SysUserId.builder()
                .id(sysUserPO.getId())
                .build();
    }

    @Override
    public Boolean update(SysUserEntity entity) {
        SysUserPO sysUserPO = sysUserConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysUserMapper.updateById(sysUserPO));
    }

    @Override
    public Boolean removeBatchByIds(List<SysUserId> ids) {
        List<Long> idList = ids.stream().map(SysUserId::getId).toList();
        return SqlHelper.retBool(sysUserMapper.deleteByIds(idList));
    }

    @Override
    public void saveUserRoles(SysUserId userId, List<SysRoleId> roleIds) {
        Long userIdVal = userId.getId();
        for (SysRoleId roleId : roleIds) {
            SysUserRolePO po = new SysUserRolePO();
            po.setUserId(userIdVal);
            po.setRoleId(roleId.getId());
            sysUserRoleMapper.insert(po);
        }
    }

    @Override
    public void removeUserRoles(SysUserId userId) {
        sysUserRoleMapper.delete(
                Wrappers.lambdaQuery(SysUserRolePO.class)
                        .eq(SysUserRolePO::getUserId, userId.getId()));
    }
}