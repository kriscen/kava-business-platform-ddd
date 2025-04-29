package com.kava.kbpd.upms.api.service;

import com.kava.kbpd.upms.api.model.dto.SysOauthClientDetailsDTO;

/**
 * @author Kris
 * @date 2025/3/31
 * @description: oauth dubbo service
 */
public interface IRemoteOauthClientDetailService {
    SysOauthClientDetailsDTO findByClientId(String clientId);
}
