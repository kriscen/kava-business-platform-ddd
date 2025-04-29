package com.kava.kbpd.common.core.base;

import com.kava.kbpd.common.core.label.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Kris
 * @date 2025/3/26
 * @description: 查询参数值对象
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class QueryParamValObj implements ValueObject {
    /**
     * 页码
     */
    int pageNo;
    /**
     * 每页条数
     */
    int pageSize;
}
