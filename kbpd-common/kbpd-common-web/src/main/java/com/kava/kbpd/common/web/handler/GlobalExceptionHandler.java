package com.kava.kbpd.common.web.handler;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.exception.BaseBizException;
import com.kava.kbpd.common.core.exception.CommonErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseBizException.class)
    public JsonResult<Void> handleBizException(BaseBizException e) {
        return JsonResult.buildError(e.getErrorCode(), e.getErrorMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return JsonResult.buildError(CommonErrorCodeEnum.CLIENT_REQUEST_BODY_CHECK_ERROR.getErrorCode(), message);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public JsonResult<Void> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return JsonResult.buildError(CommonErrorCodeEnum.CLIENT_HTTP_METHOD_ERROR);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public JsonResult<Void> handleMissingParamException(MissingServletRequestParameterException e) {
        return JsonResult.buildError(CommonErrorCodeEnum.CLIENT_REQUEST_PARAM_REQUIRED_ERROR.getErrorCode(),
                e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public JsonResult<Void> handleNotReadableException(HttpMessageNotReadableException e) {
        return JsonResult.buildError(CommonErrorCodeEnum.CLIENT_REQUEST_BODY_FORMAT_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public JsonResult<Void> handleException(Exception e) {
        log.error("系统未知异常", e);
        return JsonResult.buildError(CommonErrorCodeEnum.SYSTEM_UNKNOWN_ERROR);
    }
}
