package com.kava.kbpd.upms.api.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 国际化消息 request对象
 */
@Data
public class SysI18nRequest implements Serializable {

    private Long id;

    private String code;

    private String language;

    private String content;
}
