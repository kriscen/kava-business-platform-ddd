package com.kava.kbpd.common.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kris
 * @date 2025/4/3
 * @description: 用户类型区分
 */
@Getter
@AllArgsConstructor
public enum UserType {
    TO_C("C", "C端"),
    TO_B("B", "B端");

    private final String code;
    private final String desc;
}
