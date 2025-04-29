package com.kava.kbpd.upms.api.model.request;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 文件类型 request对象
 */
@Data
public class SysFileGroupRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 父ID
     */
    private Long pid;

    /**
     * 类型
     */
    private Long type;

    /**
     * 名称
     */
    private String name;

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

}