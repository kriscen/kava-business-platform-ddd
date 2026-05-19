package com.kava.kbpd.upms.application.model.dto;

import com.kava.kbpd.common.core.enums.Status;
import com.kava.kbpd.upms.types.enums.SysAreaType;
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
     * 状态
     */
    private Status areaStatus;

    /**
     * 区域类型
     */
    private SysAreaType areaType;

    /**
     * 城市编码
     */
    private String cityCode;
}
