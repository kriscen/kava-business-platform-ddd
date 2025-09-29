package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantListQuery;
import com.kava.kbpd.upms.domain.repository.ISysTenantRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysTenantConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysTenantMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysTenantPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysTenantRepository implements ISysTenantRepository {

    @Resource
    private SysTenantMapper sysTenantMapper;
    @Resource
    private SysTenantConverter sysTenantConverter;
    @Override
    public SysTenantId create(SysTenantEntity entity) {
        SysTenantPO sysTenantPO = sysTenantConverter.convertEntity2PO(entity);
        sysTenantMapper.insert(sysTenantPO);
        return SysTenantId.builder()
                .id(sysTenantPO.getId())
                .build();
    }

    @Override
    public Boolean update(SysTenantEntity entity) {
        SysTenantPO sysTenantPO = sysTenantConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysTenantMapper.updateById(sysTenantPO));
    }

    @Override
    public PagingInfo<SysTenantEntity> queryPage(SysTenantListQuery query) {
        Page<SysTenantPO> sysTenantPOPage = sysTenantMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysTenantPO.class));
        return PagingInfo.toResponse(sysTenantPOPage.getRecords().stream()
                        .map(sysTenantConverter::convertPO2Entity).toList(),
                sysTenantPOPage.getTotal(), sysTenantPOPage.getCurrent(), sysTenantPOPage.getSize());
    }

    @Override
    public SysTenantEntity queryById(SysTenantId id) {
        SysTenantPO sysTenantPO = sysTenantMapper.selectById(id.getId());
        return sysTenantConverter.convertPO2Entity(sysTenantPO);
    }

    @Override
    public Boolean removeBatchByIds(List<SysTenantId> ids) {
        List<Long> idList = ids.stream().map(SysTenantId::getId).toList();
        return SqlHelper.retBool(sysTenantMapper.deleteByIds(idList));
    }
}