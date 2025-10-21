package com.kava.kbpd.upms.adapter.rpc;

import com.kava.kbpd.upms.api.model.dto.SysUserDTO;
import com.kava.kbpd.upms.api.service.IRemoteUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Kris
 * @date 2025/4/7
 * @description: user dubbo service impl
 */
@Slf4j
@DubboService(version = "1.0")
public class RemoteUserService implements IRemoteUserService {
    @Override
    public SysUserDTO findByUsername(String tenantId,String username) {
        return null;
    }

    @Override
    public SysUserDTO loginByPwd(String name, String pwd) {
        SysUserDTO sysUserDTO = new SysUserDTO();
        sysUserDTO.setId(1L);
        return sysUserDTO;
    }
}
