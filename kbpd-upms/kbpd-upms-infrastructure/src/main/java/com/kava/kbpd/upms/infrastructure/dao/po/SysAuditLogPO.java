package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 审计记录表
 */
@Data
@TableName("sys_audit_log")
public class SysAuditLogPO implements Serializable {

	/**
	 * 主键
	 */
	@TableId
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
	@TableField(fill = FieldFill.INSERT)
	private String creator;

	/**
	 * 操作时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime gmtCreate;

	/**
	 * 删除标记
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

	/**
	 * 租户ID
	 */
	private Long tenantId;

}
