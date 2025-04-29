package com.kava.kbpd.upms.trigger.rpc;

import com.kava.kbpd.upms.api.model.dto.SysOauthClientDetailsDTO;
import com.kava.kbpd.upms.api.service.IRemoteOauthClientDetailService;
import com.kava.kbpd.upms.domain.permission.service.ISysOauthClientDetailsService;
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
public class RemoteOauthClientDetailsService implements IRemoteOauthClientDetailService {

    @Resource
    private ISysOauthClientDetailsService sysOauthClientDetailsService;

    @Override
    public SysOauthClientDetailsDTO findByClientId(String clientId) {
        SysOauthClientDetailsDTO dto = new SysOauthClientDetailsDTO();
        dto.setClientId(clientId);
        dto.setClientSecret("aaaa-bbbb-cccc-dddd-eeee");
        dto.setWebServerRedirectUri("https://www.baidu.com");
        dto.setScope("user");
        dto.setAuthorizedGrantTypes(new String[]{"authorization_code","password","refresh_token"});
        return dto;
    }
}
