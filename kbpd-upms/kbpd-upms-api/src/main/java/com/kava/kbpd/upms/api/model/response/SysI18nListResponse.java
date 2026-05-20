package com.kava.kbpd.upms.api.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 国际化消息 列表响应对象
 */
@Data
public class SysI18nListResponse implements Serializable {

    private Long id;

    private String code;

    private String language;

    private String content;
}
