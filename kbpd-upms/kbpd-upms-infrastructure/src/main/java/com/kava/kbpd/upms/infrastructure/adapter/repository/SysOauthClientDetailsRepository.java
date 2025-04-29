package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.permission.model.entity.SysOauthClientDetailsEntity;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysOauthClientDetailsId;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysOauthClientDetailsListQuery;
import com.kava.kbpd.upms.domain.permission.repository.ISysOauthClientDetailsRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysOauthClientDetailsConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysOauthClientDetailsMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysOauthClientDetailsPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysOauthClientDetailsRepository implements ISysOauthClientDetailsRepository {

    @Resource
    private SysOauthClientDetailsMapper sysOauthClientDetailsMapper;
    @Resource
    private SysOauthClientDetailsConverter sysOauthClientDetailsConverter;

    @Override
    public SysOauthClientDetailsId create(SysOauthClientDetailsEntity entity) {
        SysOauthClientDetailsPO sysOauthClientDetailsPO = sysOauthClientDetailsConverter.convertEntity2PO(entity);
        sysOauthClientDetailsMapper.insert(sysOauthClientDetailsPO);
        return SysOauthClientDetailsId.builder()
                .id(sysOauthClientDetailsPO.getId())
                .build();
    }

    @Override
    public Boolean update(SysOauthClientDetailsEntity entity) {
        SysOauthClientDetailsPO sysOauthClientDetailsPO = sysOauthClientDetailsConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysOauthClientDetailsMapper.updateById(sysOauthClientDetailsPO));
    }

    @Override
    public PagingInfo<SysOauthClientDetailsEntity> queryPage(SysOauthClientDetailsListQuery query) {
        Page<SysOauthClientDetailsPO> sysOauthClientDetailsPOPage = sysOauthClientDetailsMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysOauthClientDetailsPO.class));
        return PagingInfo.toResponse(sysOauthClientDetailsPOPage.getRecords().stream()
                        .map(sysOauthClientDetailsConverter::convertPO2Entity).toList(),
                sysOauthClientDetailsPOPage.getTotal(), sysOauthClientDetailsPOPage.getCurrent(), sysOauthClientDetailsPOPage.getSize());
    }

    @Override
    public SysOauthClientDetailsEntity queryById(SysOauthClientDetailsId id) {
        SysOauthClientDetailsPO sysOauthClientDetailsPO = sysOauthClientDetailsMapper.selectById(id.getId());
        return sysOauthClientDetailsConverter.convertPO2Entity(sysOauthClientDetailsPO);
    }

    @Override
    public Boolean removeBatchByIds(List<SysOauthClientDetailsId> ids) {
        List<Long> idList = ids.stream().map(SysOauthClientDetailsId::getId).toList();
        return SqlHelper.retBool(sysOauthClientDetailsMapper.deleteByIds(idList));
    }
}