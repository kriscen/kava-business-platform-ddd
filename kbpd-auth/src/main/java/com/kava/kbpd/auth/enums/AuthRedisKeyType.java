package com.kava.kbpd.auth.enums;

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
    TOKEN("token","token"),
    CONSENT("consent","consent"),
    ;
    private final String type;
    private final String desc;
}
