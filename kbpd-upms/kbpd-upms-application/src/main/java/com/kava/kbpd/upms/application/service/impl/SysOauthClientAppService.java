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
import com.kava.kbpd.upms.domain.service.ISysOauthClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysOauthClientAppService implements ISysOauthClientAppService {
    private final ISysOauthClientService sysOauthClientService;
    private final SysOauthClientAppConverter sysOauthClientAppConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysOauthClientId createOauthClient(SysOauthClientCreateCommand command) {
        SysOauthClientEntity sysOauthClientEntity = sysOauthClientAppConverter.convertCreateCommand2Entity(command);
        return sysOauthClientService.create(sysOauthClientEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOauthClient(SysOauthClientUpdateCommand command) {
        SysOauthClientEntity sysOauthClientEntity = sysOauthClientAppConverter.convertUpdateCommand2Entity(command);
        sysOauthClientService.update(sysOauthClientEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeOauthClientBatchByIds(List<SysOauthClientId> ids) {
        sysOauthClientService.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysOauthClientAppListDTO> queryOauthClientPage(SysOauthClientListQuery query) {
        PagingInfo<SysOauthClientEntity> sysOauthClientEntityPagingInfo = sysOauthClientService.queryPage(query);
        List<SysOauthClientAppListDTO> collect = sysOauthClientEntityPagingInfo.getList().stream().map(sysOauthClientAppConverter::convertEntityToListQueryDTO).toList();
        return PagingInfo.toResponse(collect, sysOauthClientEntityPagingInfo);
    }

    @Override
    public SysOauthClientAppDetailDTO queryOauthClientById(SysOauthClientId id) {
        SysOauthClientEntity OauthClientEntity = sysOauthClientService.queryById(id);
        return sysOauthClientAppConverter.convertEntityToDetailDTO(OauthClientEntity);
    }

    @Override
    public SysOauthClientAppDetailDTO queryByClientId(String clientId) {
        SysOauthClientEntity OauthClientEntity = sysOauthClientService.queryByClientId(clientId);
        return sysOauthClientAppConverter.convertEntityToDetailDTO(OauthClientEntity);
    }

}
