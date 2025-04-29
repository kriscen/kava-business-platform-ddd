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

    ;

    private final String errorCode;

    private final String errorMsg;

}