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
 * @description: 行政区划表
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class SysAreaListQuery implements ValueObject {

    /**
     * 父节点id
     */
    Long pid;
    /**
     * 地区名称
     */
    String name;

    /**
     * 地区字母
     */
    String letter;

    /**
     * 高德地区code
     */
    Long adcode;

    /**
     * 经纬度
     */
    String location;

    /**
     * 排序值
     */
    Long areaSort;

    /**
     * 0:未生效，1:生效
     */
    String areaStatus;

    /**
     * 0:国家,1:省,2:城市,3:区县
     */
    String areaType;

    /**
     * 0:非热门，1:热门
     */
    String hot;

    /**
     * 城市编码
     */
    String cityCode;

    /**
     * 分页参数
     */
    QueryParamValObj queryParam;

}
