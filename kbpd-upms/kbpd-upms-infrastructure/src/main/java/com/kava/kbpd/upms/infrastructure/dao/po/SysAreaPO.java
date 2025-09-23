package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kava.kbpd.common.database.po.SysDeletablePO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 行政区划表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@TableName("sys_area")
public class SysAreaPO extends SysDeletablePO {

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
