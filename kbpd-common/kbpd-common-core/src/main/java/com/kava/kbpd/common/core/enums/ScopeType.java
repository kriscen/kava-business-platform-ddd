package com.kava.kbpd.common.core.enums;

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
    /**
     * 自有系统（内部App）
     */
    INTERNAL("internal", "自有系统"),

    /**
     * 第三方应用
     */
    THIRD_PARTY("third_party", "第三方应用"),

    /**
     * 开放 API
     */
    OPEN_API("open_api", "开放API"),
    ;
    private final String code;
    private final String desc;
}
