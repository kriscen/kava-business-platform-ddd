package com.kava.kbpd.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Kris
 * @date 2025/10/22
 * @description: properties
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "kbpd.auth")
public class KbpdAuthProperties {

    /**
     * 白名单路径列表
     */
    private List<String> whitelistPaths;

    /**
     * 认证服务超时时间 10min
     */
    private Long authorizationTimeout = 10 * 60 * 1000L;

    /**
     * 授权服务超时 10min
     */
    private Long authorizationConsentTimeout = 10 * 60 * 1000L;

    /**
     * 刷新令牌有效期默认 30 天
     */
    private Integer refreshTokenValiditySeconds = 60 * 60 * 24 * 30;

    /**
     * 请求令牌有效期默认 12 小时
     */
    private Integer accessTokenValiditySeconds = 60 * 60 * 12;
}
