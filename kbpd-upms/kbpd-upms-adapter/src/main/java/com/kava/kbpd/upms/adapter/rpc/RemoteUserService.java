package com.kava.kbpd.upms.adapter.rpc;

import com.kava.kbpd.upms.adapter.converter.SysUserAdapterConverter;
import com.kava.kbpd.upms.api.model.dto.SysUserDTO;
import com.kava.kbpd.upms.api.service.IRemoteUserService;
import com.kava.kbpd.upms.application.model.dto.SysUserAppDetailDTO;
import com.kava.kbpd.upms.application.service.ISysUserAppService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Collections;

/**
 * @author Kris
 * @date 2025/4/7
 * @description: user dubbo service impl
 */
@Slf4j
@DubboService(version = "1.0")
public class RemoteUserService implements IRemoteUserService {
    @Resource
    private ISysUserAppService sysUserAppService;

    @Resource
    private SysUserAdapterConverter sysUserAdapterConverter;

    @Override
    public SysUserDTO findByUsername(String tenantId, String username) {
        Long tenantIdLong = Long.valueOf(tenantId);
        SysUserAppDetailDTO appDetail = sysUserAppService.queryUserByUsername(tenantIdLong, username);
        if (appDetail == null) {
            return null;
        }

        SysUserDTO sysUserDTO = sysUserAdapterConverter.convertAppDetail2RemoteDTO(appDetail);

        // 查询用户的角色标识和权限标识
        sysUserDTO.setRoles(sysUserAppService.queryRoleCodesByUserId(appDetail.getId()));
        sysUserDTO.setPermissions(sysUserAppService.queryPermissionsByUserId(appDetail.getId()));

        if (sysUserDTO.getRoles() == null) {
            sysUserDTO.setRoles(Collections.emptyList());
        }
        if (sysUserDTO.getPermissions() == null) {
            sysUserDTO.setPermissions(Collections.emptyList());
        }

        return sysUserDTO;
    }

    @Override
    public SysUserDTO loginByPwd(String name, String pwd) {
        SysUserDTO sysUserDTO = new SysUserDTO();
        sysUserDTO.setId(1L);
        return sysUserDTO;
    }
}
