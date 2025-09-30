package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysOauthClientEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientId;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientListQuery;
import com.kava.kbpd.upms.domain.repository.ISysOauthClientRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysOauthClientConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysOauthClientMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysOauthClientPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysOauthClientRepository implements ISysOauthClientRepository {

    @Resource
    private SysOauthClientMapper sysOauthClientDetailsMapper;
    @Resource
    private SysOauthClientConverter sysOauthClientDetailsConverter;

    @Override
    public SysOauthClientId create(SysOauthClientEntity entity) {
        SysOauthClientPO sysOauthClientDetailsPO = sysOauthClientDetailsConverter.convertEntity2PO(entity);
        sysOauthClientDetailsMapper.insert(sysOauthClientDetailsPO);
        return SysOauthClientId.builder()
                .id(sysOauthClientDetailsPO.getId())
                .build();
    }

    @Override
    public Boolean update(SysOauthClientEntity entity) {
        SysOauthClientPO sysOauthClientDetailsPO = sysOauthClientDetailsConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysOauthClientDetailsMapper.updateById(sysOauthClientDetailsPO));
    }

    @Override
    public PagingInfo<SysOauthClientEntity> queryPage(SysOauthClientListQuery query) {
        Page<SysOauthClientPO> sysOauthClientDetailsPOPage = sysOauthClientDetailsMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysOauthClientPO.class));
        return PagingInfo.toResponse(sysOauthClientDetailsPOPage.getRecords().stream()
                        .map(sysOauthClientDetailsConverter::convertPO2Entity).toList(),
                sysOauthClientDetailsPOPage.getTotal(), sysOauthClientDetailsPOPage.getCurrent(), sysOauthClientDetailsPOPage.getSize());
    }

    @Override
    public SysOauthClientEntity queryById(SysOauthClientId id) {
        SysOauthClientPO sysOauthClientDetailsPO = sysOauthClientDetailsMapper.selectById(id.getId());
        return sysOauthClientDetailsConverter.convertPO2Entity(sysOauthClientDetailsPO);
    }

    @Override
    public Boolean removeBatchByIds(List<SysOauthClientId> ids) {
        List<Long> idList = ids.stream().map(SysOauthClientId::getId).toList();
        return SqlHelper.retBool(sysOauthClientDetailsMapper.deleteByIds(idList));
    }
}