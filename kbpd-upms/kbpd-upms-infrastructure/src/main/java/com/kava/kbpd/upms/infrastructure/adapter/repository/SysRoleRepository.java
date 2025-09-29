package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;
import com.kava.kbpd.upms.domain.repository.ISysRoleRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysRoleConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysRoleMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysRolePO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysRoleRepository implements ISysRoleRepository {

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
        return sysRoleConverter.convertPO2Entity(sysRolePO);
    }

    @Override
    public Boolean removeBatchByIds(List<SysRoleId> ids) {
        List<Long> idList = ids.stream().map(SysRoleId::getId).toList();
        return SqlHelper.retBool(sysRoleMapper.deleteByIds(idList));
    }
}