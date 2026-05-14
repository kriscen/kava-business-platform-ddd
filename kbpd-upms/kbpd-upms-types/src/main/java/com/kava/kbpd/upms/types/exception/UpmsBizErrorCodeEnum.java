package com.kava.kbpd.upms.types.exception;

import com.kava.kbpd.common.core.exception.BaseErrorCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kris
 * @date 2025/2/5
 * @description: upms的业务异常错误码枚举
 */
@Getter
@AllArgsConstructor
public enum UpmsBizErrorCodeEnum implements BaseErrorCodeEnum {

    // ---- 角色 ----
    ROLE_NOT_FOUND("A00101", "角色不存在"),
    ROLE_CODE_DUPLICATE("A00102", "角色编码已存在"),
    ROLE_MENU_EMPTY("A00103", "角色必须关联至少一个菜单"),

    // ---- 用户 ----
    USER_NOT_FOUND("A00201", "用户不存在"),
    USER_USERNAME_DUPLICATE("A00202", "用户名已存在"),

    // ---- 菜单 ----
    MENU_NOT_FOUND("A00301", "菜单不存在"),
    MENU_SCOPE_INVALID("A00302", "菜单作用域无效"),

    // ---- 租户 ----
    TENANT_NOT_FOUND("A00401", "租户不存在"),
    TENANT_CODE_DUPLICATE("A00402", "租户编码已存在"),

    ;

    private final String errorCode;

    private final String errorMsg;

}