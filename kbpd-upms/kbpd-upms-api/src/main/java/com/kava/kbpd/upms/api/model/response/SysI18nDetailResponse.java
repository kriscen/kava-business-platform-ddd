package com.kava.kbpd.upms.api.model.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 国际化消息 详情响应对象
 */
@Data
public class SysI18nDetailResponse implements Serializable {

    private Long id;

    private String code;

    private String language;

    private String content;

    private String creator;

    private LocalDateTime gmtCreate;

    private String modifier;

    private LocalDateTime gmtModified;
}
