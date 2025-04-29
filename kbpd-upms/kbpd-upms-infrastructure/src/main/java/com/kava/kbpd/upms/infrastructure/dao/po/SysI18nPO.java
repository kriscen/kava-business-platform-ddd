package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 系统表-国际化
 */
@Data
@TableName("sys_i18n")
public class SysI18nPO implements Serializable {

	/**
	 * id
	 */
	@TableId
	private Long id;

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

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	private String creator;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime gmtCreate;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String modifier;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime gmtModified;

	/**
	 * 删除标记
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

}
