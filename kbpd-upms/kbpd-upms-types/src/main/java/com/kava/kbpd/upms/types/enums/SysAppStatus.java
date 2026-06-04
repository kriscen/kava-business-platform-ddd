package com.kava.kbpd.upms.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SysAppStatus {
    ACTIVE("ACTIVE", "可用"),
    DISABLED("DISABLED", "停用");

    private final String code;
    private final String desc;
}
