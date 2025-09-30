package com.kava.kbpd.upms.application.model.command;

import com.kava.kbpd.upms.domain.model.valobj.SysAreaId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: area update command
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysAreaUpdateCommand {
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
     * 城市编码
     */
    private String cityCode;
}
