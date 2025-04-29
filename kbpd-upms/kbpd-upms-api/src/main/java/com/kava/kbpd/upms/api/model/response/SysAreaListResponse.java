package com.kava.kbpd.upms.api.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 行政区划 列表响应对象
 */
@Data
public class SysAreaListResponse implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 父ID
     */
    private Long pid;

    /**
     * 地区名称
     */
    private String name;

    /**
     * 地区字母
     */
    private String letter;

    /**
     * 高德地区code
     */
    private Long adcode;

    /**
     * 经纬度
     */
    private String location;

    /**
     * 排序值
     */
    private Long areaSort;

    /**
     * 0:未生效，1:生效
     */
    private String areaStatus;

    /**
     * 0:国家,1:省,2:城市,3:区县
     */
    private String areaType;

    /**
     * 0:非热门，1:热门
     */
    private String hot;

    /**
     * 城市编码
     */
    private String cityCode;

}
