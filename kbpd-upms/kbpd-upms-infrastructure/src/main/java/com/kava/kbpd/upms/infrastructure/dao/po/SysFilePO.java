package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kava.kbpd.common.database.po.TenantDeletablePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 文件管理
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_file")
public class SysFilePO extends TenantDeletablePO {

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
}
