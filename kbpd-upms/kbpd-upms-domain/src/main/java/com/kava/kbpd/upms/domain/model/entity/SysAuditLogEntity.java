package com.kava.kbpd.upms.domain.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 审计记录表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysAuditLogEntity implements Entity<SysAuditLogId> {

	/**
	 * 主键
	 */
	private SysAuditLogId id;

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
	 * 租户ID
	 */
	private SysTenantId tenantId;

	@Override
	public SysAuditLogId identifier() {
		return id;
	}
}
