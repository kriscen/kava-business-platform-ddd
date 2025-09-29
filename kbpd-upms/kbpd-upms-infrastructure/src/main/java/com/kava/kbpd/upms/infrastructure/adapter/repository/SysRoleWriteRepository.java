package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.repository.ISysRoleWriteRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysRoleConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysRoleMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysRolePO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysRoleWriteRepository implements ISysRoleWriteRepository {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleConverter sysRoleConverter;

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
}