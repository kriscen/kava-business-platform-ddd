package com.kava.kbpd.common.cache.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kris
 * @date 2025/4/1
 * @description: 模块枚举
 */
@Getter
@AllArgsConstructor
public enum RedisKeyModule {

    AUTH("auth", "认证模块"),
    UPMS("upms", "权限管理模块"),

    ;
    private final String module;
    private final String desc;

}
