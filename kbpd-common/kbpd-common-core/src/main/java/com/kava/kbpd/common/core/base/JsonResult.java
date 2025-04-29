package com.kava.kbpd.common.core.base;

import com.kava.kbpd.common.core.exception.BaseErrorCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Kris
 * @date 2025/2/5
 * @description: 统一的Spring mvc响应结果封装对象
 */
@Data
public class JsonResult<T> implements Serializable {

    /**
     * 请求成功
     */
    private static final boolean REQUEST_SUCCESS = true;

    /**
     * 请求失败
     */
    private static final boolean REQUEST_FAIL = false;

    /**
     * 默认错误码
     */
    private static final String DEFAULT_ERROR_CODE = "-1";

    /**
     * 请求是否成功
     */
    private Boolean success;

    /**
     * 业务数据
     */
    private T data;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误提示语
     */
    private String errorMessage;


    public JsonResult() {
    }

    public JsonResult(Boolean success, T data, String errorCode, String errorMessage) {
        this.success = success;
        this.data = data;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * 成功，不用返回数据
     *
     * @return result
     */
    public static <T> JsonResult<T> buildSuccess() {
        return new JsonResult<>(REQUEST_SUCCESS, null, null, null);
    }

    /**
     * 成功，返回数据
     *
     * @param data 数据
     * @return result
     */
    public static <T> JsonResult<T> buildSuccess(T data) {
        return new JsonResult<>(REQUEST_SUCCESS, data, null, null);
    }

    /**
     * 失败，固定状态码
     *
     * @param errorMsg 错误提示
     * @return result
     */
    public static <T> JsonResult<T> buildError(String errorMsg) {
        return new JsonResult<>(REQUEST_FAIL, null, DEFAULT_ERROR_CODE, errorMsg);
    }

    /**
     * 失败，自定义错误码和信息
     *
     * @param errorCode 错误码
     * @param errorMsg  错误提示
     * @return result
     */
    public static <T> JsonResult<T> buildError(String errorCode, String errorMsg) {
        return new JsonResult<>(REQUEST_FAIL, null, errorCode, errorMsg);
    }

    /**
     * 失败，枚举类定义错误码和信息
     *
     * @param baseErrorCodeEnum 错误码
     * @return result
     */
    public static <T> JsonResult<T> buildError(BaseErrorCodeEnum baseErrorCodeEnum) {
        return new JsonResult<>(REQUEST_FAIL, null, baseErrorCodeEnum.getErrorCode(), baseErrorCodeEnum.getErrorMsg());
    }
}
