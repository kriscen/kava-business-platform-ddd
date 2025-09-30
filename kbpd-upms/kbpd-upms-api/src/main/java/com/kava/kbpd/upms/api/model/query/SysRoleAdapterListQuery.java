package com.kava.kbpd.upms.api.model.query;

import com.kava.kbpd.common.core.base.AdapterBaseListQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 角色 query对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleAdapterListQuery extends AdapterBaseListQuery {

	private Long roleId;

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
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreate;

	/**
	 * 修改人
	 */
	private String modifier;

	/**
	 * 更新时间
	 */
	private LocalDateTime gmtModified;

	/**
	 * 删除标识（0-正常,1-删除）
	 */
	private String delFlag;

}
