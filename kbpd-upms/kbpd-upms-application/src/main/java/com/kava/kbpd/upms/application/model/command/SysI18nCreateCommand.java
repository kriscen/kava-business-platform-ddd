package com.kava.kbpd.upms.application.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: I18n create command
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysI18nCreateCommand {

    private String code;

    private String language;

    private String content;
}
