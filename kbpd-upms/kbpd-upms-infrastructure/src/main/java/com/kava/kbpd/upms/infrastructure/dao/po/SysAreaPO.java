package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 行政区划表
 */
@Data
@FieldNameConstants
@TableName("sys_area")
public class SysAreaPO implements Serializable {

    /**
     * 主键ID
     */
    @TableId
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

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifier;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    /**
     * 删除标记
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private String delFlag;
}
