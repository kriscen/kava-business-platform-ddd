package com.kava.kbpd.common.core.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kris
 * @date 2025/3/26
 * @description: query查询入参基类
 */
@Data
public class BaseQuery implements Serializable {
    /**
     * 页码
     */
    private int pageNo = 1;
    /**
     * 每页条数
     */
    private int pageSize = 10;
}
