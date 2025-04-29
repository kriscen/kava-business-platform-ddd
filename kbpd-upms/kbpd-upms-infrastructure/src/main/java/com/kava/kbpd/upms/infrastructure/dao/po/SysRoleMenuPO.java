package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 角色菜单表
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenuPO implements Serializable {

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 菜单ID
	 */
	private Long menuId;

}
