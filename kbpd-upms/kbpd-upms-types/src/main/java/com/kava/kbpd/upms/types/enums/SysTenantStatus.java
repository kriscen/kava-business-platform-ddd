package com.kava.kbpd.upms.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SysTenantStatus {
    NORMAL("0", "正常"),
    DISABLED("9", "停用");

    private final String code;
    private final String desc;
}
