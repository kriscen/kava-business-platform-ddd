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
public enum SysMenuType {
    CATALOG("0", "目录"),
    MENU("1", "菜单"),
    BUTTON("2", "按钮");

    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String desc;
}
