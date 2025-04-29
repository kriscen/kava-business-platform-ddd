package com.kava.kbpd.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kris
 * @date 2025/4/3
 * @description: 自定义的grantType
 */
@Getter
@AllArgsConstructor
public enum CustomerGrantType {
    SMS("sms", "短信登录"),
    ;
    private final String code;
    private final String desc;

}
