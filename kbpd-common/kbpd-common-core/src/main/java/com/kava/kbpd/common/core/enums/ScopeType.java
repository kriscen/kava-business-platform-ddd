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
     * 自有 B端 系统
     */
    INTERNAL_B("internal:b", "自有B端系统"),

    /**
     * 自有 C端 系统
     */
    INTERNAL_C("internal:c", "自有C端系统"),

    /**
     * 第三方 C端 应用
     */
    THIRD_PARTY_C("third_party:c", "第三方C端应用"),

    /**
     * 第三方 OpenAPI / B端 合作方
     */
    THIRD_PARTY_B("third_party:b", "第三方B端系统"),

    /**
     * 开放 API
     */
    OPEN_API("open_api", "开放API"),
    ;
    private final String code;
    private final String desc;
}
