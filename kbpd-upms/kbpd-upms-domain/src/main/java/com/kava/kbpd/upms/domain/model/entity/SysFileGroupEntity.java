package com.kava.kbpd.upms.domain.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysFileGroupId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 文件类型
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysFileGroupEntity implements Entity<SysFileGroupId> {

	private SysFileGroupId id;

	private SysFileGroupId pid;

	private Long type;

	private String name;

	/**
	 * 租户ID
	 */
	private SysTenantId tenantId;

	@Override
	public SysFileGroupId identifier() {
		return id;
	}
}
