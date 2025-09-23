package com.kava.kbpd.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kris
 * @date 2025/9/23
 * @description: 通用状态枚举
 */
@Getter
@AllArgsConstructor
public enum Status {

    enable("1", "生效"),
    disable("0", "未生效");

    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String desc;
}
