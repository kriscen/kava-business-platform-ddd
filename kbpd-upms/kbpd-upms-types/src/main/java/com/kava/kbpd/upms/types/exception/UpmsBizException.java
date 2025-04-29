package com.kava.kbpd.upms.types.exception;

import com.kava.kbpd.common.core.exception.BaseBizException;
import com.kava.kbpd.common.core.exception.BaseErrorCodeEnum;

/**
 * @author Kris
 * @date 2025/3/26
 * @description: upms业务异常
 */
public class UpmsBizException extends BaseBizException {
    public UpmsBizException(String errorMsg) {
        super(errorMsg);
    }

    public UpmsBizException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public UpmsBizException(BaseErrorCodeEnum baseErrorCodeEnum) {
        super(baseErrorCodeEnum);
    }

    public UpmsBizException(String errorCode, String errorMsg, Object... arguments) {
        super(errorCode, errorMsg, arguments);
    }

    public UpmsBizException(BaseErrorCodeEnum baseErrorCodeEnum, Object... arguments) {
        super(baseErrorCodeEnum, arguments);
    }
}
