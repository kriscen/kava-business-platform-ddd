package com.kava.kbpd.upms.domain.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nMessageId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 系统表-国际化消息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysI18nMessage implements Entity<SysI18nMessageId> {

    private SysI18nMessageId id;

    private String code;

    private String language;

    private String content;

    @Override
    public SysI18nMessageId identifier() {
        return id;
    }
}
