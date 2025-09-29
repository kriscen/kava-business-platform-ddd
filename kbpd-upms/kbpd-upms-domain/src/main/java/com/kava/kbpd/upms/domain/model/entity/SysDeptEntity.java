package com.kava.kbpd.upms.domain.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 部门管理
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysDeptEntity implements Entity<SysDeptId> {

	private SysDeptId id;

	/**
	 * 部门名称
	 */
	private String name;

	/**
	 * 排序
	 */
	private Integer sortOrder;


	/**
	 * 父级部门id
	 */
	private SysDeptId pid;

	/**
	 * 租户ID
	 */
	private SysTenantId tenantId;

	@Override
	public SysDeptId identifier() {
		return id;
	}
}
