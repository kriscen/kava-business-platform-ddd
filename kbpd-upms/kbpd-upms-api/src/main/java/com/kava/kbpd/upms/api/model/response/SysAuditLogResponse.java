package com.kava.kbpd.upms.api.model.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 审计记录 响应对象
 */
@Data
public class SysAuditLogResponse implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 审计名称
     */
    private String auditName;

    /**
     * 字段名称
     */
    private String auditField;

    /**
     * 变更前值
     */
    private String beforeVal;

    /**
     * 变更后值
     */
    private String afterVal;

    /**
     * 操作人
     */
    private String creator;

    /**
     * 操作时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 租户ID
     */
    private Long tenantId;

}