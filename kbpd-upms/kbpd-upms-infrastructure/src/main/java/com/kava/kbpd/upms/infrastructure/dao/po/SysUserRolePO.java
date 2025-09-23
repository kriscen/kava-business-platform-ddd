package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kava.kbpd.common.database.po.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 用户角色表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user_role")
public class SysUserRolePO extends BasePO {

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 角色ID
	 */
	private Long roleId;

}
