package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kava.kbpd.common.database.po.SysDeletablePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 系统表-国际化
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_i18n")
public class SysI18nPO extends SysDeletablePO {

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
}
