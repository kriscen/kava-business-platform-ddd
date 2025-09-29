package com.kava.kbpd.upms.application.model.dto;

import lombok.Data;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: area application detail
 */
@Data
public class SysAreaAppDetailDTO {
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
     * 城市编码
     */
    private String cityCode;
}
