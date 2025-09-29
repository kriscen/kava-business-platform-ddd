package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysUserEntity;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;
import com.kava.kbpd.upms.domain.repository.ISysUserRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysUserConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysUserMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysUserPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysUserRepository implements ISysUserRepository {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserConverter sysUserConverter;

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
    public Boolean removeBatchByIds(List<SysUserId> ids) {
        List<Long> idList = ids.stream().map(SysUserId::getId).toList();
        return SqlHelper.retBool(sysUserMapper.deleteByIds(idList));
    }
}