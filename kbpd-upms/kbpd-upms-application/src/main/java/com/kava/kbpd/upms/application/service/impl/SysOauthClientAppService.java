package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.converter.SysOauthClientAppConverter;
import com.kava.kbpd.upms.application.model.command.SysOauthClientCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysOauthClientUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysOauthClientAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysOauthClientAppListDTO;
import com.kava.kbpd.upms.application.service.ISysOauthClientAppService;
import com.kava.kbpd.upms.domain.model.entity.SysOauthClientEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientId;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientListQuery;
import com.kava.kbpd.upms.domain.repository.ISysOauthClientRepository;
import com.kava.kbpd.upms.domain.service.ISysOauthClientService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: OauthClient application service
 */
@Slf4j
@Service
public class SysOauthClientAppService implements ISysOauthClientAppService {
    @Resource
    private ISysOauthClientRepository sysOauthClientRepository;

    @Resource
    private ISysOauthClientService sysOauthClientService;

    @Resource
    private SysOauthClientAppConverter sysOauthClientAppConverter;

    @Override
    public SysOauthClientId createOauthClient(SysOauthClientCreateCommand command) {
        SysOauthClientEntity sysOauthClientEntity = sysOauthClientAppConverter.convertCreateCommand2Entity(command);
        return sysOauthClientRepository.create(sysOauthClientEntity);
    }

    @Override
    public void updateOauthClient(SysOauthClientUpdateCommand command) {
        SysOauthClientEntity sysOauthClientEntity = sysOauthClientAppConverter.convertUpdateCommand2Entity(command);
        sysOauthClientRepository.update(sysOauthClientEntity);
    }

    @Override
    public void removeOauthClientBatchByIds(List<SysOauthClientId> ids) {
        sysOauthClientRepository.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysOauthClientAppListDTO> queryOauthClientPage(SysOauthClientListQuery query) {
        PagingInfo<SysOauthClientEntity> sysOauthClientEntityPagingInfo = sysOauthClientRepository.queryPage(query);
        List<SysOauthClientAppListDTO> collect = sysOauthClientEntityPagingInfo.getList().stream().map(sysOauthClientEntity -> sysOauthClientAppConverter.convertEntityToListQueryDTO(sysOauthClientEntity)).toList();
        return PagingInfo.toResponse(collect, sysOauthClientEntityPagingInfo);
    }

    @Override
    public SysOauthClientAppDetailDTO queryOauthClientById(SysOauthClientId id) {
        SysOauthClientEntity OauthClientEntity = sysOauthClientRepository.queryById(id);
        return sysOauthClientAppConverter.convertEntityToDetailDTO(OauthClientEntity);
    }

}
