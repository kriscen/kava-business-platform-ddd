package com.kava.kbpd.upms.adapter.rpc;

import com.kava.kbpd.upms.adapter.converter.SysOauthClientAdapterConverter;
import com.kava.kbpd.upms.api.model.dto.SysOauthClientDTO;
import com.kava.kbpd.upms.api.service.IRemoteOauthClientService;
import com.kava.kbpd.upms.application.model.dto.SysOauthClientAppDetailDTO;
import com.kava.kbpd.upms.application.service.ISysOauthClientAppService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Kris
 * @date 2025/3/31
 * @description: oauth dubbo service impl
 */
@Slf4j
@DubboService(version = "1.0")
public class RemoteOauthClientService implements IRemoteOauthClientService {

    @Resource
    private ISysOauthClientAppService appService;

    @Resource
    private SysOauthClientAdapterConverter adapterConverter;

    @Override
    public SysOauthClientDTO queryByClientId(String clientId) {
        SysOauthClientAppDetailDTO appDetailDTO = appService.queryByClientId(clientId);
        return adapterConverter.convertAppDetail2RemoteDTO(appDetailDTO);
    }
}
