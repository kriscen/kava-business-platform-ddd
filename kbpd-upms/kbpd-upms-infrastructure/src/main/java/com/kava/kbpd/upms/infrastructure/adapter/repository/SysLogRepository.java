package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysLogEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysLogId;
import com.kava.kbpd.upms.domain.model.valobj.SysLogListQuery;
import com.kava.kbpd.upms.domain.repository.ISysLogRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysLogConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysLogMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysLogPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysLogRepository implements ISysLogRepository {

    @Resource
    private SysLogMapper sysLogMapper;
    @Resource
    private SysLogConverter sysLogConverter;
    @Override
    public SysLogId create(SysLogEntity entity) {
        SysLogPO sysLogPO = sysLogConverter.convertEntity2PO(entity);
        sysLogMapper.insert(sysLogPO);
        return SysLogId.builder()
                .id(sysLogPO.getId())
                .build();
    }

    @Override
    public Boolean update(SysLogEntity entity) {
        SysLogPO sysLogPO = sysLogConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysLogMapper.updateById(sysLogPO));
    }

    @Override
    public PagingInfo<SysLogEntity> queryPage(SysLogListQuery query) {
        Page<SysLogPO> sysLogPOPage = sysLogMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysLogPO.class));
        return PagingInfo.toResponse(sysLogPOPage.getRecords().stream()
                        .map(sysLogConverter::convertPO2Entity).toList(),
                sysLogPOPage.getTotal(), sysLogPOPage.getCurrent(), sysLogPOPage.getSize());
    }

    @Override
    public SysLogEntity queryById(SysLogId id) {
        SysLogPO sysLogPO = sysLogMapper.selectById(id.getId());
        return sysLogConverter.convertPO2Entity(sysLogPO);
    }

    @Override
    public Boolean removeBatchByIds(List<SysLogId> ids) {
        List<Long> idList = ids.stream().map(SysLogId::getId).toList();
        return SqlHelper.retBool(sysLogMapper.deleteByIds(idList));
    }
}