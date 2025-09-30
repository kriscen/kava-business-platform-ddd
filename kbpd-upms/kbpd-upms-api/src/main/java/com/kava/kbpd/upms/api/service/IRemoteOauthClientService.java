package com.kava.kbpd.upms.api.service;

import com.kava.kbpd.upms.api.model.dto.SysOauthClientDTO;

/**
 * @author Kris
 * @date 2025/3/31
 * @description: oauth dubbo service
 */
public interface IRemoteOauthClientService {
    SysOauthClientDTO queryByClientId(String clientId);
}
