package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kava.kbpd.common.database.po.TenantDeletablePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 审计记录表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_audit_log")
public class SysAuditLogPO extends TenantDeletablePO {

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

}
