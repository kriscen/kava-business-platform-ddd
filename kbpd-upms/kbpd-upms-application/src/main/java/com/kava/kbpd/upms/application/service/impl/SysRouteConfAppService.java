package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.converter.SysRouteConfAppConverter;
import com.kava.kbpd.upms.application.model.command.SysRouteConfCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysRouteConfUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysRouteConfAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysRouteConfAppListDTO;
import com.kava.kbpd.upms.application.service.ISysRouteConfAppService;
import com.kava.kbpd.upms.domain.model.entity.SysRouteConfEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfId;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfListQuery;
import com.kava.kbpd.upms.domain.repository.ISysRouteConfRepository;
import com.kava.kbpd.upms.domain.service.ISysRouteConfService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: RouteConf application service
 */
@Slf4j
@Service
public class SysRouteConfAppService implements ISysRouteConfAppService {
    @Resource
    private ISysRouteConfRepository sysRouteConfRepository;

    @Resource
    private ISysRouteConfService sysRouteConfService;

    @Resource
    private SysRouteConfAppConverter sysRouteConfAppConverter;

    @Override
    public SysRouteConfId createRouteConf(SysRouteConfCreateCommand command) {
        SysRouteConfEntity sysRouteConfEntity = sysRouteConfAppConverter.convertCreateCommand2Entity(command);
        return sysRouteConfRepository.create(sysRouteConfEntity);
    }

    @Override
    public void updateRouteConf(SysRouteConfUpdateCommand command) {
        SysRouteConfEntity sysRouteConfEntity = sysRouteConfAppConverter.convertUpdateCommand2Entity(command);
        sysRouteConfRepository.update(sysRouteConfEntity);
    }

    @Override
    public void removeRouteConfBatchByIds(List<SysRouteConfId> ids) {
        sysRouteConfRepository.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysRouteConfAppListDTO> queryRouteConfPage(SysRouteConfListQuery query) {
        PagingInfo<SysRouteConfEntity> sysRouteConfEntityPagingInfo = sysRouteConfRepository.queryPage(query);
        List<SysRouteConfAppListDTO> collect = sysRouteConfEntityPagingInfo.getList().stream().map(sysRouteConfEntity -> sysRouteConfAppConverter.convertEntityToListQueryDTO(sysRouteConfEntity)).toList();
        return PagingInfo.toResponse(collect, sysRouteConfEntityPagingInfo);
    }

    @Override
    public SysRouteConfAppDetailDTO queryRouteConfById(SysRouteConfId id) {
        SysRouteConfEntity RouteConfEntity = sysRouteConfRepository.queryById(id);
        return sysRouteConfAppConverter.convertEntityToDetailDTO(RouteConfEntity);
    }

}
