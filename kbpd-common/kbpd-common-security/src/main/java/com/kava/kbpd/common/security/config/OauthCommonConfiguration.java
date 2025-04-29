package com.kava.kbpd.common.security.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kris
 * @date 2025/4/9
 * @description: oauth 公共配置bean
 */
@Configuration
public class OauthCommonConfiguration {
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }
}
