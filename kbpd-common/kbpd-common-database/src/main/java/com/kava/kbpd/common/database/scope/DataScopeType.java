package com.kava.kbpd.common.database.scope;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: 数据权限类型
 */
@Getter
@AllArgsConstructor
public enum DataScopeType {

	/**
	 * 查询全部数据
	 */
	ALL(0, "全部"),

	/**
	 * 自定义
	 */
	CUSTOM(1, "自定义"),

	/**
	 * 本级及子级
	 */
	OWN_CHILD_LEVEL(2, "本级及子级"),

	/**
	 * 本级
	 */
	OWN_LEVEL(3, "本级"),

	/**
	 * 本人
	 */
	SELF_LEVEL(4, "本人");

	/**
	 * 类型
	 */
	private final Integer type;

	/**
	 * 描述
	 */
	private final String desc;

}
