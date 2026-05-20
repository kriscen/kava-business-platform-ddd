package com.kava.kbpd.upms.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: I18n list query result
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysI18nAppListDTO {

    private Long id;

    private String code;

    private String language;

    private String content;
}
