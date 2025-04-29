package com.kava.kbpd.upms.domain.permission.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysRoleId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 角色 实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleEntity implements Entity<SysRoleId> {

	private SysRoleId id;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 角色标识
	 */
	private String roleCode;

	/**
	 * 角色描述
	 */
	private String roleDesc;

	/**
	 * 数据权限类型
	 */
	private Integer dsType;

	/**
	 * 数据权限作用范围
	 */
	private String dsScope;

	@Override
	public SysRoleId identifier() {
		return id;
	}
}
