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

	/**
	 * 权限标识集合
	 */
	private List<String> permissions;

	/**
	 * 角色集合
	 */
	private List<String> roles;

}
