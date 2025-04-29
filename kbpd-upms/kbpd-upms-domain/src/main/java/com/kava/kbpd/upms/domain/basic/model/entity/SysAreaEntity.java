package com.kava.kbpd.upms.domain.basic.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAreaId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 行政区划表
 */
@Data
@Builder
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
public class SysAreaEntity implements Entity<SysAreaId> {

    /**
     * 主键ID
     */
    private SysAreaId id;

    /**
     * 父ID
     */
    private SysAreaId pid;

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

    @Override
    public SysAreaId identifier() {
        return id;
    }
}
