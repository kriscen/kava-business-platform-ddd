package com.kava.kbpd.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kris
 * @date 2025/3/26
 * @description: yes and no enum
 */
@Getter
@AllArgsConstructor
public enum YesNoEnum {

	YES("1", "是"), NO("0", "否");

	/**
	 * 编码
	 */
	private final String code;

	/**
	 * 描述
	 */
	private final String desc;

}
