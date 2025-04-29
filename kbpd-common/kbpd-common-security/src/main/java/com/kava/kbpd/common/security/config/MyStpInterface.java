package com.kava.kbpd.common.security.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.kava.kbpd.common.security.constants.SecurityConstants;
import com.kava.kbpd.common.security.enums.UserType;
import com.kava.kbpd.upms.api.model.dto.SysUserDTO;
import com.kava.kbpd.upms.api.service.IRemoteUserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Kris
 * @date 2025/4/8
 * @description: StpInterface 实现
 */
@RequiredArgsConstructor
public class MyStpInterface implements StpInterface {

    private final IRemoteUserService remoteUserService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Object userType = StpUtil.getExtra(SecurityConstants.JWT_USER_TYPE);
        if (UserType.TO_B.getCode().equals(userType)) {
            SysUserDTO user = remoteUserService.findByUsername(String.valueOf(loginId));
            return user.getPermissions();
        }else {
            //c端不做权限校验
            return List.of();
        }
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Object userType = StpUtil.getExtra(SecurityConstants.JWT_USER_TYPE);
        if (UserType.TO_B.getCode().equals(userType)) {
            SysUserDTO user = remoteUserService.findByUsername(String.valueOf(loginId));
            return user.getRoles();
        }else {
            //c端不做权限校验
            return List.of();
        }
    }
}
