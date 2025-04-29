package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysAuditLogEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAuditLogId;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAuditLogListQuery;
import com.kava.kbpd.upms.domain.basic.repository.ISysAuditLogRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysAuditLogConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysAuditLogMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysAuditLogPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysAuditLogRepository implements ISysAuditLogRepository {

    @Resource
    private SysAuditLogMapper sysAuditLogMapper;
    @Resource
    private SysAuditLogConverter sysAuditLogConverter;

    @Override
    public SysAuditLogId create(SysAuditLogEntity entity) {
        SysAuditLogPO SysAuditLogPO = sysAuditLogConverter.convertEntity2PO(entity);
        sysAuditLogMapper.insert(SysAuditLogPO);
        return SysAuditLogId.builder()
                .id(SysAuditLogPO.getId())
                .build();
    }

    @Override
    public Boolean update(SysAuditLogEntity entity) {
        SysAuditLogPO sysAuditLog = sysAuditLogConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysAuditLogMapper.updateById(sysAuditLog));
    }

    @Override
    public PagingInfo<SysAuditLogEntity> queryPage(SysAuditLogListQuery query) {
        Page<SysAuditLogPO> SysAuditLogPOPage = sysAuditLogMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysAuditLogPO.class));
        return PagingInfo.toResponse(SysAuditLogPOPage.getRecords().stream()
                        .map(sysAuditLogConverter::convertPO2Entity).toList(),
                SysAuditLogPOPage.getTotal(), SysAuditLogPOPage.getCurrent(), SysAuditLogPOPage.getSize());
    }

    @Override
    public SysAuditLogEntity queryById(SysAuditLogId id) {
        SysAuditLogPO sysAuditLogPO = sysAuditLogMapper.selectById(id.getId());
        return sysAuditLogConverter.convertPO2Entity(sysAuditLogPO);
    }


    @Override
    public Boolean removeBatchByIds(List<SysAuditLogId> ids) {
        List<Long> idList = ids.stream().map(SysAuditLogId::getId).toList();
        return SqlHelper.retBool(sysAuditLogMapper.deleteByIds(idList));
    }
}