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

    // ---- 10-01 角色 ----
    ROLE_NOT_FOUND("10010001", "角色不存在"),
    ROLE_CODE_DUPLICATE("10010002", "角色编码已存在"),
    ROLE_MENU_EMPTY("10010003", "角色必须关联至少一个菜单"),

    // ---- 10-02 用户 ----
    USER_NOT_FOUND("10020001", "用户不存在"),
    USER_USERNAME_DUPLICATE("10020002", "用户名已存在"),

    // ---- 10-03 菜单 ----
    MENU_NOT_FOUND("10030001", "菜单不存在"),
    MENU_SCOPE_INVALID("10030002", "菜单作用域无效"),
    MENU_PID_SELF_REFERENCE("10030003", "菜单父节点不能为自身"),
    MENU_PID_CIRCULAR("10030004", "菜单父节点不能形成循环引用"),
    MENU_HAS_CHILDREN("10030005", "菜单存在子菜单，无法删除"),
    MENU_REFERENCED_BY_ROLE("10030006", "菜单已被角色引用，无法删除"),

    // ---- 10-04 租户 ----
    TENANT_NOT_FOUND("10040001", "租户不存在"),
    TENANT_CODE_DUPLICATE("10040002", "租户编码已存在"),
    TENANT_STATUS_INVALID_TRANSITION("10040003", "租户状态流转不合法"),

    // ---- 10-05 部门 ----
    DEPT_PID_SELF_REFERENCE("10050001", "部门父节点不能为自身"),
    DEPT_PID_CIRCULAR("10050002", "部门父节点不能形成循环引用"),
    DEPT_HAS_CHILDREN("10050003", "部门存在子部门，无法删除"),
    DEPT_REFERENCED_BY_USER("10050004", "部门已被用户引用，无法删除"),

    // ---- 10-06 OAuth客户端 ----
    CLIENT_ID_DUPLICATE("10060001", "客户端ID已存在"),
    CLIENT_SECRET_REQUIRED("10060002", "客户端密钥不能为空"),
    CLIENT_TOKEN_VALIDITY_INVALID("10060003", "令牌有效期无效"),
    CLIENT_GRANT_TYPES_REQUIRED("10060004", "授权方式不能为空"),

    // ---- 10-08 国际化 ----
    I18N_CODE_DUPLICATE("10080001", "国际化编码已存在"),

    // ---- 10-09 应用 ----
    APP_NOT_FOUND("10090001", "应用不存在"),
    APP_CODE_DUPLICATE("10090002", "应用编码已存在"),
    APP_NOT_DELETABLE("10090003", "系统应用不可删除"),
    APP_NOT_DISABLEABLE("10090004", "系统应用不可停用"),
    APP_IN_USE("10090005", "应用仍有租户使用，无法删除"),
    APP_DISABLED("10090006", "应用已停用，无法订阅"),

    // ---- 10-10 租户应用 ----
    TENANT_APP_ALREADY_SUBSCRIBED("10100001", "租户已订阅该应用"),
    TENANT_APP_NOT_UNSUBSCRIBABLE("10100002", "kava-base 系统应用不可退订"),
    TENANT_APP_MENU_OUT_OF_SCOPE("10100003", "菜单超出可分配范围"),
    APP_MENU_NOT_FOUND("10100004", "关联的菜单不存在"),

    ;

    private final String errorCode;

    private final String errorMsg;

}