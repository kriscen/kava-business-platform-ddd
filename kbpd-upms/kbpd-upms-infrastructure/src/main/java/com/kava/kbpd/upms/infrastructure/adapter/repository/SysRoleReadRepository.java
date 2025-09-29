package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;
import com.kava.kbpd.upms.domain.repository.ISysRoleReadRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysRoleConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysRoleMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysRolePO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class SysRoleReadRepository implements ISysRoleReadRepository {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleConverter sysRoleConverter;

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

}