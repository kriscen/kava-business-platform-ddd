package com.kava.kbpd.upms.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SysMenuLevel {
    PLATFORM("PLATFORM", "平台级"),
    TENANT("TENANT", "租户级");

    private final String code;
    private final String desc;
}
