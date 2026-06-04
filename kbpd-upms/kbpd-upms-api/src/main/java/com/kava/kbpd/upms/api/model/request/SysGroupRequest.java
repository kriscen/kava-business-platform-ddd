package com.kava.kbpd.upms.api.model.request;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 分组管理 request对象
 */
@Data
public class SysGroupRequest implements Serializable {

    /**
     * 分组ID
     */
    private Long id;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;

    /**
     * 父级分组id
     */
    private Long pid;

}
