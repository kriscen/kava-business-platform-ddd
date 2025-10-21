package com.kava.kbpd.upms.api.service;

import com.kava.kbpd.upms.api.model.dto.SysUserDTO;

/**
 * @author Kris
 * @date 2025/3/31
 * @description: user dubbo service
 */
public interface IRemoteUserService {
    SysUserDTO findByUsername(String tenantId,String username);

    SysUserDTO loginByPwd(String name, String pwd);
}
