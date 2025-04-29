package com.kava.kbpd.upms.domain.basic.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysI18nId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 系统表-国际化
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysI18nEntity implements Entity<SysI18nId> {

	/**
	 * id
	 */
	private SysI18nId id;

	/**
	 * key
	 */
	private String name;

	/**
	 * 中文
	 */
	private String zhCn;

	/**
	 * 英文
	 */
	private String en;

	@Override
	public SysI18nId identifier() {
		return id;
	}
}
