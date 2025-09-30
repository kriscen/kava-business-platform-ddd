package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.converter.SysLogAppConverter;
import com.kava.kbpd.upms.application.model.command.SysLogCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysLogUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysLogAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysLogAppListDTO;
import com.kava.kbpd.upms.application.service.ISysLogAppService;
import com.kava.kbpd.upms.domain.model.entity.SysLogEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysLogId;
import com.kava.kbpd.upms.domain.model.valobj.SysLogListQuery;
import com.kava.kbpd.upms.domain.repository.ISysLogRepository;
import com.kava.kbpd.upms.domain.service.ISysLogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: Log application service
 */
@Slf4j
@Service
public class SysLogAppService implements ISysLogAppService {
    @Resource
    private ISysLogRepository sysLogRepository;

    @Resource
    private ISysLogService sysLogService;

    @Resource
    private SysLogAppConverter sysLogAppConverter;

    @Override
    public SysLogId createLog(SysLogCreateCommand command) {
        SysLogEntity sysLogEntity = sysLogAppConverter.convertCreateCommand2Entity(command);
        return sysLogRepository.create(sysLogEntity);
    }

    @Override
    public void updateLog(SysLogUpdateCommand command) {
        SysLogEntity sysLogEntity = sysLogAppConverter.convertUpdateCommand2Entity(command);
        sysLogRepository.update(sysLogEntity);
    }

    @Override
    public void removeLogBatchByIds(List<SysLogId> ids) {
        sysLogRepository.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysLogAppListDTO> queryLogPage(SysLogListQuery query) {
        PagingInfo<SysLogEntity> sysLogEntityPagingInfo = sysLogRepository.queryPage(query);
        List<SysLogAppListDTO> collect = sysLogEntityPagingInfo.getList().stream().map(sysLogEntity -> sysLogAppConverter.convertEntityToListQueryDTO(sysLogEntity)).toList();
        return PagingInfo.toResponse(collect, sysLogEntityPagingInfo);
    }

    @Override
    public SysLogAppDetailDTO queryLogById(SysLogId id) {
        SysLogEntity LogEntity = sysLogRepository.queryById(id);
        return sysLogAppConverter.convertEntityToDetailDTO(LogEntity);
    }

}
