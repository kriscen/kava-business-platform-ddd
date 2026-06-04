package com.kava.kbpd.upms.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SysTenantAppStatus {
    ACTIVE("ACTIVE", "有效"),
    EXPIRED("EXPIRED", "过期");

    private final String code;
    private final String desc;
}
