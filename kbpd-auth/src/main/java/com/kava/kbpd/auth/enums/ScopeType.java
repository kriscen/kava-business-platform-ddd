package com.kava.kbpd.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kris
 * @date 2025/4/3
 * @description: scope枚举
 */
@Getter
@AllArgsConstructor
public enum ScopeType {
    OPENAPI("openapi","第三方接入使用openapi"),
    SERVER("server","自己的系统使用server"),
    ;
    private final String code;
    private final String desc;
}
