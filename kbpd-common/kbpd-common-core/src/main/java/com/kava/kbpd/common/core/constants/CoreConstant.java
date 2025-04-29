package com.kava.kbpd.common.core.constants;

/**
 * @author Kris
 * @date 2025/2/5
 * @description: 框架核心的一些常量定义
 */
public interface CoreConstant {

    /**
     * 默认时区
     */
    String DEFAULT_TIME_ZONE = "Asia/Shanghai";

    /**
     * 统一的日期格式
     */
    String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    /**
     * 统一的日期时间格式
     */
    String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 统一的时间格式
     */
    String TIME_FORMAT_PATTERN = "HH:mm:ss";

    /**
     * 格林威治时间格式
     */
    String GMT_FORMAT_PATTERN = "EEE MMM dd HH:mm:ss z yyyy";

    /**
     * 统一分隔符
     */
    String SEPARATOR = ":";
}