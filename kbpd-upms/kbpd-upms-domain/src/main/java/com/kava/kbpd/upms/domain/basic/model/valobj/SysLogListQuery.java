package com.kava.kbpd.upms.domain.basic.model.valobj;

import com.kava.kbpd.common.core.base.QueryParamValObj;
import com.kava.kbpd.common.core.label.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 日志
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class SysLogListQuery implements ValueObject {

   
    /**
     * 分页参数
     */
    QueryParamValObj queryParam;

}
