package com.kava.kbpd.upms.domain.model.entity;

import com.kava.kbpd.common.core.label.AggregateRoot;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 角色 实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleEntity implements AggregateRoot<SysRoleId> {

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

	/**
	 * 所拥有的menuId集合
	 */
	private List<SysMenuId> menuIds;

	/**
	 * 租户ID
	 */
	private SysTenantId tenantId;

	@Override
	public SysRoleId identifier() {
		return id;
	}
}
