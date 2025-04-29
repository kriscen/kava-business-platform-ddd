package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 文件管理
 */
@Data
@TableName("sys_file")
public class SysFilePO implements Serializable {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 原文件名
     */
    private String original;

    /**
     * 容器名称
     */
    private String bucketName;

    /***
     * 文件夹
     */
    private String dir;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件组
     */
    private Long groupId;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件hash
     */
    private String hash;

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
     * 删除标识：1-删除，0-正常
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private String delFlag;

}
