package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kava.kbpd.common.database.po.TenantDeletablePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 角色表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_role")
public class SysRolePO extends TenantDeletablePO {

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
}
