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
public enum SysRoleDataScope {

    ALL("0", "全部"),
    CUSTOM("1", "自定义"),
    GROUP_AND_CHILD("2", "本分组及以下"),
    GROUP_ONLY("3", "本分组"),
    SELF("4", "仅本人");

    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String desc;
}
