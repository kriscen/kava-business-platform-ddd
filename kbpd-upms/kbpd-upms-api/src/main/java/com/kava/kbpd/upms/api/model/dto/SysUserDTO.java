package com.kava.kbpd.upms.api.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Kris
 * @date 2025/4/1
 * @description: userInfo
 */
@Data
public class SysUserDTO implements Serializable {

	private Long id;

	private String username;

	/**
	 * 密码哈希值（带前缀如 {bcrypt}）
	 */
	private String password;

	private Long deptId;

	private Long tenantId;

	/**
	 * 用户锁定标记："0"正常，"1"锁定
	 */
	private String lockFlag;

	/**
	 * 权限标识集合
	 */
	private List<String> permissions;

	/**
	 * 角色标识集合
	 */
	private List<String> roles;

}
