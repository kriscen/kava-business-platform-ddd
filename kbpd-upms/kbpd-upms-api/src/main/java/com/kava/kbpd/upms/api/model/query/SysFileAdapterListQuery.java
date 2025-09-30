package com.kava.kbpd.upms.api.model.query;

import com.kava.kbpd.common.core.base.AdapterBaseListQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 文件管理 query对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysFileAdapterListQuery extends AdapterBaseListQuery {

    /**
     * 编号
     */
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
     * 更新时间
     */
    private LocalDateTime gmtModified;

    /**
     * 删除标识：1-删除，0-正常
     */
    private String delFlag;

}
