package com.kava.kbpd.auth.enums;

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
    TO_B("1", "B端"),
    TO_C("2", "C端"),
    ERROR("9", "错误请求用户"),


    ;

    private final String code;
    private final String desc;
}
