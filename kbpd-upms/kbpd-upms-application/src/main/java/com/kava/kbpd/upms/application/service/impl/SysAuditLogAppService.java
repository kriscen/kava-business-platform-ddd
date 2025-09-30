package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.converter.SysAuditLogAppConverter;
import com.kava.kbpd.upms.application.model.command.SysAuditLogCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysAuditLogUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysAuditLogAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAuditLogAppListDTO;
import com.kava.kbpd.upms.application.service.ISysAuditLogAppService;
import com.kava.kbpd.upms.domain.model.entity.SysAuditLogEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogId;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogListQuery;
import com.kava.kbpd.upms.domain.repository.ISysAuditLogRepository;
import com.kava.kbpd.upms.domain.service.ISysAuditLogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: audit log application service
 */
@Slf4j
@Service
public class SysAuditLogAppService implements ISysAuditLogAppService {
    @Resource
    private ISysAuditLogRepository sysAuditLogRepository;

    @Resource
    private ISysAuditLogService sysAuditLogService;

    @Resource
    private SysAuditLogAppConverter sysAuditLogAppConverter;

    @Override
    public SysAuditLogId createAuditLog(SysAuditLogCreateCommand command) {
        SysAuditLogEntity sysAuditLogEntity = sysAuditLogAppConverter.convertCreateCommand2Entity(command);
        return sysAuditLogRepository.create(sysAuditLogEntity);
    }

    @Override
    public void updateAuditLog(SysAuditLogUpdateCommand command) {
        SysAuditLogEntity sysAuditLogEntity = sysAuditLogAppConverter.convertUpdateCommand2Entity(command);
        sysAuditLogRepository.update(sysAuditLogEntity);
    }

    @Override
    public void removeAuditLogBatchByIds(List<SysAuditLogId> ids) {
        sysAuditLogRepository.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysAuditLogAppListDTO> queryAuditLogPage(SysAuditLogListQuery query) {
        PagingInfo<SysAuditLogEntity> sysAuditLogEntityPagingInfo = sysAuditLogRepository.queryPage(query);
        List<SysAuditLogAppListDTO> collect = sysAuditLogEntityPagingInfo.getList().stream().map(sysAuditLogEntity -> sysAuditLogAppConverter.convertEntityToListQueryDTO(sysAuditLogEntity)).toList();
        return PagingInfo.toResponse(collect, sysAuditLogEntityPagingInfo);
    }

    @Override
    public SysAuditLogAppDetailDTO queryAuditLogById(SysAuditLogId id) {
        SysAuditLogEntity AuditLogEntity = sysAuditLogRepository.queryById(id);
        return sysAuditLogAppConverter.convertEntityToDetailDTO(AuditLogEntity);
    }

}
