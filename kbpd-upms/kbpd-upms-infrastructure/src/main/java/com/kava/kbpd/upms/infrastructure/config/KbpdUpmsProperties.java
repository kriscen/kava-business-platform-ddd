package com.kava.kbpd.upms.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kris
 * @date 2025/4/7
 * @description: 配置中心文件
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "kbpd.upms")
public class KbpdUpmsProperties {
    private Integer test;
}
