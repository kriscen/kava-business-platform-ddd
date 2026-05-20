package com.kava.kbpd.upms.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: I18n application detail
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysI18nAppDetailDTO {

    private Long id;

    private String code;

    private String language;

    private String content;

    private String creator;

    private LocalDateTime gmtCreate;

    private String modifier;

    private LocalDateTime gmtModified;
}
