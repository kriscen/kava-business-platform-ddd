package com.kava.kbpd.upms.domain.model.valobj;

import com.kava.kbpd.common.core.base.QueryParamValObj;
import com.kava.kbpd.common.core.label.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 国际化消息列表查询
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class SysI18nListQuery implements ValueObject {

    QueryParamValObj queryParam;

    String code;

    String language;
}
