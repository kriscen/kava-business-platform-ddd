package com.kava.kbpd.upms.domain.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysFileId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 文件管理
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysFileEntity implements Entity<SysFileId> {

    /**
     * 编号
     */
    private SysFileId id;

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
     * 租户ID
     */
    private SysTenantId tenantId;

    @Override
    public SysFileId identifier() {
        return id;
    }
}
