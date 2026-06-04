package com.kava.kbpd.upms.domain.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 分组管理
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysGroupEntity implements Entity<SysGroupId> {

	private SysGroupId id;

	/**
	 * 分组名称
	 */
	private String name;

	/**
	 * 排序
	 */
	private Integer sortOrder;


	/**
	 * 父级分组id
	 */
	private SysGroupId pid;

	/**
	 * 租户ID
	 */
	private SysTenantId tenantId;

	@Override
	public SysGroupId identifier() {
		return id;
	}
}
