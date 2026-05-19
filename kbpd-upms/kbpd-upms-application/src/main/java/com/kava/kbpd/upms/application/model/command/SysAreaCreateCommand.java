package com.kava.kbpd.upms.application.model.command;

import com.kava.kbpd.common.core.enums.Status;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaId;
import com.kava.kbpd.upms.types.enums.SysAreaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: area create command
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysAreaCreateCommand {

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
