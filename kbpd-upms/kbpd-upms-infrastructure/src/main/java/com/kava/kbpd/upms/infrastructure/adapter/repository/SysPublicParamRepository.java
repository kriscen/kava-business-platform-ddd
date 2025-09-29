package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysPublicParamEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamId;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamListQuery;
import com.kava.kbpd.upms.domain.repository.ISysPublicParamRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysPublicParamConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysPublicParamMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysPublicParamPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysPublicParamRepository implements ISysPublicParamRepository {

    @Resource
    private SysPublicParamMapper sysPublicParamMapper;
    @Resource
    private SysPublicParamConverter sysPublicParamConverter;
    @Override
    public SysPublicParamId create(SysPublicParamEntity entity) {
        SysPublicParamPO sysPublicParamPO = sysPublicParamConverter.convertEntity2PO(entity);
        sysPublicParamMapper.insert(sysPublicParamPO);
        return SysPublicParamId.builder()
                .id(sysPublicParamPO.getId())
                .build();
    }

    @Override
    public Boolean update(SysPublicParamEntity entity) {
        SysPublicParamPO sysPublicParamPO = sysPublicParamConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysPublicParamMapper.updateById(sysPublicParamPO));
    }

    @Override
    public PagingInfo<SysPublicParamEntity> queryPage(SysPublicParamListQuery query) {
        Page<SysPublicParamPO> sysPublicParamPOPage = sysPublicParamMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysPublicParamPO.class));
        return PagingInfo.toResponse(sysPublicParamPOPage.getRecords().stream()
                        .map(sysPublicParamConverter::convertPO2Entity).toList(),
                sysPublicParamPOPage.getTotal(), sysPublicParamPOPage.getCurrent(), sysPublicParamPOPage.getSize());
    }

    @Override
    public SysPublicParamEntity queryById(SysPublicParamId id) {
        SysPublicParamPO sysPublicParamPO = sysPublicParamMapper.selectById(id.getId());
        return sysPublicParamConverter.convertPO2Entity(sysPublicParamPO);
    }

    @Override
    public Boolean removeBatchByIds(List<SysPublicParamId> ids) {
        List<Long> idList = ids.stream().map(SysPublicParamId::getId).toList();
        return SqlHelper.retBool(sysPublicParamMapper.deleteByIds(idList));
    }
}