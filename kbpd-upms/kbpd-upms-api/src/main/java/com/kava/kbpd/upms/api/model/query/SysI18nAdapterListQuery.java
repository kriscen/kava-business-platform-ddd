package com.kava.kbpd.upms.api.model.query;

import com.kava.kbpd.common.core.base.AdapterBaseListQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 国际化消息 query对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysI18nAdapterListQuery extends AdapterBaseListQuery {

    private String code;

    private String language;
}
