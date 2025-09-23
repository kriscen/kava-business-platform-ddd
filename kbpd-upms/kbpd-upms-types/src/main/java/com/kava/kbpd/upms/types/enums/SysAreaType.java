package com.kava.kbpd.upms.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kris
 * @date 2025/9/23
 * @description: 区域类型
 */
@Getter
@AllArgsConstructor
public enum SysAreaType {
    COUNTRY("0", "国家"),
    PROVINCE("1", "省"),
    CITY("2", "城市"),
    DISTRICT("3", "区县");

    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String desc;
}
