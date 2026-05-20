package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kava.kbpd.common.database.po.SysDeletablePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 系统表-国际化消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_i18n_message")
public class SysI18nMessagePO extends SysDeletablePO {

    private String code;

    private String language;

    private String content;
}
