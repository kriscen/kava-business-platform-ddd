package com.kava.kbpd.common.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kris
 * @date 2025/2/5
 * @description: 系统通用的业务异常错误码枚举
 */
@Getter
@AllArgsConstructor
public enum CommonErrorCodeEnum implements BaseErrorCodeEnum {

    // =========== 00-00 系统级错误 =========

    SYSTEM_UNKNOWN_ERROR("00000001", "系统未知错误"),

    // =========== 00-01 客户端请求错误 =========

    CLIENT_HTTP_METHOD_ERROR("00010001", "客户端HTTP请求方法错误"),
    CLIENT_REQUEST_BODY_CHECK_ERROR("00010002", "客户端请求体参数校验不通过"),
    CLIENT_REQUEST_BODY_FORMAT_ERROR("00010003", "客户端请求体JSON格式错误或字段类型不匹配"),
    CLIENT_PATH_VARIABLE_ERROR("00010004", "客户端URL中的参数类型错误"),
    CLIENT_REQUEST_PARAM_CHECK_ERROR("00010005", "客户端请求参数校验不通过"),
    CLIENT_REQUEST_PARAM_REQUIRED_ERROR("00010006", "客户端请求缺少必填的参数"),

    // =========== 00-02 服务端错误 =========

    SERVER_ILLEGAL_ARGUMENT_ERROR("00020001", "业务方法参数检查不通过"),
    SERVER_DATABASE_ERROR("00020002", "数据库操作异常"),
    SERVER_RPC_ERROR("00020003", "RPC调用异常"),

    // =========== 00-03 认证/授权错误 =========

    AUTH_UNAUTHORIZED("00030001", "未认证"),
    AUTH_TOKEN_EXPIRED("00030002", "token已过期"),
    AUTH_TOKEN_INVALID("00030003", "token无效"),
    AUTH_FORBIDDEN("00030004", "权限不足"),
    AUTH_TENANT_INVALID("00030005", "租户不存在或已禁用"),
    ;

    private final String errorCode;

    private final String errorMsg;

}