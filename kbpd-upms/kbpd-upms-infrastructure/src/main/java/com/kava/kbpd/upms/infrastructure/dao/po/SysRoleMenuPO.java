package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kava.kbpd.common.database.po.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 角色菜单表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_role_menu")
public class SysRoleMenuPO extends BasePO {

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 菜单ID
	 */
	private Long menuId;

}
