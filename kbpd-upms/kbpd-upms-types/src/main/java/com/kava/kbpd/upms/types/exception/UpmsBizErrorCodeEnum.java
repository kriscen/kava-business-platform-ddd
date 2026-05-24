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
    MENU_PID_SELF_REFERENCE("A00303", "菜单父节点不能为自身"),
    MENU_PID_CIRCULAR("A00304", "菜单父节点不能形成循环引用"),
    MENU_HAS_CHILDREN("A00305", "菜单存在子菜单，无法删除"),
    MENU_REFERENCED_BY_ROLE("A00306", "菜单已被角色引用，无法删除"),

    // ---- 部门 ----
    DEPT_PID_SELF_REFERENCE("A00501", "部门父节点不能为自身"),
    DEPT_PID_CIRCULAR("A00502", "部门父节点不能形成循环引用"),
    DEPT_HAS_CHILDREN("A00503", "部门存在子部门，无法删除"),
    DEPT_REFERENCED_BY_USER("A00504", "部门已被用户引用，无法删除"),

    // ---- 租户 ----
    TENANT_NOT_FOUND("A00401", "租户不存在"),
    TENANT_CODE_DUPLICATE("A00402", "租户编码已存在"),
    TENANT_STATUS_INVALID_TRANSITION("A00403", "租户状态流转不合法"),

    ;

    private final String errorCode;

    private final String errorMsg;

}