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
public enum SysFileGroupType {
    FILE(10, "文件"),
    VIDEO(20, "视频");

    /**
     * 编码
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String desc;
}
