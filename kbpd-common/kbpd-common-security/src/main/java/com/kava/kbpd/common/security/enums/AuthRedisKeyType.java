package com.kava.kbpd.common.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kris
 * @date 2025/4/1
 * @description: redis key type
 */
@Getter
@AllArgsConstructor
public enum AuthRedisKeyType {
    SA_TOKEN("saToken", "token"),
    SMS_CODE("smsCode", "短信验证码"),
    ;
    private final String type;
    private final String desc;
}
