package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;
import com.kava.kbpd.upms.domain.repository.ISysUserReadRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysUserConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysUserMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysUserPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class SysUserReadRepository implements ISysUserReadRepository {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserConverter sysUserConverter;

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

}