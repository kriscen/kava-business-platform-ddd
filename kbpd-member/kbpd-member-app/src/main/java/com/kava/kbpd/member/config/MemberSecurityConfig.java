package com.kava.kbpd.member.config;

import com.kava.kbpd.upms.api.service.IRemoteUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kris
 * @date 2025/4/9
 * @description:
 */
@Configuration
public class MemberSecurityConfig {

    @DubboReference(version = "1.0")
    private IRemoteUserService remoteUserService;

}
