package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysRouteConfEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfId;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfListQuery;
import com.kava.kbpd.upms.domain.repository.ISysRouteConfRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysRouteConfConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysRouteConfMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysRouteConfPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysRouteConfRepository implements ISysRouteConfRepository {

    @Resource
    private SysRouteConfMapper sysRouteConfMapper;
    @Resource
    private SysRouteConfConverter sysRouteConfConverter;
    @Override
    public SysRouteConfId create(SysRouteConfEntity entity) {
        SysRouteConfPO sysRouteConfPO = sysRouteConfConverter.convertEntity2PO(entity);
        sysRouteConfMapper.insert(sysRouteConfPO);
        return SysRouteConfId.builder()
                .id(sysRouteConfPO.getId())
                .build();
    }

    @Override
    public Boolean update(SysRouteConfEntity entity) {
        SysRouteConfPO sysRouteConfPO = sysRouteConfConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysRouteConfMapper.updateById(sysRouteConfPO));
    }

    @Override
    public PagingInfo<SysRouteConfEntity> queryPage(SysRouteConfListQuery query) {
        Page<SysRouteConfPO> sysRouteConfPOPage = sysRouteConfMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysRouteConfPO.class));
        return PagingInfo.toResponse(sysRouteConfPOPage.getRecords().stream()
                        .map(sysRouteConfConverter::convertPO2Entity).toList(),
                sysRouteConfPOPage.getTotal(), sysRouteConfPOPage.getCurrent(), sysRouteConfPOPage.getSize());
    }

    @Override
    public SysRouteConfEntity queryById(SysRouteConfId id) {
        SysRouteConfPO sysRouteConfPO = sysRouteConfMapper.selectById(id.getId());
        return sysRouteConfConverter.convertPO2Entity(sysRouteConfPO);
    }

    @Override
    public Boolean removeBatchByIds(List<SysRouteConfId> ids) {
        List<Long> idList = ids.stream().map(SysRouteConfId::getId).toList();
        return SqlHelper.retBool(sysRouteConfMapper.deleteByIds(idList));
    }
}