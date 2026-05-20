package com.kava.kbpd.upms.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kris
 * @description: 系统支持的语言标识
 */
@Getter
@AllArgsConstructor
public enum SysLanguageEnum {
    ZH_CN("zh_CN", "中文"),
    EN("en", "英文");

    private final String code;

    private final String desc;
}
