package com.kava.kbpd.upms.adapter.rpc;

import com.kava.kbpd.upms.api.model.dto.SysOauthClientDTO;
import com.kava.kbpd.upms.api.service.IRemoteOauthClientService;
import com.kava.kbpd.upms.domain.service.ISysOauthClientService;
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
    private ISysOauthClientService sysOauthClientDetailsService;

    @Override
    public SysOauthClientDTO findByClientId(String clientId) {
        SysOauthClientDTO dto = new SysOauthClientDTO();
        dto.setClientId(clientId);
        dto.setClientSecret("aaaa-bbbb-cccc-dddd-eeee");
        dto.setWebServerRedirectUri("https://www.baidu.com");
        dto.setScope("user");
        dto.setAuthorizedGrantTypes(new String[]{"authorization_code","password","refresh_token"});
        return dto;
    }
}
