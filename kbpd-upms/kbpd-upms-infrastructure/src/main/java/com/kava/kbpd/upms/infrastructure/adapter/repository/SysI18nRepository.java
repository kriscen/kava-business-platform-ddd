package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysI18nEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysI18nId;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysI18nListQuery;
import com.kava.kbpd.upms.domain.basic.repository.ISysI18nRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysI18nConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysI18nMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysI18nPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysI18nRepository implements ISysI18nRepository {

    @Resource
    private SysI18nMapper sysI18nMapper;
    @Resource
    private SysI18nConverter sysI18nConverter;
    @Override
    public SysI18nId create(SysI18nEntity entity) {
        SysI18nPO sysI18nPO = sysI18nConverter.convertEntity2PO(entity);
        sysI18nMapper.insert(sysI18nPO);
        return SysI18nId.builder()
                .id(sysI18nPO.getId())
                .build();
    }

    @Override
    public Boolean update(SysI18nEntity entity) {
        SysI18nPO sysI18nPO = sysI18nConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysI18nMapper.updateById(sysI18nPO));
    }

    @Override
    public PagingInfo<SysI18nEntity> queryPage(SysI18nListQuery query) {
        Page<SysI18nPO> sysI18nPOPage = sysI18nMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysI18nPO.class));
        return PagingInfo.toResponse(sysI18nPOPage.getRecords().stream()
                        .map(sysI18nConverter::convertPO2Entity).toList(),
                sysI18nPOPage.getTotal(), sysI18nPOPage.getCurrent(), sysI18nPOPage.getSize());
    }

    @Override
    public SysI18nEntity queryById(SysI18nId id) {
        SysI18nPO sysI18nPO = sysI18nMapper.selectById(id.getId());
        return sysI18nConverter.convertPO2Entity(sysI18nPO);
    }

    @Override
    public Boolean removeBatchByIds(List<SysI18nId> ids) {
        List<Long> idList = ids.stream().map(SysI18nId::getId).toList();
        return SqlHelper.retBool(sysI18nMapper.deleteByIds(idList));
    }
}